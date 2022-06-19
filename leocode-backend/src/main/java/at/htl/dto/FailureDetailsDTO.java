package at.htl.dto;

public class FailureDetailsDTO {
    public String type;
    public String message;

    public FailureDetailsDTO(String type, String message) {
        this.type = type;
        this.message = message;
    }

    public FailureDetailsDTO() {

    }

    @Override
    public String toString() {
        return "FailureDetails{" +
                "type='" + type + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
