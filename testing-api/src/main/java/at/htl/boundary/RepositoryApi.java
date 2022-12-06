package at.htl.boundary;

import at.htl.entities.Example;
import org.eclipse.jgit.api.Git;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Set;

@Produces(MediaType.APPLICATION_JSON)
@Path("repos")
public class RepositoryApi {

    @Inject
    @RestClient()
    ExampleService exampleService;

    @GET
    public Response getRepos() {
        return Response.ok("not implemented yet!!").build();
    }

    @GET
    @Path("{id}")
    public Set<Example> getExampleById(@PathParam("id") long id) {
        return null;
    }


    @GET
    @Path("{exampleId}")
    public Response pullRepo(@PathParam("exampleId") long exampleId) throws IOException {


        return Response.ok().build();
    }
}
