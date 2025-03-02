package backend.academy.scrapper.service.parser;

import backend.academy.scrapper.model.value.ParsedStackOverflowLink;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component
public class StackOverflowLinkParser implements LinkParser<ParsedStackOverflowLink> {

    /** This regular expression is used to extract the question id from a StackOverflow question URL. */
    private static final String STACKOVERFLOW_QUESTION_URL_REGEX = "^(?:https?://)?" // Optional protocol http or https
            + "(?:www\\.)?" // Optional www subdomain
            + "stackoverflow\\.com/questions/" // Mandatory stackoverflow.com/questions domain
            + "(\\d+)" // Capture group for question ID
            + "(?:/[^/]+)?$"; // Optional question title

    private static final Pattern STACKOVERFLOW_QUESTION_URL_PATTERN =
            Pattern.compile(STACKOVERFLOW_QUESTION_URL_REGEX, Pattern.CASE_INSENSITIVE);

    private static final String STANDARD_URL_FIRST_PART = "https://stackoverflow.com/questions/";

    @Override
    public ParsedStackOverflowLink parseUrl(String url) {
        Matcher matcher = STACKOVERFLOW_QUESTION_URL_PATTERN.matcher(url);

        if (!matcher.matches()) {
            throw new LinkFormatException(url, "URL does not match the StackOverflow question pattern.");
        }

        long questionId = Long.parseLong(matcher.group(1));
        String standardUrl = STANDARD_URL_FIRST_PART + questionId;

        return new ParsedStackOverflowLink(questionId, standardUrl);
    }

    @Override
    public ParsedStackOverflowLink parseStandardUrl(String standardUrl) {
        long questionId = Long.parseLong(standardUrl.substring(STANDARD_URL_FIRST_PART.length()));
        return new ParsedStackOverflowLink(questionId, standardUrl);
    }
}
