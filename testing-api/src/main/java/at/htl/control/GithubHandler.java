package at.htl.control;

import at.htl.entities.Repository;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.util.FileUtils;

import javax.enterprise.context.ApplicationScoped;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@ApplicationScoped
public class GithubHandler {
// https://quarkiverse.github.io/quarkiverse-docs/quarkus-jgit/dev/index.html
    public static void FetchFilesOfRepo(Repository repository, String dir) throws IOException, GitAPIException {
        // clear tmpgit folder
        File tmpGitFolder = new File(dir);
        if (tmpGitFolder.exists()) {
            FileUtils.delete(tmpGitFolder, FileUtils.RECURSIVE);
        }

        File tmpDir = Files.createTempDirectory(dir).toFile();
        try (Git git = Git.cloneRepository().setDirectory(tmpDir).setURI(repository.repoUrl).call()) {
            System.out.println("worked");
        }
    }
}
