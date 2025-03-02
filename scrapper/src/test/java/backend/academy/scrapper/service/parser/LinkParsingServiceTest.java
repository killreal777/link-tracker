package backend.academy.scrapper.service.parser;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import backend.academy.scrapper.model.entity.Link;
import backend.academy.scrapper.model.value.ParsedLink;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class LinkParsingServiceTest {

    @Mock
    private LinkParser<BaseParsedLink> linkParser1;

    @Mock
    private LinkParser<BaseParsedLink> linkParser2;

    private LinkParsingService linkParsingService;

    @BeforeEach
    void setUp() {
        List<LinkParser<?>> linkParsers = List.of(linkParser1, linkParser2);
        this.linkParsingService = new LinkParsingService(linkParsers);
    }

    @Test
    void shouldParseLink() {
        String link = "link";
        var expected = new BaseParsedLink("expected URL", Link.Type.GITHUB_REPOSITORY);
        when(linkParser1.parseUrl(anyString())).thenThrow(new LinkFormatException("Error parsing link"));
        when(linkParser2.parseUrl(link)).thenReturn(expected);
        var result = linkParsingService.parseLink(link);
        assertThat(result).isNotNull();
        assertThat(result.linkType()).isEqualTo(expected.linkType());
        assertThat(result.standardUrl()).isEqualTo(expected.standardUrl());
        verify(linkParser1).parseUrl(link);
        verify(linkParser2).parseUrl(link);
    }

    @Test
    void shouldThrowLinkFormatException() {
        String link = "link";
        when(linkParser1.parseUrl(anyString())).thenThrow(new LinkFormatException("Error parsing link"));
        when(linkParser2.parseUrl(link)).thenThrow(new LinkFormatException("Error parsing link"));
        assertThatThrownBy(() -> linkParsingService.parseLink(link)).isInstanceOf(LinkFormatException.class);
        verify(linkParser1).parseUrl(link);
        verify(linkParser2).parseUrl(link);
    }

    private record BaseParsedLink(String standardUrl, Link.Type linkType) implements ParsedLink {}
}
