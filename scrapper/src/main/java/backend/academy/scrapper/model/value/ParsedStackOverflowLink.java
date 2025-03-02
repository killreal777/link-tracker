package backend.academy.scrapper.model.value;

import backend.academy.scrapper.model.entity.Link;

public record ParsedStackOverflowLink(long questionId, String standardUrl) implements ParsedLink {

    @Override
    public Link.Type linkType() {
        return Link.Type.STACKOVERFLOW_QUESTION;
    }
}
