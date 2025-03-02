package backend.academy.scrapper.model.value;

import backend.academy.scrapper.model.entity.Link;

public interface ParsedLink {

    /**
     * URL in standard format.
     *
     * @see backend.academy.scrapper.model.entity.Link#standardUrl()
     */
    String standardUrl();

    Link.Type linkType();
}
