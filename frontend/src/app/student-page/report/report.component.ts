import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-report',
  templateUrl: './report.component.html',
  styleUrls: ['./report.component.css']
})
export class ReportComponent implements OnInit {
  exercises = ['Exercise 1 - Java For Beginners: 13 of 14 Unit Tests', 'Exercise 2 - Calculator: 15 of 15 Unit Tests'];
  constructor() { }

  ngOnInit(): void {
  }

}
