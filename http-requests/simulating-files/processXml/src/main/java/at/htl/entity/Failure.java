package at.htl.entity;

public class Failure {
    public String type;
    public String message;

    public Failure(String type, String message) {
        this.type = type;
        this.message = message;
    }

    public Failure() {

    }

    @Override
    public String toString() {
        return "Failure{" +
                "type='" + type + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
