package backend.academy.scrapper.repository;

import backend.academy.scrapper.model.entity.Link;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class LinkRepository extends InMemoryRepository<Link> {

    public List<Link> findAllByType(Link.Type type) {
        return findAll().stream().filter(link -> link.type() == type).toList();
    }

    public Optional<Link> findByUrl(String url) {
        return findAll().stream().filter(link -> url.equals(link.standardUrl())).findAny();
    }
}
