package at.htl.dto;

public class RepositoryDTO {

    public String teacher;
    public String name;
    public String token;

    public RepositoryDTO() {
    }

    public RepositoryDTO(String teacher, String name, String token) {
        this.teacher = teacher;
        this.name = name;
        this.token = token;
    }

    @Override
    public String toString() {
        return "RepositoryDTO{" +
                "teacher=" + teacher +
                ", name='" + name + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
