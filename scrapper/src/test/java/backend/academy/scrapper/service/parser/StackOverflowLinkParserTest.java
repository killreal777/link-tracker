package backend.academy.scrapper.service.parser;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import backend.academy.scrapper.model.value.ParsedStackOverflowLink;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

public class StackOverflowLinkParserTest {

    static StackOverflowLinkParser stackOverflowLinkParser = new StackOverflowLinkParser();

    @ParameterizedTest
    @MethodSource("provideValidUrlsAndParsedLinks")
    void shouldParseStackOverflowLinkFromAnyValidUrl(String url, ParsedStackOverflowLink expected) {
        var result = stackOverflowLinkParser.parseUrl(url);
        assertThat(result).isNotNull().isEqualTo(expected);
    }

    static Stream<Arguments> provideValidUrlsAndParsedLinks() {
        return Stream.of(
                Arguments.of(
                        "stackoverflow.com/questions/12345",
                        new ParsedStackOverflowLink(12345, "https://stackoverflow.com/questions/12345")),
                Arguments.of(
                        "www.stackoverflow.com/questions/12345",
                        new ParsedStackOverflowLink(12345, "https://stackoverflow.com/questions/12345")),
                Arguments.of(
                        "stackoverflow.com/questions/12345/how-to-become-middle-java-developer-without-experience",
                        new ParsedStackOverflowLink(12345, "https://stackoverflow.com/questions/12345")),
                Arguments.of(
                        "www.stackoverflow.com/questions/12345/how-to-become-middle-java-developer-without-experience",
                        new ParsedStackOverflowLink(12345, "https://stackoverflow.com/questions/12345")),
                Arguments.of(
                        "http://stackoverflow.com/questions/12345",
                        new ParsedStackOverflowLink(12345, "https://stackoverflow.com/questions/12345")),
                Arguments.of(
                        "http://www.stackoverflow.com/questions/12345",
                        new ParsedStackOverflowLink(12345, "https://stackoverflow.com/questions/12345")),
                Arguments.of(
                        "http://stackoverflow.com/questions/12345/how-to-become-middle-java-developer-without-experience",
                        new ParsedStackOverflowLink(12345, "https://stackoverflow.com/questions/12345")),
                Arguments.of(
                        "http://www.stackoverflow.com/questions/12345/how-to-become-middle-java-developer-without-experience",
                        new ParsedStackOverflowLink(12345, "https://stackoverflow.com/questions/12345")),
                Arguments.of(
                        "https://stackoverflow.com/questions/12345",
                        new ParsedStackOverflowLink(12345, "https://stackoverflow.com/questions/12345")),
                Arguments.of(
                        "https://www.stackoverflow.com/questions/12345",
                        new ParsedStackOverflowLink(12345, "https://stackoverflow.com/questions/12345")),
                Arguments.of(
                        "https://stackoverflow.com/questions/12345/how-to-become-middle-java-developer-without-experience",
                        new ParsedStackOverflowLink(12345, "https://stackoverflow.com/questions/12345")),
                Arguments.of(
                        "https://www.stackoverflow.com/questions/12345/how-to-become-middle-java-developer-without-experience",
                        new ParsedStackOverflowLink(12345, "https://stackoverflow.com/questions/12345")));
    }

    @ParameterizedTest
    @CsvSource({
        "a[psdufpasdfopaspdf",
        "019uu lkoi13jeio  oij3oi3 ",
        "t.me/killreal777",
        "wwww.stackoverflow.com/questions/12345",
        "ttps://stackoverflow.com/questions/12345",
        "https://stackoverflow.com/questions/12345/",
        "https://stackoverflow.com/questions/",
        "https://stackoverflow.com/questions/sdd2345",
        "https://stackoverflow.com/questions/12345/how-to-become-middle-java-developer-without-experience/",
        "https://stackoverflow.com/questions/12345/how-to-become-middle-java-developer-without-experience/t-bank"
    })
    void shouldThrowLinkFormatExceptionWhenUrlIsInvalid(String invalidUrl) {
        assertThatThrownBy(() -> stackOverflowLinkParser.parseUrl(invalidUrl)).isInstanceOf(LinkFormatException.class);
    }

    @ParameterizedTest
    @MethodSource("provideUrlsInStandardFormatAndParsedLinks")
    void shouldParseStackOverflowLinkFromUrlInStandardFormat(String standardUrl, ParsedStackOverflowLink expected) {
        var result = stackOverflowLinkParser.parseStandardUrl(standardUrl);
        assertThat(result).isNotNull().isEqualTo(expected);
    }

    static Stream<Arguments> provideUrlsInStandardFormatAndParsedLinks() {
        return Stream.of(
                Arguments.of(
                        "https://stackoverflow.com/questions/12345",
                        new ParsedStackOverflowLink(12345, "https://stackoverflow.com/questions/12345")),
                Arguments.of(
                        "https://stackoverflow.com/questions/54321",
                        new ParsedStackOverflowLink(54321, "https://stackoverflow.com/questions/54321")),
                Arguments.of(
                        "https://stackoverflow.com/questions/11111",
                        new ParsedStackOverflowLink(11111, "https://stackoverflow.com/questions/11111")));
    }
}
