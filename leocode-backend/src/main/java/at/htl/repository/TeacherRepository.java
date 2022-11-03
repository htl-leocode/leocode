package at.htl.repository;

import at.htl.dto.RepositoryDTO;
import at.htl.entity.Repository;
import at.htl.entity.Teacher;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

@ApplicationScoped
public class TeacherRepository implements PanacheRepository<Teacher> {

    @Inject
    Logger log;


    @Transactional
    public void insertRepository(RepositoryDTO repository){
        Teacher t = find("name", repository.teacher).firstResult();

        log.info(t);

        Repository r = new Repository(repository.name, t, repository.token);
        r.persist();
    }

}
