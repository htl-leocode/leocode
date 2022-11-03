package at.htl.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "LC_Repository")
public class Repository extends PanacheEntity {

    public String name;

    public String token;

    @ManyToOne(cascade = CascadeType.ALL)
    public Teacher teacher;


    public Repository() {
    }

    public Repository(String name, Teacher teacher, String token) {
        this.name = name;
        this.teacher = teacher;
        this.token = token;
    }

    @Override
    public String toString() {
        return "Repository{" +
                "name='" + name + '\'' +
                ", teacher=" + teacher +
                ", token='" + token + '\'' +
                '}';
    }
}
