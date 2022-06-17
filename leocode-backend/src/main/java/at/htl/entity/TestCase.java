package at.htl.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "LC_TESTCASE")
public class TestCase extends PanacheEntity {
    public String name;
    public String className;
    public String time;

    @OneToOne(cascade = CascadeType.ALL)
    public FailureDetails failure;

    public TestCase(String name, String className, String time) {
        this.name = name;
        this.className = className;
        this.time = time;
    }

    public TestCase() {

    }

    @Override
    public String toString() {
        return "TestCase{" +
                "name='" + name + '\'' +
                ", className='" + className + '\'' +
                ", time='" + time + '\'' +
                ", failure=" + failure +
                '}';
    }
}
