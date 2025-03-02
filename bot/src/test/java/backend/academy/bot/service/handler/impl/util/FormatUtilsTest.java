package backend.academy.bot.service.handler.impl.util;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import backend.academy.scrapper.api.dto.LinkResponse;
import backend.academy.scrapper.api.dto.ListLinksResponse;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class FormatUtilsTest {

    @ParameterizedTest
    @MethodSource("provideLinkResponsesAndExpectedStrings")
    void shouldFormatLinkResponse(LinkResponse linkResponse, String expected) {
        String result = FormatUtils.formatLinkResponse(linkResponse);
        assertThat(result).isNotNull().isEqualTo(expected);
    }

    static Stream<Arguments> provideLinkResponsesAndExpectedStrings() {
        return Stream.of(
                Arguments.of(
                        new LinkResponse(1L, "url", Set.of("tag1", "tag2", "tag3"), Set.of("filter1", "filter2")),
                        """
                    URL: url
                    Filters: [filter1, filter2]
                    Tags: [tag1, tag2, tag3]"""),
                Arguments.of(
                        new LinkResponse(1L, "url", Set.of("tag1", "tag2", "tag3"), null),
                        """
                    URL: url
                    Tags: [tag1, tag2, tag3]"""),
                Arguments.of(
                        new LinkResponse(1L, "url", null, Set.of("filter1", "filter2")),
                        """
                    URL: url
                    Filters: [filter1, filter2]"""),
                Arguments.of(new LinkResponse(1L, "url", null, null), "URL: url"));
    }

    @ParameterizedTest
    @MethodSource("provideListLinksResponsesAndExpectedStrings")
    void shouldFormatListLinksResponse(ListLinksResponse listLinksResponse, String expected) {
        String result = FormatUtils.formatListLinksResponse(listLinksResponse);
        assertThat(result).isNotNull().isEqualTo(expected);
    }

    static Stream<Arguments> provideListLinksResponsesAndExpectedStrings() {
        return Stream.of(
                Arguments.of(
                        new ListLinksResponse(
                                List.of(
                                        new LinkResponse(
                                                1L,
                                                "url1",
                                                Set.of("tag1", "tag2", "tag3"),
                                                Set.of("filter1", "filter2")),
                                        new LinkResponse(1L, "url2", Set.of("tag1", "tag2", "tag3"), null),
                                        new LinkResponse(1L, "url3", null, Set.of("filter1", "filter2")),
                                        new LinkResponse(1L, "url4", null, null)),
                                3),
                        """
                    URL: url1
                    Filters: [filter1, filter2]
                    Tags: [tag1, tag2, tag3]

                    URL: url2
                    Tags: [tag1, tag2, tag3]

                    URL: url3
                    Filters: [filter1, filter2]

                    URL: url4"""),
                Arguments.of(
                        new ListLinksResponse(
                                List.of(new LinkResponse(
                                        1L, "url1", Set.of("tag1", "tag2", "tag3"), Set.of("filter1", "filter2"))),
                                1),
                        """
                    URL: url1
                    Filters: [filter1, filter2]
                    Tags: [tag1, tag2, tag3]"""));
    }
}
