package at.htl;

import at.htl.entity.Failure;
import at.htl.entity.TestCase;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String filename = "";
        List<TestCase> testCases = new ArrayList<>();

        File folder = new File(".");
        File[] listOfFiles = folder.listFiles();
        for (File file : listOfFiles) {
            if (file.isFile()) {
                if (file.getName().matches(".*xml")) {
                    filename = file.getName();
                }
            }
        }

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
                        Failure failure = new Failure(type, message);
                        testCase.failure = failure;
                    }
                    testCases.add(testCase);
                }
            }

            testCases.forEach(testCase -> System.out.println(testCase));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}