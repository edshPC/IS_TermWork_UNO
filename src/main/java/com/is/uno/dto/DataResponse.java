package com.is.uno.dto;

import lombok.*;
import org.springframework.http.ResponseEntity;

@Data
@AllArgsConstructor
public class DataResponse<T> extends Response<DataResponse<T>> {
    private T data;

    public static <T> ResponseEntity<?> success(T data) {
        return new DataResponse<T>(data).asResponseEntity();
    }

}
