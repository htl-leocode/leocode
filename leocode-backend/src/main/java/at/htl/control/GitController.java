package at.htl.control;

import at.htl.entity.Example;

import at.htl.entity.Repository;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

@ApplicationScoped
public class GitController {

    @Inject
    Logger log;

    public static Git addOrInsertToGit(Git g, Example example, String token){
        try {

            g.add().addFilepattern(Objects.requireNonNull(".")).call();
            g.commit().setMessage("The Example "+ example.name +"was created by LeoCode").call();
            g.push().setCredentialsProvider(
                    new UsernamePasswordCredentialsProvider(example.repository.teacher.ghUsername, token)
            ).setRemote("origin").call();

        } catch (GitAPIException e) {
            throw new RuntimeException(e);
        }

        return g;

    }

    public Git cloneRepositoryToDir(File dir, Repository repository) {
        try {
            if(repository.token != null){
                return Git.cloneRepository()
                        .setURI("https://github.com/" + repository.teacher.ghUsername + "/" + repository.name + ".git")
                        .setDirectory(dir)
                        .setCredentialsProvider(
                                new UsernamePasswordCredentialsProvider(repository.teacher.ghUsername, repository.token)
                        ).call();
            }

            return Git.cloneRepository()
                    .setURI("https://github.com/" + repository.teacher.ghUsername + "/" + repository.name + ".git")
                    .setDirectory(dir)
                    .call();
        } catch (GitAPIException e) {
            throw new RuntimeException(e);
        }
    }
}
