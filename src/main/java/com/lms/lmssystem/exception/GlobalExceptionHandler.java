package com.lms.lmssystem.exception;

import com.lms.lmssystem.dto.response.ErrorReply;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 404
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorReply> handleIllegalArgument(IllegalArgumentException ex) {

        log.error("IllegalArgumentException: {}", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorReply(ex.getMessage()));
    }

    //404
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorReply> handleEntityNotFound(UserNotFoundException ex) {

        log.error("UserNotFoundException: {}", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorReply(ex.getMessage()));
    }


    //500

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorReply> handleAnyException(AuthenticationException ex) {

        log.error("AuthenticationException: {}", ex);

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorReply(ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorReply> handleUnexpected(Exception ex) {

        log.error("Unexpected exception", ex);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorReply("Внутренняя ошибка сервера"));
    }




}
