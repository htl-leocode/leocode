package at.htl.control;

import at.htl.entities.FailureDetails;
import at.htl.entities.TestCase;
import io.quarkus.runtime.StartupEvent;
import org.jboss.logging.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class SurefireReports {

    public void start(@Observes StartupEvent event) {
        // for test purposes only
        //List<TestCase> testCases = GetTestCases();
    }

    @Inject
    Logger logger;

    private void MoveFiles() {
        File folder = new File(".");
    }

    private String GetFilename(){
        File folder = new File("../../project-under-test/target/surefire-reports");
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                if (file.isFile()) {
                    if (file.getName().matches("TEST-.*.xml")) {
                        return (file.getAbsolutePath());
                    }
                }
            }
        }
        return "";
    }

    public List<TestCase> GetTestCases(){
        MoveFiles();
        String filename = GetFilename();
        logger.info(filename);
        List<TestCase> testCases = new ArrayList<>();

        try {
            File fXmlFile = new File(filename);
            logger.info(fXmlFile);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("testcase");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;

                    var name = eElement.getAttribute("name");
                    var className = eElement.getAttribute("classname");
                    var time = eElement.getAttribute("time");

                    TestCase testCase = new TestCase(name,className,time);

                    var elements = eElement.getElementsByTagName("failure");
                    if (elements.getLength() > 0) {
                        var failureNode = elements.item(0);
                        Element eeElement = (Element) failureNode;
                        var type = eeElement.getAttribute("type");
                        var message = failureNode.getTextContent();
                        testCase.failure = new FailureDetails(type, message);
                    }
                    testCases.add(testCase);
                }
            }

            testCases.forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return testCases;
    }
}