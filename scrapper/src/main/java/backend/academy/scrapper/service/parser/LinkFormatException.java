package backend.academy.scrapper.service.parser;

import lombok.Getter;

@Getter
public class LinkFormatException extends IllegalArgumentException {

    private final String link;

    public LinkFormatException(String link) {
        super(String.format("Wrong format for link: %s", link));
        this.link = link;
    }

    public LinkFormatException(String link, String message) {
        super(message);
        this.link = link;
    }
}
