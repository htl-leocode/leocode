import {Injectable, NgZone} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Example} from '../model/example.model';
import {Submission} from '../model/submission.model';
import {Teacher} from "../model/teacher.model";
import {RepositoryDTO} from "../model/repositoryDto.model";
import {Repository} from "../model/repository.model";

@Injectable({
  providedIn: 'root'
})
export class HttpService {

  BASE_URL = 'http://0.0.0.0:9090/';

  constructor(private http: HttpClient,
              private _zone: NgZone) { }


  getTeacherList(): Observable<Teacher[]>{
    return this.http.get<Teacher[]>(this.BASE_URL + 'teacher/allTeacher');
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

  createExample(form: HTMLFormElement): Observable<Example>{
    return this.http.post<Example>(this.BASE_URL + 'example/newExample', new FormData(form));
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
}
