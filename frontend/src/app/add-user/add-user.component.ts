import { Component, OnInit } from '@angular/core';
import {HttpService} from "../services/http.service";
import {Router} from "@angular/router";
import {AuthenticationService} from "../authentification/authentication.service";
import {Teacher} from "../model/teacher.model";

@Component({
  selector: 'app-add-user',
  templateUrl: './add-user.component.html',
  styleUrls: ['./add-user.component.css']
})
export class AddUserComponent implements OnInit {

  teacher: Teacher = {
    name: "",
    ghUsername: "",
    id: 0
  }

  constructor(private http: HttpService,
              public router: Router, private authService: AuthenticationService) {
    this.teacher.name = this.authService.username.getValue();
  }

  add(){
    //console.log(this.teacher);
    this.http.postTeacher(this.teacher);
  }

  ngOnInit(): void {
  }

}
