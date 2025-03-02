package backend.academy.scrapper.service.parser;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import backend.academy.scrapper.model.value.ParsedGitHubLink;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

public class GitHubLinkParserTest {

    static GitHubLinkParser gitHubLinkParser = new GitHubLinkParser();

    @ParameterizedTest
    @MethodSource("provideValidUrlsAndParsedLinks")
    void shouldParseGitHubLinkFromAnyValidUrl(String url, ParsedGitHubLink expected) {
        var result = gitHubLinkParser.parseUrl(url);
        assertThat(result).isNotNull().isEqualTo(expected);
    }

    static Stream<Arguments> provideValidUrlsAndParsedLinks() {
        return Stream.of(
                Arguments.of(
                        "github.com/killreal777/is-project",
                        new ParsedGitHubLink("killreal777", "is-project", "https://github.com/killreal777/is-project")),
                Arguments.of(
                        "www.github.com/killreal777/is-project",
                        new ParsedGitHubLink("killreal777", "is-project", "https://github.com/killreal777/is-project")),
                Arguments.of(
                        "http://github.com/killreal777/is-project",
                        new ParsedGitHubLink("killreal777", "is-project", "https://github.com/killreal777/is-project")),
                Arguments.of(
                        "http://www.github.com/killreal777/is-project",
                        new ParsedGitHubLink("killreal777", "is-project", "https://github.com/killreal777/is-project")),
                Arguments.of(
                        "https://github.com/killreal777/is-project",
                        new ParsedGitHubLink("killreal777", "is-project", "https://github.com/killreal777/is-project")),
                Arguments.of(
                        "https://www.github.com/killreal777/is-project",
                        new ParsedGitHubLink(
                                "killreal777", "is-project", "https://github.com/killreal777/is-project")));
    }

    @ParameterizedTest
    @CsvSource({
        "a[psdufpasdfopaspdf",
        "019uu lkoi13jeio  oij3oi3 ",
        "otherhub.com/killreal777/is-project",
        "wwww.github.com/killreal777/is-project",
        "ttps://github.com/killreal777/is-project",
        "https://github.com/killreal777",
        "https://github.com/killreal777/",
        "https://github.com/killreal777/is-project/",
        "https://github.com/killreal777/is-project/blob/main/src/main/java/itmo/is/project/IsProjectApplication.java",
    })
    void shouldThrowLinkFormatExceptionWhenUrlIsInvalid(String invalidUrl) {
        assertThatThrownBy(() -> gitHubLinkParser.parseUrl(invalidUrl)).isInstanceOf(LinkFormatException.class);
    }

    @ParameterizedTest
    @MethodSource("provideUrlsInStandardFormatAndParsedLinks")
    void shouldParseGitHubLinkFromUrlInStandardFormat(String standardUrl, ParsedGitHubLink expected) {
        var result = gitHubLinkParser.parseStandardUrl(standardUrl);
        assertThat(result).isNotNull().isEqualTo(expected);
    }

    static Stream<Arguments> provideUrlsInStandardFormatAndParsedLinks() {
        return Stream.of(
                Arguments.of(
                        "https://github.com/killreal777/is-project",
                        new ParsedGitHubLink("killreal777", "is-project", "https://github.com/killreal777/is-project")),
                Arguments.of(
                        "https://github.com/killreal777/is-labs",
                        new ParsedGitHubLink("killreal777", "is-labs", "https://github.com/killreal777/is-labs")),
                Arguments.of(
                        "https://github.com/killreal777/social-network",
                        new ParsedGitHubLink(
                                "killreal777", "social-network", "https://github.com/killreal777/social-network")));
    }
}
