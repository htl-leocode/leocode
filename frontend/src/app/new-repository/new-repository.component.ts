import { Component, OnInit } from '@angular/core';
import {Router} from "@angular/router";
import {Teacher} from "../model/teacher.model";
import {HttpService} from "../services/http.service";
import {error} from "protractor";
import {AuthenticationService} from "../authentification/authentication.service";
import {RepositoryDTO} from "../model/repositoryDto.model";

@Component({
  selector: 'app-new-repository',
  templateUrl: './new-repository.component.html',
  styleUrls: ['./new-repository.component.css']
})
export class NewRepositoryComponent implements OnInit {

    currRepo : RepositoryDTO = {
      name: "",
      teacher: "",
      token: ""
    }

  constructor(public router: Router, private http: HttpService, private authService: AuthenticationService) {
    this.currRepo.teacher = this.authService.username.getValue();
  }

  ngOnInit(): void {
  }

  enter() {
    console.log(this.currRepo)

    this.http.postRepository(this.currRepo).subscribe()
  }
}
