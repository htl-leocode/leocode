package at.htl.boundary;

import at.htl.entities.Example;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.Set;

@Path("/example")
@RegisterRestClient
public interface ExampleService {

    @GET
    Set<Example> getExampleById(@PathParam long id);

}
