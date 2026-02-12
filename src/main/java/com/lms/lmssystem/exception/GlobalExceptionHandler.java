package com.lms.lmssystem.exception;

import com.lms.lmssystem.dto.ErrorReply;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
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
    @ExceptionHandler(jakarta.persistence.EntityNotFoundException.class)
    public ResponseEntity<ErrorReply> handleEntityNotFound(jakarta.persistence.EntityNotFoundException ex) {

        log.error("EntityNotFoundException: {}", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorReply(ex.getMessage()));
    }


    //500

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorReply> handleAnyException(Exception ex) {

        log.error("Unexpected exception", ex);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorReply("Внутренняя ошибка сервера"));
}
