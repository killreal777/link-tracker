package backend.academy.bot.service.handler.registry;

import backend.academy.bot.service.handler.UpdateHandler;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public abstract class UpdateHandlerRegistry<H extends UpdateHandler, K> {

    protected final Map<K, H> handlers;

    public UpdateHandlerRegistry(Set<H> handlers, Function<H, K> keyMapper) {
        this.handlers = handlers.stream().collect(Collectors.toMap(keyMapper, Function.identity()));
    }

    public Optional<H> getHandler(K key) {
        return Optional.ofNullable(handlers.get(key));
    }
}
