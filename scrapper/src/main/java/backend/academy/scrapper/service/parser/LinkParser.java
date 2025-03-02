package backend.academy.scrapper.service.parser;

import backend.academy.scrapper.model.value.ParsedLink;

public interface LinkParser<P extends ParsedLink> {

    /**
     * Parses link from any valid URL.
     *
     * @param url the URL to be parsed. May be in any valid format.
     * @return parsed link, containing the parsed information from the URL.
     */
    P parseUrl(String url);

    /**
     * Parses link from URL in standard format
     *
     * @param standardUrl the URL to be parsed. Must be in standard format.
     * @return parsed link, containing the parsed information from the URL.
     * @see backend.academy.scrapper.model.entity.Link#standardUrl()
     */
    P parseStandardUrl(String standardUrl);
}
