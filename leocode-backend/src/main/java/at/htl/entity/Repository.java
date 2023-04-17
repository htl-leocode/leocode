package at.htl.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "LC_Repository")
public class Repository extends PanacheEntity {

    public String repoUrl;

    @JsonbTransient
    public String token;

    @ManyToOne(cascade = CascadeType.ALL)
    public Teacher teacher;


    public Repository() {
    }

    public Repository(String repoUrl, Teacher teacher, String token) {
        this.repoUrl = repoUrl;
        this.teacher = teacher;
        this.token = token;
    }

    @Override
    public String toString() {
        return "Repository{" +
                "name='" + repoUrl + '\'' +
                ", teacher=" + teacher +
                ", token='" + token + '\'' +
                '}';
    }

    public boolean isValid() {
        return repoUrl != null && teacher != null && token != null;
    }
}
