package at.htl.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "LC_SUBMISSION_RESULT")
public class SubmissionResult extends PanacheEntity {
    public SubmissionStatus submissionStatus;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn
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
