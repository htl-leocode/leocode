package at.htl.boundary;

import at.htl.entities.Example;
import org.eclipse.jgit.api.Git;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Path("repos")
public class RepositoryApi {

    @Inject
    @RestClient
    ExampleService exampleService;

    // GET /github/repos
    @GET
    public Response getRepos() {
        return Response.ok("not implemented yet").build();
    }


    @GET
    @Path("{exampleId}")
    public Response pullRepo(@PathParam("exampleId") long exampleId) throws IOException {


        return Response.ok().build();
    }
}
