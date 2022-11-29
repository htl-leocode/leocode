package at.htl.kafka;

import at.htl.control.FileHandler;
import at.htl.entities.SubmissionResult;
import at.htl.entities.SubmissionStatus;
import at.htl.entities.Submission;
import at.htl.entities.TestCase;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

@ApplicationScoped
public class SubmissionListener {

    @Inject
    Logger log;

    @Inject
    FileHandler fileHandler;

    @Inject
    SubmissionProducer submissionProducer;

    @Incoming("submission-input")
    public void listen(Submission s) {
        if (s.getStatus().equals(SubmissionStatus.SUBMITTED)) {
            log.info("Received Message: " + s.toString());
            Runnable runnable = () -> {
                List<TestCase> testResults = fileHandler.testProject(s.pathToProject, s.example.type, s.example.whitelist, s.example.blacklist);
                s.submissionResult = new SubmissionResult(testResults);
                //s.result = fileHandler.testProject(s.pathToProject, s.example.type, s.example.whitelist, s.example.blacklist);

                //TODO: re-add whitelist and blacklist functionality after migration to new test-system
                /*if(s.result.toLowerCase().contains("whitelist") || s.result.toLowerCase().contains("blacklist")){
                    s.setStatus(SubmissionStatus.FAILED);
                } else {
                    //s.setStatus(fileHandler.evaluateStatus(s.submissionResult));
                }*/
                fileHandler.evaluateStatus(s.submissionResult);

                submissionProducer.sendSubmition(s);
            };
            new Thread(runnable).start();
        }
    }
}
