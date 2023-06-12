package com.chicmic.eNaukri.CustomExceptions;


import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.List;

public class ApiError {

    private final HttpStatus status;
    private final List<String> message;
    private final Instant timestamp;

    public ApiError(HttpStatus status, List<String> message, Instant timestamp) {
        this.status= status;
        this.message = message;
        this.timestamp = timestamp;
    }

    public HttpStatus getStatus() {
        return this.status;
    }

    public List<String> getMessage() {
        return this.message;
    }

    public Instant getTimestamp() {
        return this.timestamp;
    }
}
