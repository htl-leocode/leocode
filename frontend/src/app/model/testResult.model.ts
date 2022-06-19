import { TestCase } from "./testCase.model"

export interface TestResult {
    submissionStatus:string,
    testCases: TestCase[];

}