package at.htl.entities;

public class TestCase {
    public String name;
    public String className;
    public String time;
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
