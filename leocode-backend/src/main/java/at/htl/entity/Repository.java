package at.htl.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "LC_Repository")
public class Repository extends PanacheEntity {

    public String name;

    @ManyToOne(cascade = CascadeType.ALL)
    public Teacher teacher;

    public Repository() {
    }

    public Repository(String name, Teacher teacher) {
        this.name = name;
        this.teacher = teacher;
    }

    @Override
    public String toString() {
        return "Repository{" +
                "name='" + name + '\'' +
                ", teacher=" + teacher +
                '}';
    }
}
