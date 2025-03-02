package backend.academy.scrapper.controller;

import backend.academy.scrapper.api.dto.ApiErrorResponse;
import backend.academy.scrapper.service.EntityNotFoundException;
import backend.academy.scrapper.service.LinkNotFoundWithUrlException;
import backend.academy.scrapper.service.check.LinkNotExistsException;
import backend.academy.scrapper.service.parser.LinkFormatException;
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

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(LinkNotFoundWithUrlException.class)
    public ApiErrorResponse handleLinkNotFoundWithUrlException(LinkNotFoundWithUrlException e) {
        return createApiErrorResponse(e, HttpStatus.NOT_FOUND, "Link not found");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400 по ТЗ
    @ExceptionHandler(EntityNotFoundException.class)
    public ApiErrorResponse handleException(EntityNotFoundException e) {
        return createApiErrorResponse(e, HttpStatus.BAD_REQUEST, "Entity not found");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(LinkFormatException.class)
    public ApiErrorResponse handleLinkFormatException(LinkFormatException e) {
        return createApiErrorResponse(e, HttpStatus.BAD_REQUEST, "Wrong link format");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(LinkNotExistsException.class)
    public ApiErrorResponse handleLinkNotExistsException(LinkNotExistsException e) {
        return createApiErrorResponse(e, HttpStatus.BAD_REQUEST, "Link does not exist");
    }

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
                .setMessage("Scrapper API error response")
                .addKeyValue("response", response)
                .log();
        return response;
    }
}
