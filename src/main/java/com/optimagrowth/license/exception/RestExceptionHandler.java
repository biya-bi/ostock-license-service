package com.optimagrowth.license.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.optimagrowth.service.MessageService;

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
    ResponseEntity<Void> handle(NotFoundException e) {
        log.error(messageService.getMessage(RESOURCE_NOT_FOUND), e);
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(RequestNotPermitted.class)
    ResponseEntity<Void> handle(RequestNotPermitted e) {
        log.error(messageService.getMessage(UNEXPECTED_ERROR_OCCURRED), e);
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }

    @ExceptionHandler(FeignException.Unauthorized.class)
    ResponseEntity<Void> handle(FeignException.Unauthorized e) {
        log.error(messageService.getMessage(UNEXPECTED_ERROR_OCCURRED), e);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<Void> handle(Exception e) {
        log.error(messageService.getMessage(UNEXPECTED_ERROR_OCCURRED), e);
        return ResponseEntity.internalServerError().build();
    }

}
