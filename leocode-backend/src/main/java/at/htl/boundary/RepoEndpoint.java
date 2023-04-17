package at.htl.boundary;

import at.htl.control.GitController;
import at.htl.repository.ExampleRepository;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;

@Path("/repo")
public class RepoEndpoint {

    @Inject
    Logger logger;

    @Inject
    GitController gitController;

    @Inject
    ExampleRepository exampleRepository;

    @POST
    @Path("{name}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response addRepo(@PathParam("name") String repoName,
                            @QueryParam("template") String templateRepo,
                            @QueryParam("collaborators") ArrayList<String> collaborators) {
        String repoUrl;

        try {
            repoUrl = gitController.createRepo(repoName, templateRepo, collaborators);

            if (repoUrl.startsWith("E:")) {
                return Response.status(400, repoUrl).build();
            }
        } catch (IOException e) {
            logger.error(e);
            return Response.serverError().build();
        }

        return Response.ok(repoUrl).build();
    }

    @GET
    @Path("{exampleId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRepoByExample(@PathParam("exampleId") Long exampleId) {
        return Response.ok(exampleRepository.findById(exampleId).repository).build();
    }


}
