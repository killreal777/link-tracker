package backend.academy.scrapper.model.value;

import backend.academy.scrapper.model.entity.Link;

public record ParsedGitHubLink(String owner, String repo, String standardUrl) implements ParsedLink {

    @Override
    public Link.Type linkType() {
        return Link.Type.GITHUB_REPOSITORY;
    }
}
