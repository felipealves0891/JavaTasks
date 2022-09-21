package com.example.tasks.model.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import java.time.ZonedDateTime;

public class PayloadDto {

    @Getter
    private HttpStatus status;

    @Getter
    private Object data;

    @Getter
    private ZonedDateTime timestamp;

    public PayloadDto(HttpStatus status, Object data) {
        this.status = status;
        this.data = data;
        this.timestamp = ZonedDateTime.now();
    }
}
