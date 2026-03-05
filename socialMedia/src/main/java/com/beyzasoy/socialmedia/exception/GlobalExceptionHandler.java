package com.beyzasoy.socialmedia.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

@RestControllerAdvice     //Controllerlardaki hatayı yakalayıp mesaj yollar
public class GlobalExceptionHandler {   //Normalde whiteLabel hatası gelecekken RestControllerAdvice sayesinde kendi JSON mesajımıı döndürürüz.

    @ExceptionHandler(ApiException.class)   //ApiException fırlatıldığında çalışır.
    public ResponseEntity<ApiErrorResponse> handleApiException(ApiException ex) {

        ApiErrorResponse error = new ApiErrorResponse(
                LocalDateTime.now(),
                ex.getStatus().value(),  //400 , 200 gibi
                ex.getStatus().getReasonPhrase(),  //Not found, unauthorized
                ex.getMessage()
        );

        return ResponseEntity.status(ex.getStatus()).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)  //Validation hataları(notBlank,Size)
    public ResponseEntity<ApiErrorResponse> handleValidationException() {

        ApiErrorResponse error = new ApiErrorResponse(
                LocalDateTime.now(),
                400,
                "Bad Request",
                "Validation error"
        );

        return ResponseEntity.badRequest().body(error);
    }
}