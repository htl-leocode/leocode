package at.htl.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.json.bind.annotation.JsonbProperty;
import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "LC_Teacher")
public class Teacher extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonbProperty("id")
    public Long id;

    @JsonbProperty("name")
    public String name;

    @JsonbProperty("ghUsername")
    public String ghUsername;

    public Teacher() {
    }



    @Override
    public String toString() {
        return "Teacher{" +
                "name='" + name + '\'' +
                ", ghUsername='" + ghUsername + '\'' +
                '}';
    }
}
