package at.htl.boundary;

import at.htl.control.GitController;
import at.htl.dto.GithubExampleDTO;
import at.htl.entity.Example;
import at.htl.entity.ExampleType;
import at.htl.entity.Repository;
import at.htl.entity.Teacher;
import at.htl.repository.ExampleRepository;
import at.htl.repository.TeacherRepository;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Path("/repo")
public class RepoEndpoint {

    @Inject
    Logger logger;

    @Inject
    TeacherRepository teacherRepository;

    @Inject
    GitController gitController;

    @Inject
    ExampleRepository exampleRepository;

    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    @Transactional
    public Response addRepo(GithubExampleDTO example) {
        String repoUrl;

        if (example.isPubl()) {
            logger.info("Creating public repo");
            try {
                repoUrl = gitController.createRepo(
                        example.getName(),
                        ExampleType.values()[example.getType()].toString().toLowerCase() + "-template",
                        example.getCollaborators()
                );

                if (repoUrl.startsWith("E:")) {
                    return Response.status(400, repoUrl).build();
                }
            } catch (IOException e) {
                logger.error(e);
                return Response.serverError().build();
            }
        } else {
            logger.info("Creating private repo");
            repoUrl = example.getRepoUrl();
        }

        String username = example.getCollaborators().get(0);
        Teacher t = username != null ? teacherRepository.getTeacherByGhName(username) : null;

        logger.info(example.getCollaborators().get(0));

        Example persistExample = new Example();
        persistExample.name = example.getName();
        persistExample.description = example.getDescription();
        persistExample.type = ExampleType.values()[example.getType()];
        persistExample.repository = new Repository(
                repoUrl,
                t,
                example.getRepoToken()
        );

        persistExample.persist();

        return Response.ok(repoUrl).build();
    }

    @GET
    @Path("{exampleId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRepoByExample(@PathParam("exampleId") Long exampleId) {
        return Response.ok(exampleRepository.findById(exampleId).repository).build();
    }

    @GET
    @Path("/getReadmeOfExample/{exampleId}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getReadmeOfRepo(@PathParam("exampleId") long exampleId) throws IOException {
        Repository repo = exampleRepository.findById(exampleId).repository;
        String repoUrl = repo.repoUrl;
        var splitContent = repoUrl.replace(".git", "").split("/");
        var editedUrl = splitContent[splitContent.length - 2] + "/" + splitContent[splitContent.length - 1];
        logger.info(repo.token);

        String readme = gitController.getReadmeOfRepo(editedUrl, repo.token);

        return Response.ok(readme).build();
    }
}
