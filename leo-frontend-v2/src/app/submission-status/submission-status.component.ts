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

  submissionId: number = -1;
  submissionStatus: string = "";
  spinnerIsVisible = true;
  testResult = '';

  result: TestResult = {submissionStatus: "", testCases: []};
  nrOfTests: number = 0;
  testsPassed: number = 0;

  constructor(private http: HttpService,
    private route: ActivatedRoute,
    public router: Router,
    public authService: AuthenticationService) { }

  ngOnInit(): void {
    const res = this.route.snapshot.paramMap.get('id');
    if(res != null){
      this.submissionId = +res;
    }
    this.getSubmissionStatus(this.submissionId);
  }

  getSubmissionStatus(id: number): void {
    this.http.getSubmissionStatusSse(id).subscribe(messageEvent => {
      console.log("spinnerstatus: ", this.spinnerIsVisible);

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

  getClearResult() {
    console.log("typeof result:",typeof(this.result));
    console.log("result",this.result);

    console.log("spinnerVisible: ", this.spinnerIsVisible);



    if (this.submissionStatus == 'CORRECT') {
      //return "Congrats! All tests passed :)"
    } else {
      let splitstring: string[] = this.testResult.split(';');
      console.log('splitstring:',splitstring);
      let obj = JSON.parse(splitstring[0]);

      this.result = JSON.parse(splitstring[0]);

      console.log('parsed Testresult object:',this.result);
      console.log('x',this.result.testCases[0].failure.message);

      this.nrOfTests = this.result.testCases.length;
      this.testsPassed = this.result.testCases.length;

      this.result.testCases.forEach(element => {
        if(element.failure != null){
          this.testsPassed = this.testsPassed - 1;
        }
      });
    }
  }
}
