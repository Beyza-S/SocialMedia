package com.beyzasoy.socialmedia.exception;

import java.time.LocalDateTime;

public class ApiErrorResponse {

    private LocalDateTime timestamp; //hata ne zaman oluştu
    private int status;
    private String error;
    private String message;

    //Constructor
    public ApiErrorResponse(LocalDateTime timestamp, int status, String error, String message) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    public int getStatus() {
        return status;
    }
    public String getError() {
        return error;
    }
    public String getMessage() {
        return message;
    }
}