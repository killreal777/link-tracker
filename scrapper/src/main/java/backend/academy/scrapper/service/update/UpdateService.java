package backend.academy.scrapper.service.update;

import backend.academy.bot.api.dto.LinkUpdate;
import backend.academy.scrapper.model.entity.Link;
import java.time.OffsetDateTime;
import java.util.List;

public interface UpdateService {

    List<LinkUpdate> getAllUpdates(OffsetDateTime from);

    Link.Type linkType();
}
