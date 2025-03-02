package backend.academy.scrapper.repository;

import backend.academy.scrapper.model.entity.TgChat;
import org.springframework.stereotype.Repository;

@Repository
public class TgChatRepository extends InMemoryRepository<TgChat> {}
