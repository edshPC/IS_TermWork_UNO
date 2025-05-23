package com.is.uno.dto;

import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Response<T> {
    private String message;
    private boolean success = true;

    public ResponseEntity<T> asResponseEntity() {
        return ResponseEntity.ok((T)this);
    }

    public ResponseEntity<T> asResponseEntity(HttpStatus status) {
        return ResponseEntity.status(status).body((T)this);
    }

}
