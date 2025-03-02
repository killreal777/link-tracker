package backend.academy.scrapper.service;

import lombok.Getter;

@Getter
public class LinkNotFoundWithUrlException extends EntityNotFoundException {

    private final String url;

    public LinkNotFoundWithUrlException(String url) {
        super(String.format("Link not found with URL: %s", url));
        this.url = url;
    }
}
