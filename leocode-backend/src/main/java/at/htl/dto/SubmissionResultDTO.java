package at.htl.dto;

import at.htl.entity.SubmissionStatus;
import at.htl.entity.TestCase;

import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.List;

public class SubmissionResultDTO {
    public SubmissionStatus submissionStatus;

    public List<TestCaseDTO> testCases;

    public SubmissionResultDTO(List<TestCaseDTO> testCasesDTO) {
        this();
        this.testCases = testCases;
    }

    public SubmissionResultDTO(SubmissionStatus submissionStatus, List<TestCaseDTO> testCasesDTO) {
        this.submissionStatus = submissionStatus;
        this.testCases = testCasesDTO;
    }

    public SubmissionResultDTO() {
        this.submissionStatus = SubmissionStatus.CREATED;
    }

    @Override
    public String toString() {
        return "SubmissionResult{" +
                "submissionStatus=" + submissionStatus +
                ", testCases=" + testCases +
                '}';
    }
}
