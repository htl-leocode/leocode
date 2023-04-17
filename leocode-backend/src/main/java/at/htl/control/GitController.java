package at.htl.control;

import at.htl.entity.Example;
import at.htl.entity.Repository;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.OrTreeFilter;
import org.eclipse.jgit.treewalk.filter.PathFilter;
import org.eclipse.jgit.treewalk.filter.TreeFilter;
import org.eclipse.jgit.util.FileUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;
import org.kohsuke.github.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@ApplicationScoped
public class GitController {

    @Inject
    Logger log;

    @ConfigProperty(name = "LEOCODE_REPOS_TOKEN")
    String publicRepoToken;

    public static Git addOrInsertToGit(Git g, Example example, String token) {
        try {

            g.add().addFilepattern(Objects.requireNonNull(".")).call();

            g.commit().setMessage("The Example " + example.name + "was created by LeoCode").call();

            g.push().setCredentialsProvider(new UsernamePasswordCredentialsProvider(example.repository.teacher.ghUsername, token)).setRemote("origin").call();

        } catch (GitAPIException e) {
            throw new RuntimeException(e);
        }

        return g;

    }

    public Git cloneRepositoryWithNoCheckoutToDir(File dir, Repository repository) {
        try {
            if (repository.token != null) {

                log.info(repository.repoUrl);

                return Git.cloneRepository().setURI(repository.repoUrl).setDirectory(dir).setNoCheckout(true).setCredentialsProvider(new UsernamePasswordCredentialsProvider(repository.teacher.ghUsername, repository.token)).call();
            }

            return Git.cloneRepository().setURI(repository.repoUrl).setDirectory(dir).call();
        } catch (GitAPIException e) {
            throw new RuntimeException(e);
        }
    }

    public Git cloneRepositoryToDir(File dir, Repository repository) {
        try {
            if (repository.token != null) {
                return Git.cloneRepository().setURI(repository.repoUrl).setDirectory(dir).setCredentialsProvider(new UsernamePasswordCredentialsProvider(repository.teacher.ghUsername, repository.token)).call();
            }

            return Git.cloneRepository().setURI(repository.repoUrl).setDirectory(dir).call();
        } catch (GitAPIException e) {
            throw new RuntimeException(e);
        }
    }

    public void checkoutFolder(File dir, Example example) {

        try {

            //String url = "https://github.com/" + example.repository.teacher.ghUsername + "/" + example.repository.name + ".git";
            String hash = "origin/master";
            String subPath = "NewTestingExample";

            UsernamePasswordCredentialsProvider upcp = new UsernamePasswordCredentialsProvider("raphaelabl", "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");

            importExample("https://github.com/raphaelabl/NewLeocodeTest.git", "v1.0.0", new File("target", "examples"), upcp, "NewTestingExample");

        } catch (GitAPIException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private static void importExample(String repoUrl, String tag, File destination, UsernamePasswordCredentialsProvider upcp, String... exampleNames) throws Exception {


        File target = new File("target/", "" + Math.abs(repoUrl.hashCode()));

        Git git = getGit(repoUrl, target, upcp);

        org.eclipse.jgit.lib.Repository repository = git.getRepository();
        ObjectId tree = repository.resolve((tag == null ? "HEAD" : tag) + "^{tree}");
        TreeWalk treeWalk = new TreeWalk(repository);
        treeWalk.setRecursive(false);

        File baseDir = destination;
        treeWalk.addTree(tree);
        treeWalk.setFilter(createFilter(exampleNames));
        while (treeWalk.next()) {
            if (treeWalk.isSubtree()) {
                treeWalk.enterSubtree();
            } else {
                File f = new File(baseDir, treeWalk.getPathString());
                FileUtils.mkdirs(f.getParentFile(), true);
                ObjectId id = treeWalk.getObjectId(0);
                try (FileOutputStream fos = new FileOutputStream(f)) {
                    repository.open(id).copyTo(fos);
                }
            }
        }
        //treeWalk.release();

        System.err.println(String.format("Imported %s from %s", exampleNames == null || exampleNames.length == 0 ? "everything" : Arrays.asList(exampleNames), repoUrl));
    }

    private static TreeFilter createFilter(String[] exampleNames) {
        if (exampleNames == null || exampleNames.length == 0) {
            return TreeFilter.ALL;
        }
        if (exampleNames.length == 1) {
            return PathFilter.create(exampleNames[0]);
        }
        List<TreeFilter> filters = new ArrayList<>(exampleNames.length);
        for (String exampleName : exampleNames) {
            filters.add(PathFilter.create(exampleName));
        }
        return OrTreeFilter.create(filters);
    }

    private static Git getGit(String repoUrl, File target, UsernamePasswordCredentialsProvider upcp) throws Exception {

        if (target.exists()) {
            System.err.println("Opening clone of " + repoUrl);
            return Git.open(target);
        }

        CloneCommand clone = Git.cloneRepository().setCredentialsProvider(upcp).setDepth(1)
                //.setBranch("master")
                .setCloneAllBranches(true).setNoCheckout(true).setURI(repoUrl).setDirectory(target)
                // .setProgressMonitor(monitor )
                ;
        System.err.println("Cloning " + repoUrl);
        Git git = clone.call();

        return git;
    }


    public String createRepo(String repoName, String templateRepo, List<String> collaborators) throws IOException {
        GitHub github = GitHub.connect("leocode-repos", publicRepoToken);
        GHOrganization org = github.getOrganization("leocode-repos");
        GHCreateRepositoryBuilder repoBuilder = org.createRepository(repoName);

        repoBuilder.private_(false);
        repoBuilder.fromTemplateRepository("leocode-repos", templateRepo);
        repoBuilder.owner("leocode-repos");

        GHRepository repo = null;
        try {
            repo = repoBuilder.create();
        } catch (IOException e) {
            if (e.getMessage().contains("Name already exists on this account")) {
                return String.format("E: Repository name %s already in use", repoName);
            }
        }

        if (repo == null) {
            return "E: Unknown server error";
        }

        for (String collaborator : collaborators) {
            try {
                GHUser user = github.getUser(collaborator);
                repo.addCollaborators(GHOrganization.RepositoryRole.from(GHOrganization.Permission.ADMIN), user);
            } catch (IOException e) {
                if (e.getMessage().contains("org.kohsuke.github.GHFileNotFoundException")) {
                    return String.format("E: Invalid user %s", collaborator);
                }
            }
        }
        URL url = repo.getHtmlUrl();
        return url.toString();

    }

}
