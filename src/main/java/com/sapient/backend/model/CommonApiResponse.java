package com.sapient.backend.model;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Data
public class CommonApiResponse<T> {

    HttpStatus status;
    T data;

    public CommonApiResponse(HttpStatus httpStatus, T data) {
        this.status = httpStatus;
        this.data = data;
    }

    public ResponseEntity<T> build() {
        return ResponseEntity.status(status)
                .body(data);
    }
}
