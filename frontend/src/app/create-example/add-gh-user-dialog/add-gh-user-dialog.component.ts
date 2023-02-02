import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {Teacher} from "../../model/teacher.model";
@Component({
  selector: 'app-add-gh-user-dialog',
  templateUrl: './add-gh-user-dialog.component.html',
  styleUrls: ['./add-gh-user-dialog.component.css']
})
export class AddGhUserDialogComponent implements OnInit {

  constructor(private dialogRef: MatDialogRef<AddGhUserDialogComponent>, @Inject(MAT_DIALOG_DATA) public data: Teacher) { }

  ngOnInit(): void {
  }

  onNoClick(){
    this.dialogRef.close()
  }

}
