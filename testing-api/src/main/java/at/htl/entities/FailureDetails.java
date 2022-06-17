package at.htl.entities;

public class FailureDetails {
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
