package com.optimagrowth.license.exception;

import java.util.Locale;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.optimagrowth.license.service.MessageService;

import feign.FeignException;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@EnableWebMvc
@Slf4j
class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String RESOURCE_NOT_FOUND = "resource.not.found";
    private static final String UNEXPECTED_ERROR_OCCURRED = "unexpected.error.occurred";

    private final MessageService messageService;

    RestExceptionHandler(MessageService messsageService) {
        this.messageService = messsageService;
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseBody
    ResponseEntity<Void> handle(NotFoundException e,
            @RequestHeader(value = "Accept-Language", required = false) Locale locale) {
        log.error(messageService.getMessage(RESOURCE_NOT_FOUND, locale), e);
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(RequestNotPermitted.class)
    ResponseEntity<Void> handle(RequestNotPermitted e,
            @RequestHeader(value = "Accept-Language", required = false) Locale locale) {
        log.error(messageService.getMessage(UNEXPECTED_ERROR_OCCURRED, locale), e);
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }

    @ExceptionHandler(FeignException.Unauthorized.class)
    ResponseEntity<Void> handle(FeignException.Unauthorized e,
            @RequestHeader(value = "Accept-Language", required = false) Locale locale) {
        log.error(messageService.getMessage(UNEXPECTED_ERROR_OCCURRED, locale), e);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<Void> handle(Exception e,
            @RequestHeader(value = "Accept-Language", required = false) Locale locale) {
        log.error(messageService.getMessage(UNEXPECTED_ERROR_OCCURRED, locale), e);
        return ResponseEntity.internalServerError().build();
    }

}
