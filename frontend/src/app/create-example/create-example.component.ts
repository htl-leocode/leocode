import { Component, OnInit } from '@angular/core';
import {HttpService} from '../services/http.service';
import {Router} from '@angular/router';
import {Repository} from "../model/repository.model";
import {AuthenticationService} from "../authentification/authentication.service";

@Component({
  selector: 'app-create-example',
  templateUrl: './create-example.component.html',
  styleUrls: ['./create-example.component.css']
})
export class CreateExampleComponent implements OnInit {

  form: HTMLFormElement;

  repositories: Repository[];

  checkPublic: boolean = false;

  teacherName: string = "";

  constructor(private http: HttpService,
              public router: Router, private authService: AuthenticationService) {
    this.teacherName = this.authService.username.getValue();
  }

  ngOnInit(): void {
    this.http.getRepositories(this.authService.username.getValue()).subscribe({
      next: data =>{
        data.forEach(p => console.log(p));
        this.repositories = data;
      }
    })
  }

  upload(): void {
    this.form = document.forms.namedItem('createExampleForm');
    if (this.form.checkValidity()) {
      this.http.createExample(this.form).subscribe(
        data => {
          this.router.navigate(['example', data.id]);
          },
        error => {
          alert('Sorry there has been an error!');
          console.log(error);
        }
        );
    }
  }

}
