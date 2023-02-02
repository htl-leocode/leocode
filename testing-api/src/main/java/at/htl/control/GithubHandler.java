package at.htl.control;

import at.htl.entities.Repository;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.jgit.util.FileUtils;

import javax.enterprise.context.ApplicationScoped;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@ApplicationScoped
public class GithubHandler {
    // https://quarkiverse.github.io/quarkiverse-docs/quarkus-jgit/dev/index.html
    public static void FetchFilesOfRepo(Repository repository, String dir) throws IOException, GitAPIException {
        // clear tmpgit folder
        String tmpDirPath = "tmpdir";
        File tmpGitFolder = new File(tmpDirPath);
        if (tmpGitFolder.exists()) {
            FileUtils.delete(tmpGitFolder, FileUtils.RECURSIVE);
        }
        File tmpDir = Files.createTempDirectory(tmpDirPath).toFile();
        File targetDir = new File(dir);

        try (Git git = Git.cloneRepository()
                .setDirectory(tmpDir)
                .setCredentialsProvider(
                        new UsernamePasswordCredentialsProvider(repository.teacher.ghUsername, repository.token)
                ).setURI(repository.repoUrl).call()) {
            Files.deleteIfExists(Paths.get(tmpDirPath+"/.git"));
            //Files.copy(Paths.get(tmpDir.getPath()), Paths.get(targetDir.getPath()));
            org.apache.commons.io.FileUtils.copyDirectory(tmpDir,targetDir);
            System.out.println("Successfully fetched files from repo!");
        }
    }
}
