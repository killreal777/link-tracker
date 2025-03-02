package backend.academy.scrapper.client.stackoverflow;

import backend.academy.scrapper.client.stackoverflow.dto.ListQuestionsResponse;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange
public interface StackOverflowRestClient {

    Integer FIRST_PAGE = 1;
    Integer MAX_PAGE_SIZE = 100;
    Integer MAX_IDS = 100;
    String IDS_DELIMITER = ";";

    @GetExchange("/questions/{ids}")
    ResponseEntity<ListQuestionsResponse> getQuestionsByIds(
            @PathVariable String ids,
            @RequestParam(value = "site", defaultValue = StackOverflowRestClientConfig.SITE) String site,
            @RequestParam(value = "filter", defaultValue = StackOverflowRestClientConfig.FILTER, required = false)
                    String filter,
            @RequestParam(value = "page", defaultValue = "1", required = false) int page,
            @RequestParam(value = "page_size", defaultValue = "100", required = false) int pageSize);

    default ResponseEntity<ListQuestionsResponse> getQuestionsByIds(String ids) {
        return getQuestionsByIds(
                ids,
                StackOverflowRestClientConfig.SITE,
                StackOverflowRestClientConfig.FILTER,
                FIRST_PAGE,
                MAX_PAGE_SIZE);
    }

    default ResponseEntity<ListQuestionsResponse> getQuestionsByIds(List<String> idsList) {
        if (idsList.size() > MAX_IDS) {
            throw new IllegalArgumentException("Too many ids");
        }
        String ids = String.join(IDS_DELIMITER, idsList);
        return getQuestionsByIds(ids);
    }
}
