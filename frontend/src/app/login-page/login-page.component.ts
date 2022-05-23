import { Component, OnInit } from '@angular/core';
import {Router} from "@angular/router";
import {AuthenticationService} from "../authentification/authentication.service";

@Component({
  selector: 'app-login-page',
  templateUrl: './login-page.component.html',
  styleUrls: ['./login-page.component.css']
})
export class LoginPageComponent implements OnInit {
  isGuest = false;
  isTeacher = false;
  isStudent = false;

  constructor(public router: Router, private authService: AuthenticationService) {
    console.log(this.authService.roles.getValue());
  }

  ngOnInit(): void {
    if(this.authService.roles.getValue()[0] == 'teacher'){
      this.isTeacher = true;
      this.router.navigateByUrl('/list-example/1');
    }
    else if(this.authService.roles.getValue()[0] == 'student'){
      this.isStudent=true;
      this.router.navigateByUrl('/list-example/0');
    }
  }

}
