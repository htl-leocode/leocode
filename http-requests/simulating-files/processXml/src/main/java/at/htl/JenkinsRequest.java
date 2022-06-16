package at.htl;

import com.cdancy.jenkins.rest.JenkinsClient;
import com.cdancy.jenkins.rest.domain.system.SystemInfo;

/*
https://www.jenkins.io/doc/book/using/remote-access-api/
https://github.com/cdancy/jenkins-rest
* */

public class JenkinsRequest {
    private static final String URL = "http://127.0.0.1:8081/job/mvn-project/build?token=avFPxhFrSm6a5UQHm3Kf2Ge4oYzdz3ESuF7igg2v";

    public static void main(String[] args) {
        sendRequest(URL);
    }

    public static void sendRequest(String url) {
        JenkinsClient client = JenkinsClient.builder()
                .endPoint(url)
                .credentials("admin:11d1a936ffef3dbc7856d7d963dade76d2")
                .build();

        SystemInfo systemInfo = client.api().systemApi().systemInfo();
    }
}
