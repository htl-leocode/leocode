package at.htl.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Entity
@Table(name = "LC_SUBMISSIONS")
public class Submission extends PanacheEntity {
    public String pathToProject;
    public String author;

    /*public String result = "";
    @Enumerated(value = EnumType.STRING)
    private SubmissionStatus status; //private so the timestamp gets updated, whenever it is set => see setter
    */

    @OneToOne(cascade = CascadeType.ALL)
    public SubmissionResult submissionResult;

    @Column(columnDefinition = "TIMESTAMP")
    public LocalDateTime lastTimeChanged;

    @ManyToOne
    @JoinColumn(name = "example")
    public Example example;

    public Submission() {
        this.submissionResult = new SubmissionResult();
        //setStatus(SubmissionStatus.CREATED);
    }

    public Submission(String pathToProject, String author, Example example) {
        this();
        this.pathToProject = pathToProject;
        this.author = author;
        this.example = example;
    }

    public void setStatus(SubmissionStatus status) {
        this.submissionResult.submissionStatus = status;
        this.lastTimeChanged = LocalDateTime.now(ZoneId.of( "Europe/Paris" ));
    }

    public SubmissionStatus getStatus() {
        return this.submissionResult.submissionStatus;
    }

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
