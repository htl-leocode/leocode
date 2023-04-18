import {Component, OnInit} from '@angular/core';
import {HttpService} from '../services/http.service';
import {Router} from '@angular/router';
import {AuthenticationService} from "../authentification/authentication.service";
import {MatDialog, MatDialogRef} from "@angular/material/dialog";
import {AddGhUserDialogComponent} from "./add-gh-user-dialog/add-gh-user-dialog.component";
import {Teacher} from "../model/teacher.model";


@Component({
  selector: 'app-create-example',
  templateUrl: './create-example.component.html',
  styleUrls: ['./create-example.component.css']
})
export class CreateExampleComponent implements OnInit {

  form: HTMLFormElement;
  checkPublic: boolean = true;
  selectedType: string = "";

  repositoryName: string = "";

  description: string = "";
  public types: string[] = ['maven-template','C#','Go','Python','C','Javascript']



  teacher: Teacher = {
    name: "",
    ghUsername: "",
    id: 0
  };

  ghUser: string = ""

  constructor(private http: HttpService,
              public dialog: MatDialog,
              public router: Router, private authService: AuthenticationService) {
    this.teacher.name = this.authService.username.getValue();
  }

  ngOnInit(): void {

    this.http.getTeacher(this.authService.username.getValue()).subscribe(
      data => {
        if (data == null) {
          //TODO
          const dialogRef = this.dialog.open(AddGhUserDialogComponent, {data: this.teacher});

          dialogRef.afterClosed().subscribe({
            next: value => {
              this.teacher.ghUsername = value;
              this.http.postTeacher(this.teacher).subscribe();
            }
          })
        }else{
          this.teacher = data;
        }
      }
    );

  }

  upload(): void {

      this.http.createExample(this.repositoryName, this.description, this.selectedType, this.teacher.ghUsername).subscribe(
        data => {
          console.log("test",data)
        },
        error => {
          alert('Sorry there has been an error!');
          console.log(error);
        }
      );
  }


  setType(type: string) {
      this.selectedType = type;
  }



}
