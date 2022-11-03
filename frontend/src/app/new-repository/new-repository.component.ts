import { Component, OnInit } from '@angular/core';
import {Router} from "@angular/router";
import {Teacher} from "../model/teacher.model";
import {HttpService} from "../services/http.service";
import {error} from "protractor";
import {AuthenticationService} from "../authentification/authentication.service";

@Component({
  selector: 'app-new-repository',
  templateUrl: './new-repository.component.html',
  styleUrls: ['./new-repository.component.css']
})
export class NewRepositoryComponent implements OnInit {

  teachers: string[] = []

  name: string = "";
  teacherName: string;

  constructor(public router: Router, private http: HttpService, private authService: AuthenticationService) {
    this.teacherName = this.authService.username.getValue();
  }

  ngOnInit(): void {
    this.http.getTeacherList().subscribe({
      next: data => this.teachers = data.map(a => a.name),
      error: err => console.log(err)
    })
  }

  enter() {
    console.log("Teacher:",this.teacherName);
    console.log(this.name)

    this.http.postRepository({
       name: this.name,
       teacher: this.teacherName
    }).subscribe()
  }
}
