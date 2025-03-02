package backend.academy.scrapper.service.check;

import backend.academy.scrapper.client.stackoverflow.StackOverflowRestClient;
import backend.academy.scrapper.client.stackoverflow.StackOverflowRestClientApiErrorResponseException;
import backend.academy.scrapper.model.entity.Link;
import backend.academy.scrapper.model.value.ParsedStackOverflowLink;
import backend.academy.scrapper.service.parser.StackOverflowLinkParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StackOverflowLinkCheckService implements LinkCheckService {

    private final StackOverflowRestClient stackOverflowRestClient;
    private final StackOverflowLinkParser stackOverflowLinkParser;

    @Override
    public void checkExistence(String standardUrl) {
        try {
            ParsedStackOverflowLink parsedLink = stackOverflowLinkParser.parseStandardUrl(standardUrl);
            stackOverflowRestClient.getQuestionsByIds(Long.toString(parsedLink.questionId()));
        } catch (StackOverflowRestClientApiErrorResponseException e) {
            throw new LinkNotExistsException(linkType(), standardUrl);
        }
    }

    @Override
    public Link.Type linkType() {
        return Link.Type.STACKOVERFLOW_QUESTION;
    }
}
