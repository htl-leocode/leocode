package at.htl.dto;

import at.htl.entity.FailureDetails;
import at.htl.entity.Submission;
import at.htl.entity.SubmissionResult;
import at.htl.entity.TestCase;

import javax.ws.rs.PathParam;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SubmissionDTO {
    public long id;
    public String status;
//    public String result;

    public SubmissionResultDTO submissionResult; // Refactor this to SubmissionResultDTO

    public String lastTimeChanged;

    public SubmissionDTO(long id, String status, SubmissionResultDTO submissionResultDTO, String lastTimeChanged) {
        this.id = id;
        this.status = status;
        this.submissionResult = submissionResultDTO;
        this.lastTimeChanged = lastTimeChanged;
    }

    public SubmissionDTO(Submission submission){
        this.id = submission.id;
        this.status = submission.getStatus().toString();

        SubmissionResultDTO tmpSubmissionResultDto = new SubmissionResultDTO();

        tmpSubmissionResultDto.submissionStatus = submission.submissionResult.submissionStatus;

        List<TestCaseDTO> testCaseDtoList = new ArrayList<TestCaseDTO>();

        for (TestCase test: submission.submissionResult.testCases) {
             TestCaseDTO testCase = new TestCaseDTO();

             testCase.name = test.name;
             testCase.className = test.className;
             testCase.time = test.time;
             testCase.failure = new FailureDetailsDTO(test.failure.type, test.failure.message);

             testCaseDtoList.add(testCase);

        }

        tmpSubmissionResultDto.testCases = testCaseDtoList;


        this.submissionResult = tmpSubmissionResultDto;


        this.lastTimeChanged = submission.lastTimeChanged.atZone(ZoneId.of( "Europe/Paris" )).format(DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy"));
    }

}
