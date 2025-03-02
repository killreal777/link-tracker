package backend.academy.scrapper.service.check;

import backend.academy.scrapper.model.entity.Link;

public interface LinkCheckService {

    void checkExistence(String standardUrl);

    Link.Type linkType();
}
