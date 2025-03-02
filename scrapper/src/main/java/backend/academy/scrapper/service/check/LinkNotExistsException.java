package backend.academy.scrapper.service.check;

import backend.academy.scrapper.model.entity.Link;
import lombok.Getter;

@Getter
public class LinkNotExistsException extends RuntimeException {

    private final Link.Type linkType;
    private final String url;

    public LinkNotExistsException(Link.Type type, String url) {
        super(String.format("%s does not exist with URL: %s", type.typeName(), url));
        this.linkType = type;
        this.url = url;
    }
}
