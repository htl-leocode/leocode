import { Component, OnInit } from '@angular/core';
import { HttpService } from '../services/http.service';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthenticationService } from '../authentification/authentication.service';
import { TestResult } from '../model/testResult.model';

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
      if (!this.submissionStatus.includes('SUBMITTED')) {
        this.spinnerIsVisible = false;
      }
    }, error => {
      console.log(error);
    });

  }

  getClearResult(): string {

    if (this.submissionStatus == 'CORRECT') {
      return "All tests are correct"
    } else {
      let splitstring: string[] = this.testResult.split(';');
      console.log('splitstring:',splitstring);
      console.log('parsed plain object:',JSON.parse(splitstring[0]));
      let testResult: TestResult = JSON.parse(splitstring[0]);
      console.log('parsed Testresult object:',testResult);
      let testresultreturnstring:string;
      testResult.testCases.forEach(element => {
        testresultreturnstring+=element.failure.message
      });
      return testresultreturnstring;
    }
  }
}
