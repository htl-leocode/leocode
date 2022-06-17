package at.htl.entities;

import java.time.LocalDateTime;

public class Submission {
    public long id;
    public String pathToProject;
    public String author;
//    public String result;
    //private SubmissionStatus status; //private so the timestamp gets updated, whenever it is set => see setter

    // replaces submissionStatus
    public SubmissionResult submissionResult;
    public LocalDateTime lastTimeChanged;
    public Example example;

    public Submission() {
        this.submissionResult = new SubmissionResult();
//        setStatus(SubmissionStatus.CREATED);
    }

    public Submission(String pathToProject, String author, Example example) {
        this();
        this.pathToProject = pathToProject;
        this.author = author;
        this.example = example;
    }

    public void setStatus(SubmissionStatus status) {
        this.submissionResult.submissionStatus = status;
        this.lastTimeChanged = LocalDateTime.now();
    }

    public SubmissionStatus getStatus() {
        return this.submissionResult.submissionStatus;
    }

    /*@Override
    public String toString() {
        return "Submission{" +
                "id=" + id +
                ", pathToProject='" + pathToProject + '\'' +
                ", author='" + author + '\'' +
                ", result='" + result + '\'' +
                ", status=" + status +
                ", lastTimeChanged=" + lastTimeChanged +
                ", example=" + example.id +
                '}';
    }*/

    @Override
    public String toString() {
        return "Submission{" +
                "id=" + id +
                ", pathToProject='" + pathToProject + '\'' +
                ", author='" + author + '\'' +
                ", submissionResult=" + submissionResult +
                ", lastTimeChanged=" + lastTimeChanged +
                ", example=" + example +
                '}';
    }
}