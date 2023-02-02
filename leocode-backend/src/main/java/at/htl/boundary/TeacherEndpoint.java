package at.htl.boundary;

import at.htl.dto.RepositoryDTO;
import at.htl.entity.Repository;
import at.htl.entity.Teacher;
import at.htl.repository.TeacherRepository;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/teacher")
public class TeacherEndpoint {

    @Inject
    Logger log;

    @Inject
    TeacherRepository teacherRepository;

    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public void newTeacher(Teacher teacher){


        Teacher t = Teacher.find("name", teacher.name).firstResult();


        if(t == null){
            Teacher newT = new Teacher();

            newT.name = teacher.name;
            newT.ghUsername = teacher.ghUsername;

            newT.persist();
            return;
        }
        log.info(t);
        t.name = teacher.ghUsername;
        t.persist();
    }

    @POST
    @Path("/newRepository")
    @Consumes(MediaType.APPLICATION_JSON)
    public void newRepository(RepositoryDTO repository){

        log.info(repository);

        teacherRepository.insertRepository(repository);
    }

    @GET
    @Path("/allTeacher")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Teacher> getAllTeacher(){
        return Teacher.listAll();
    }

    @GET
    @Path("/allRepository")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Repository> getAllRepository(){
        return Repository.listAll();
    }

    @GET
    @Path("/repositoryByTeacher/{teacher}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Repository> getRepositorysByTeacher(@PathParam("teacher") String teacher){

        List<Repository> res = Repository.find("teacher.name", teacher).list();
        log.info(res);
        return res;

    }


}
