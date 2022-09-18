package at.htl.util;

import at.htl.entities.ExampleType;

import javax.swing.text.html.FormSubmitEvent;

public class PathConverter {


    /**
     * @param exampleType Specifies which build tool is used (MAVEN, ANT, ...)
     * @param projectPath Full folder path, e.g.: "../projects-in-queue/project-under-test-7.zip"
     * @return a folderName of the format: "MAVEN/project-under-test-7"
     */
    public static String ExtractFolderName(ExampleType exampleType, String projectPath) {
        return exampleType.name() + "/"
                + projectPath.substring(projectPath.lastIndexOf("/") + 1, projectPath.lastIndexOf("."));
    }
}
