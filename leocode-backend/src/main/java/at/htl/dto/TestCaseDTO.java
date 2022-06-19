package at.htl.dto;

import at.htl.entity.FailureDetails;

import javax.persistence.CascadeType;
import javax.persistence.OneToOne;

public class TestCaseDTO {
    public String name;
    public String className;
    public String time;

    public FailureDetailsDTO failure;

    public TestCaseDTO(String name, String className, String time) {
        this.name = name;
        this.className = className;
        this.time = time;
    }

    public TestCaseDTO() {
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
