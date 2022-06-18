package at.htl.control;

import com.cdancy.jenkins.rest.JenkinsClient;
import io.quarkus.runtime.StartupEvent;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

/*
https://www.jenkins.io/doc/book/using/remote-access-api/
https://github.com/cdancy/jenkins-rest
* */

@ApplicationScoped
public class JenkinsRequest {

    //TODO: refactor these tokens into .env file
    private static final String ADMIN_TOKEN = "11d1a936ffef3dbc7856d7d963dade76d2";
    private static final String MVN_PROJECT_TOKEN = "avFPxhFrSm6a5UQHm3Kf2Ge4oYzdz3ESuF7igg2v";

    private static final String JENKINS_JOB = "mvn-project";

    private static final String JENKINS_URL = "http://jenkins:8080";

    // for local testing only:
    //private static final String JENKINS_URL = "http://localhost:8081";

    private static final String BUILD_URL = JENKINS_URL+"/job/"+JENKINS_JOB+"/build?token="+MVN_PROJECT_TOKEN;

    private static int currentJobNumber = -1;

    @Inject
    Logger log;

    public void start(@Observes StartupEvent event) {
        // for testing/dev purposes only
        //sendRequest();
    }

    public void sendRequest() {
        // create jenkins rest-client instance
        JenkinsClient client = JenkinsClient.builder()
                .endPoint(JENKINS_URL)
                .credentials("admin:"+ADMIN_TOKEN)
                .build();

        // store current queue size
        var prevSize = client.api().queueApi().queue().size();


        log.info("send jenkins-api build request");
        // tell jenkins to start the build
        var response = client.api().jobsApi().build(null, JENKINS_JOB);
        int currSize = Integer.MAX_VALUE;

        log.info("wait for build to appear in queue");
        // wait for build to appear in queue
        while(currSize != prevSize){
            currSize = client.api().queueApi().queue().size();
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        log.info("wait for build to finish");
        // wait for build to finish
        currentJobNumber = client.api().jobsApi().lastBuildNumber(null,JENKINS_JOB);
        var buildInfo = client.api().jobsApi().buildInfo(null, JENKINS_JOB,currentJobNumber);
        while(buildInfo.building()){
            buildInfo = client.api().jobsApi().buildInfo(null, JENKINS_JOB,currentJobNumber);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        log.info("build finished");

        // log buildInfo for debugging purposes
        /*buildInfo = client.api().jobsApi().buildInfo(null, JENKINS_JOB,currentJobNumber);
        System.out.println(buildInfo);*/
    }

}
