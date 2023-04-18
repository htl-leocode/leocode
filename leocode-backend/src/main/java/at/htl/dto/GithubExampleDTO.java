package at.htl.dto;

import java.util.List;

public class GithubExampleDTO {

    String name;
    String description;
    String type;
    List<String> collaborators;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getCollaborators() {
        return collaborators;
    }

    public void setCollaborators(List<String> collaborators) {
        this.collaborators = collaborators;
    }

    public GithubExampleDTO() {
    }

    public GithubExampleDTO(String name, String description, String type, List<String> collaborators) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.collaborators = collaborators;
    }
}
