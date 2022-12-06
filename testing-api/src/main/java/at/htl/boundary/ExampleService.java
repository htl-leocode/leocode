package at.htl.boundary;

import at.htl.entities.Example;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Set;

@Produces(MediaType.APPLICATION_JSON)
@Path("/example")
@RegisterRestClient(baseUri = "http://backend:9090/example")
public interface ExampleService {

    @GET
    @Path("{id}")
    Set<Example> getExampleById(@PathParam long id);

}
