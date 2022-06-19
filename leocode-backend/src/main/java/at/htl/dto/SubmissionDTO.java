package at.htl.dto;

import at.htl.entity.Submission;
import at.htl.entity.SubmissionResult;

import javax.ws.rs.PathParam;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

public class SubmissionDTO {
    public long id;
    public String status;
//    public String result;

    public SubmissionResult submissionResult; // Refactor this to SubmissionResultDTO

    public String lastTimeChanged;

    public SubmissionDTO(long id, String status, SubmissionResult submissionResult, String lastTimeChanged) {
        this.id = id;
        this.status = status;
        this.submissionResult = submissionResult;
        this.lastTimeChanged = lastTimeChanged;
    }

    public SubmissionDTO(Submission submission){
        this.id = submission.id;
        this.status = submission.getStatus().toString();
        this.submissionResult = submission.submissionResult;
        this.lastTimeChanged = submission.lastTimeChanged.atZone(ZoneId.of( "Europe/Paris" )).format(DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy"));
    }

}
