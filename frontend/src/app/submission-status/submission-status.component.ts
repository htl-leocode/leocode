import { Component, OnInit } from '@angular/core';
import {HttpService} from '../services/http.service';
import {ActivatedRoute, Router} from '@angular/router';
import {AuthenticationService} from '../authentification/authentication.service';

@Component({
  selector: 'app-submission-status',
  templateUrl: './submission-status.component.html',
  styleUrls: ['./submission-status.component.css']
})
export class SubmissionStatusComponent implements OnInit {

  submissionId: number;
  submissionStatus: string;
  spinnerIsVisible = true;
  testResult = '';

  constructor(private http: HttpService,
              private route: ActivatedRoute,
              public router: Router,
              public authService: AuthenticationService) { }

  ngOnInit(): void {
    this.submissionId = +this.route.snapshot.paramMap.get('id');
    this.getSubmissionStatus(this.submissionId);
  }

  getSubmissionStatus(id: number): void {
    this.http.getSubmissionStatusSse(id).subscribe(messageEvent => {
      this.submissionStatus = messageEvent.data.split(';')[0];
      this.testResult = messageEvent.data.split(';')[1] === undefined ? '' : messageEvent.data.split(';')[1];
      console.log(messageEvent.data);
      if (!this.submissionStatus.includes('SUBMITTED')){
        this.spinnerIsVisible = false;
      }
    }, error => {
      console.log(error);
    });

  }

}
