package backend.academy.scrapper.service.parser;

import backend.academy.scrapper.model.value.ParsedGitHubLink;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component
public class GitHubLinkParser implements LinkParser<ParsedGitHubLink> {

    /** This regular expression is used to extract the username and repository name from a GitHub repository URL. */
    private static final String GITHUB_REPO_URL_REGEX = "^(?:https?://)?" // Optional protocol http or https
            + "(?:www\\.)?" // Optional www subdomain
            + "github\\.com/" // Mandatory github.com domain
            + "([^/]+)/" // Capture group for username
            + "([^/]+)$"; // Capture group for repository name

    private static final Pattern GITHUB_REPO_URL_PATTERN =
            Pattern.compile(GITHUB_REPO_URL_REGEX, Pattern.CASE_INSENSITIVE);

    private static final String STANDARD_URL_FIRST_PART = "https://github.com/";

    @Override
    public ParsedGitHubLink parseUrl(String url) {
        Matcher matcher = GITHUB_REPO_URL_PATTERN.matcher(url);

        if (!matcher.matches()) {
            throw new LinkFormatException(url, "URL does not match the GitHub repository pattern.");
        }

        String owner = matcher.group(1);
        String repo = matcher.group(2);
        String standardUrl = STANDARD_URL_FIRST_PART + owner + "/" + repo;

        return new ParsedGitHubLink(owner, repo, standardUrl);
    }

    @Override
    public ParsedGitHubLink parseStandardUrl(String standardUrl) {
        String[] ownerAndRepo =
                standardUrl.substring(STANDARD_URL_FIRST_PART.length()).split("/");
        return new ParsedGitHubLink(ownerAndRepo[0], ownerAndRepo[1], standardUrl);
    }
}
