package backend.academy.bot.controller;

import backend.academy.bot.api.dto.ApiErrorResponse;
import java.util.Arrays;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Exception.class)
    public ApiErrorResponse handleException(Exception e) {
        return createApiErrorResponse(e, HttpStatus.BAD_REQUEST, "Something went wrong");
    }

    private ApiErrorResponse createApiErrorResponse(Exception e, HttpStatus status, String message) {
        ApiErrorResponse response = new ApiErrorResponse(
                message,
                Integer.toString(status.value()),
                e.getClass().getSimpleName(),
                e.getMessage(),
                Arrays.stream(e.getStackTrace()).map(Objects::toString).toList());
        log.atInfo()
                .setMessage("Bot API error response")
                .addKeyValue("response", response)
                .log();
        return response;
    }
}
