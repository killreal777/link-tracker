package backend.academy.scrapper.service.check;

import backend.academy.scrapper.client.github.GitHubRestClient;
import backend.academy.scrapper.client.github.GitHubRestClientApiErrorResponseException;
import backend.academy.scrapper.model.entity.Link;
import backend.academy.scrapper.model.value.ParsedGitHubLink;
import backend.academy.scrapper.service.parser.GitHubLinkParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GitHubLinkCheckService implements LinkCheckService {

    private final GitHubRestClient gitHubRestClient;
    private final GitHubLinkParser gitHubLinkParser;

    @Override
    public void checkExistence(String standardUrl) {
        try {
            ParsedGitHubLink parsedLink = gitHubLinkParser.parseStandardUrl(standardUrl);
            gitHubRestClient.getRepository(parsedLink.owner(), parsedLink.repo());
        } catch (GitHubRestClientApiErrorResponseException e) {
            throw new LinkNotExistsException(linkType(), standardUrl);
        }
    }

    @Override
    public Link.Type linkType() {
        return Link.Type.GITHUB_REPOSITORY;
    }
}
