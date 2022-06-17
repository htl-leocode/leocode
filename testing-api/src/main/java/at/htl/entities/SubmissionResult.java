package at.htl.entities;

import java.util.List;

public class SubmissionResult {
    public SubmissionStatus submissionStatus;

    public List<TestCase> testCases;

    public SubmissionResult(List<TestCase> testCases) {
        this();
        this.testCases = testCases;
    }

    public SubmissionResult(SubmissionStatus submissionStatus, List<TestCase> testCases) {
        this.submissionStatus = submissionStatus;
        this.testCases = testCases;
    }

    public SubmissionResult() {
        this.submissionStatus = SubmissionStatus.CREATED;
    }


}
