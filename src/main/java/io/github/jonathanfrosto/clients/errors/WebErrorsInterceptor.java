package io.github.jonathanfrosto.clients.errors;

import io.github.jonathanfrosto.clients.errors.exceptions.BadRequestException;
import io.github.jonathanfrosto.clients.errors.exceptions.NotFoundException;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class WebErrorsInterceptor {

    private final LocaleResolver localeResolver;
    private final MessageSource messageSource;

    public WebErrorsInterceptor(LocaleResolver localeResolver, MessageSource messageSource) {
        this.localeResolver = localeResolver;
        this.messageSource = messageSource;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Error beanValidation(MethodArgumentNotValidException e, HttpServletRequest request) {
        List<FieldError> allErrors = e.getFieldErrors();
        List<String> description = allErrors.stream()
                .map(error -> error.getField() + " - " + error.getDefaultMessage())
                .collect(Collectors.toList());

        return getDefaultError(request, String.join(", ", description));
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Error notFound(Exception e, HttpServletRequest request) {
        return getWithDescriptionByCode(request, e.getLocalizedMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Error badRequest(Exception e, HttpServletRequest request) {
        return getWithDescriptionByCode(request, e.getLocalizedMessage());
    }

    private Error getWithDescriptionByCode(HttpServletRequest request, String code) {
        String description = messageSource.getMessage(code, null, localeResolver.resolveLocale(request));
        return getDefaultError(request, description);
    }

    private Error getDefaultError(HttpServletRequest request, String description) {
        return Error.builder()
                .path(request.getServletPath())
                .description(description)
                .method(request.getMethod())
                .build();
    }

}
