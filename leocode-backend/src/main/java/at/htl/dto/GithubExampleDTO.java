package at.htl.dto;

import java.util.List;

public class GithubExampleDTO {

    String name;
    String description;
    int type;
    List<String> collaborators;

    boolean isPublic;

    String repoUrl;

    String repoToken;

    public String getRepoUrl() {
        return repoUrl;
    }

    public void setRepoUrl(String repoUrl) {
        this.repoUrl = repoUrl;
    }

    public String getRepoToken() {
        return repoToken;
    }

    public void setRepoToken(String repoToken) {
        this.repoToken = repoToken;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

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

    public int getType() {
        return type;
    }

    public void setType(int type) {
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

    public GithubExampleDTO(String name, String description, int type, List<String> collaborators, boolean isPublic, String repoUrl, String repoToken) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.collaborators = collaborators;
        this.isPublic = isPublic;
        this.repoUrl = repoUrl;
        this.repoToken = repoToken;
    }
}
