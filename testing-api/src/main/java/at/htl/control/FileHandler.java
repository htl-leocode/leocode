package at.htl.control;

import at.htl.entities.ExampleType;
import at.htl.entities.SubmissionResult;
import at.htl.entities.SubmissionStatus;
import at.htl.entities.TestCase;
import at.htl.util.PathConverter;
import io.quarkus.runtime.Startup;
import io.quarkus.runtime.StartupEvent;
import org.apache.commons.io.FileUtils;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Startup
@ApplicationScoped
public class FileHandler {
    private final Path BUILD_RESULT = Paths.get("../result.txt");

    public Path pathToProject;
    public HashMap<Path, String> currentFiles;

    @Inject
    Logger log;

    @Inject
    SurefireReports surefireReports;

    @Inject
    JenkinsRequest jenkinsRequest;

    //Pull image at the beginning, so testing goes faster
    void onStart(@Observes StartupEvent ev)  {

    }

    public List<TestCase> testProject(String projectPath, ExampleType type, Set<String> whitelist, Set<String> blacklist) {
        String shortTestPath = PathConverter.ExtractFolderName(type,projectPath); // MAVEN/project-under-test-x
        String fullTestPath = "../project-under-test/"+shortTestPath; // ../projects-under-test/MAVEN/project-under-test-x

        setup(projectPath,fullTestPath);
        unzipProject(fullTestPath);

        String resWhitelist;
        String resBlacklist;

        //TODO: re-add white- and blacklist functionality after migration to new test-system
        /*if((resWhitelist = checkWhiteOrBlacklist("whitelist", whitelist)) != null) {
            return resWhitelist;
        } else if ((resBlacklist = checkWhiteOrBlacklist("blacklist", blacklist)) != null) {
            return resBlacklist;
        } else {*/
            try {
                switch (type){
                    case MAVEN:
                        createMavenProjectStructure(fullTestPath);
                        //runTests();
                        log.info("start testing");
                        jenkinsRequest.sendRequest(shortTestPath); // tell jenkins to start testing, since the files are ready
                        log.info("finished testing");
                    break;
                    default:
                        throw new Exception("Project Type not supported yet!");
                }
                return getResult(fullTestPath);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("Exception in testProject() in FileHandler");
                log.error(e);
                //return "Oops, something went wrong!";
                return null;
            }
        /*}*/

    }

    public void setup(String projectPath, String fullTestPath) {
        try {
            log.info("setup test environment");
            pathToProject = Paths.get(projectPath);
            currentFiles = new HashMap<Path, String>();

            Path dir = Paths.get(fullTestPath);
            File projectDirectory = dir.toFile();

            if (projectDirectory.exists()) {
                log.info("flushing " + projectDirectory.getPath());
                FileUtils.cleanDirectory(projectDirectory);
            } else {
                log.info("creating " + projectDirectory.getPath());
                Files.createDirectories(dir,
                        PosixFilePermissions.asFileAttribute(
                                PosixFilePermissions.fromString("rwxrwxrwx")
                        ));

            }

            if (BUILD_RESULT.toFile().exists()) {
                Files.delete(BUILD_RESULT);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void unzipProject(String folderName) {
        try (ZipInputStream zis = new ZipInputStream(Files.newInputStream(pathToProject))) {
            log.info("unzipping " + pathToProject);
            File dest = Paths.get(folderName).toFile();

            ZipEntry zipEntry = zis.getNextEntry();
            while (zipEntry != null) {
                File newFile = new File(dest + "/" + zipEntry.toString());

                if (zipEntry.toString().contains("/")) {
                    if (!newFile.getParentFile().exists()) {
                        newFile.getParentFile().mkdirs();
                    }
                    currentFiles.put(newFile.toPath() ,"test");
                } else if (newFile.getPath().contains(".java")) {
                    currentFiles.put(newFile.toPath(), "code");
                } else {
                    currentFiles.put(newFile.toPath(), "other");
                }

                newFile.createNewFile();

                FileOutputStream fos = new FileOutputStream(newFile);
                fos.write(zis.readAllBytes());
                fos.close();

                zipEntry = zis.getNextEntry();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createMavenProjectStructure(String fullFilePath) {
        log.info("create Maven Project Structure");

        currentFiles.forEach((k, v) -> {
            File file = k.toFile();
            StringBuilder fileDestination = new StringBuilder().append(fullFilePath);

            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                fileDestination.append("/src/");

                switch (v) {
                    case "test":
                        fileDestination.append("test");
                        break;
                    case "code":
                        fileDestination.append("main");
                        break;
                    case "other": //Files already exist at the correct path (../project-under-test/)
                        return;
                }

                fileDestination.append("/java/");

                //evaluate packages & filename

                String packages = br.readLine();
                log.info(packages);
                packages = packages
                        .substring(
                                packages.lastIndexOf(" ") + 1,
                                packages.lastIndexOf(";"))
                        .replace(".", "/");
                String filename = k.toString().substring(k.toString().lastIndexOf("/"));

                fileDestination.append(packages).append(filename);

                //create Directories
                String directoriesOnly = fileDestination.substring(0, fileDestination.lastIndexOf("/") + 1);
                File directories = new File(directoriesOnly);
                if (!directories.exists()) {
                    directories.mkdirs();
                }

                //move actual File
                file.renameTo(new File(fileDestination.toString()));

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        //Delete temporary directory
        Paths.get(fullFilePath + "/test").toFile().delete();
    }


    public List<TestCase> getResult(String testPath){
        log.info("getResult");
        List<TestCase> testCases = surefireReports.GetTestCases(testPath);
        return testCases;
    }

    public void evaluateStatus(SubmissionResult result){
        log.info("evaluateStatus");
        SubmissionStatus status = SubmissionStatus.ERROR;

        if(result == null || result.testCases == null || result.testCases.size() == 0){
            log.error("results are null");
            status = SubmissionStatus.ERROR;
        }

        log.info(result.testCases);

        if(result.testCases.stream().anyMatch(new Predicate<TestCase>() {
            @Override
            public boolean test(TestCase testCase) {
                return testCase.failure != null;
            }
        }))
        {
            status = SubmissionStatus.FAILED;
        }
        else if(result == null || result.testCases == null || result.testCases.size() == 0){
            status = SubmissionStatus.ERROR;
        }
        else {
            status = SubmissionStatus.CORRECT;
        }
        result.submissionStatus = status;

        /*List<String> resultList = List.of(result.split("\n"));
        result = resultList.get(resultList.size()-1);

        result = result.substring(result.lastIndexOf(" ") + 1);

        SubmissionStatus status;
        switch (result){
            case "FAILURE":
                status = SubmissionStatus.FAILED;
                break;
            case "SUCCESS":
                status = SubmissionStatus.CORRECT;
                break;
            default:
                status = SubmissionStatus.ERROR;
                break;
        }
        return status;*/
    }

    public String checkWhiteOrBlacklist(String type, Set<String> list){
        List<Map.Entry<Path, String>> currentCodeFiles = currentFiles.entrySet().stream()
                .filter(pathStringEntry -> pathStringEntry.getValue().equals("code"))
                .collect(Collectors.toList());

        for(Map.Entry<Path, String> e: currentCodeFiles) {
            for(String s : list) {
                try{
                    switch (type.toLowerCase()){
                        case "blacklist":
                            log.info("checking Blacklist");
                            int c = checkForUsage(s, e.getKey().toFile());
                            if(c >= 0){
                                log.info("Blacklist Error: " + s + " has been used at line "+ c + "!");
                                return "Blacklist Error: " + s + " has been used at line "+ c + "!";
                            }
                            break;
                        case "whitelist":
                            log.info("checking Whitelist");
                            if(checkForUsage(s, e.getKey().toFile()) < 0){
                                log.info("Whitelist Error: " + s + " has not been used!");
                                return "Whitelist Error: " + s + " has not been used!";
                            }
                            break;
                        default:
                            throw new IOException();
                    }

                } catch (IOException ioException) {
                    ioException.printStackTrace();
                    return "Sorry, there has been an unknown error!";
                }
            }
        }
        return null;
    }

    public int checkForUsage(String needle, File haystack) throws IOException {
        int lineNumber = 0;
        try(BufferedReader br = new BufferedReader(new FileReader(haystack))) {
            String line;
            while((line = br.readLine())!= null) {
                lineNumber++;
                String[] words = line.split(" ");
                for (String w: words) {
                    if(w.equalsIgnoreCase(needle)){
                        return lineNumber;
                    }
                }
            }
        }
        return -1;
    }
}
