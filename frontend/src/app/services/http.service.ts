import {Injectable, NgZone} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Example} from '../model/example.model';
import {Submission} from '../model/submission.model';
import { environment } from 'src/environments/environment';
import {Teacher} from "../model/teacher.model";
import {RepositoryDTO} from "../model/repositoryDto.model";
import {Repository} from "../model/repository.model";

@Injectable({
  providedIn: 'root'
})
export class HttpService {
  // when running outside of docker:
  //BASE_URL = 'http://localhost:9090/';

  BASE_URL = environment.BACKEND_URL;
  //BASE_URL = 'http://localhost:4200/api/';

  constructor(private http: HttpClient,
              private _zone: NgZone) { }


  getTeacher(teacherName: string): Observable<Teacher>{
    return this.http.get<Teacher>(this.BASE_URL + 'teacher/getGhUsername/'+teacherName);
  }

  getTeacherList(): Observable<Teacher[]>{
    return this.http.get<Teacher[]>(this.BASE_URL + 'teacher/allTeacher');
  }

  postTeacher(teacher: Teacher){
    return this.http.post(this.BASE_URL + 'teacher/add', teacher);
  }

  getRepositories(teacherName: string): Observable<Repository[]>{
    return this.http.get<Repository[]>(this.BASE_URL + 'teacher/repositoryByTeacher/'+teacherName)
  }

  postRepository(repository: RepositoryDTO){
    return this.http.post(this.BASE_URL + 'teacher/newRepository', repository)
  }

  getExampleList(): Observable<Example[]> {
    return this.http.get<Example[]>(this.BASE_URL + 'example/list');
  }

  getExampleById(id: number): Observable<Example>{
    return this.http.get<Example>(this.BASE_URL  + 'example/' + id);
  }

  createExample(name: string, description: string, type: string, collaborator: string): Observable<string>{
    return this.http.post(this.BASE_URL + "repo/create",
      {
              name: name,
              description: description,
              type: type,
              collaborators: [collaborator]
      },{responseType: 'text'});
  }

  testExample(form: FormData): Observable<any>{
    return this.http.post<any>(this.BASE_URL + 'submission', form);
  }

  getSubmissionStatusSse(id: number): Observable<MessageEvent> {

    return Observable.create(observer => {
      const eventSource = new EventSource(this.BASE_URL + 'submission/' + id);
      eventSource.onmessage = event => {
        this._zone.run(() => {
          observer.next(event);
        });
      };
      eventSource.onerror = error => {
        this._zone.run(() => {
          observer.error(error);
        });
      };
    });
  }

  getFinishedSubmissions(username: string): Observable<Submission[]> {
    return this.http.get<Submission[]>(this.BASE_URL + 'submission/history/' + username);
  }

  getRepoByExample(exampleId: number) {
    return this.http.get<Repository>(this.BASE_URL + 'repo/'+exampleId);
  }

  getReadmeFromRepo(repoUrl: string) {
    return this.http.get(`https://raw.githubusercontent.com/${repoUrl}/main/README.md`,
    {responseType: 'text'});
  }
}
