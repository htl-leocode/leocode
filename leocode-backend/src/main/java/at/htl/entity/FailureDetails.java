package at.htl.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "LC_FAILURE_DETAILS")
public class FailureDetails extends PanacheEntity {
    public String type;
    public String message;

    public FailureDetails(String type, String message) {
        this.type = type;
        this.message = message;
    }

    public FailureDetails() {

    }

    @Override
    public String toString() {
        return "FailureDetails{" +
                "type='" + type + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
