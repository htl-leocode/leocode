package at.htl.dto;

public class RepositoryDTO {

    public String teacher;
    public String name;

    public RepositoryDTO() {
    }

    public RepositoryDTO(String teacher, String name) {
        this.teacher = teacher;
        this.name = name;
    }

    @Override
    public String toString() {
        return "RepositoryDTO{" +
                "teacher=" + teacher +
                ", name='" + name + '\'' +
                '}';
    }
}
