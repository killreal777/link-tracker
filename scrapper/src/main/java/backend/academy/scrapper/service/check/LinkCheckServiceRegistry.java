package backend.academy.scrapper.service.check;

import backend.academy.scrapper.model.entity.Link;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LinkCheckServiceRegistry {

    private final Map<Link.Type, LinkCheckService> services;

    @Autowired
    public LinkCheckServiceRegistry(Set<LinkCheckService> services) {
        this.services = services.stream().collect(Collectors.toMap(LinkCheckService::linkType, Function.identity()));
    }

    public LinkCheckService get(Link.Type linkType) {
        return services.get(linkType);
    }
}
