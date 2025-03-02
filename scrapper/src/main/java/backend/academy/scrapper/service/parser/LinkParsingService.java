package backend.academy.scrapper.service.parser;

import backend.academy.scrapper.model.value.ParsedLink;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LinkParsingService {

    private final List<LinkParser<?>> linkParsers;

    public ParsedLink parseLink(String link) {
        ParsedLink parsedLink = null;

        for (LinkParser<?> linkParser : linkParsers) {
            if (parsedLink != null) {
                break;
            }
            try {
                parsedLink = linkParser.parseUrl(link);
            } catch (LinkFormatException ignored) {
            }
        }
        if (parsedLink == null) {
            throw new LinkFormatException(link);
        }
        return parsedLink;
    }
}
