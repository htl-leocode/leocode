package at.htl.control;

import com.cdancy.jenkins.rest.JenkinsClient;
import com.cdancy.jenkins.rest.domain.system.SystemInfo;

/*
https://www.jenkins.io/doc/book/using/remote-access-api/
https://github.com/cdancy/jenkins-rest
* */

public class JenkinsRequest {

    //TODO: refactor these tokens into .env file
    private static final String ADMIN_TOKEN = "11d1a936ffef3dbc7856d7d963dade76d2";
    private static final String MVN_PROJECT_TOKEN = "avFPxhFrSm6a5UQHm3Kf2Ge4oYzdz3ESuF7igg2v";

    private static final String JENKINS_URL = "http://jenkins:8080";

    private static final String URL = JENKINS_URL+"/job/mvn-project/build?token="+MVN_PROJECT_TOKEN;

    public static void main(String[] args) {
        sendRequest();
    }

    public static void sendRequest() {
        JenkinsClient client = JenkinsClient.builder()
                .endPoint(URL)
                .credentials("admin:"+ADMIN_TOKEN)
                .build();

        SystemInfo systemInfo = client.api().systemApi().systemInfo();
    }
}
