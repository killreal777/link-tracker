package backend.academy.bot.service.handler.impl.util;

import backend.academy.scrapper.api.dto.LinkResponse;
import backend.academy.scrapper.api.dto.ListLinksResponse;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FormatUtils {

    public static String formatLinkResponse(LinkResponse response) {
        StringBuilder sb = new StringBuilder(String.format("URL: %s", response.url()));
        if (response.filters() != null) {
            sb.append('\n').append(String.format("Filters: %s", sorted(response.filters())));
        }
        if (response.tags() != null) {
            sb.append('\n').append(String.format("Tags: %s", sorted(response.tags())));
        }
        return sb.toString();
    }

    private List<String> sorted(Set<String> strings) {
        return strings.stream().sorted().toList();
    }

    public static String formatListLinksResponse(ListLinksResponse response) {
        return response.links().stream().map(FormatUtils::formatLinkResponse).collect(Collectors.joining("\n\n"));
    }
}
