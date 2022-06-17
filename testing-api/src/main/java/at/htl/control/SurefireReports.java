package at.htl.control;

import at.htl.entities.FailureDetails;
import at.htl.entities.TestCase;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SurefireReports {

    public static void main(String[] args) {
        List<TestCase> testCases = GetTestCases();
    }

    private static String GetFilename(){
        File folder = new File(".");
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                if (file.isFile()) {
                    if (file.getName().matches(".*Test.xml")) {
                        return (file.getName());
                    }
                }
            }
        }
        return "";
    }

    public static List<TestCase> GetTestCases(){
        String filename = GetFilename();
        List<TestCase> testCases = new ArrayList<>();

        try {
            File fXmlFile = new File(filename);
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