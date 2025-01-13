package com.is.uno.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@ControllerAdvice
public class GlobalControllerExceptionHandler {
   @ExceptionHandler
   @ResponseStatus(HttpStatus.BAD_REQUEST)
   public ErrorResponse handleUserAlreadyExistException(UserAlreadyExistException e) {
      return new ErrorResponse(e.getClass().getCanonicalName(),
              e.getMessage());
   }

   @ExceptionHandler
   @ResponseStatus(HttpStatus.BAD_REQUEST)
   public ErrorResponse handleUserNotFoundException(UserNotFoundException e) {
      return new ErrorResponse(e.getClass().getCanonicalName(),
            e.getMessage());
   }

   @ExceptionHandler
   @ResponseStatus(HttpStatus.BAD_REQUEST)
   public ErrorResponse handleMForbiddenException(ForbiddenException e) {
      return new ErrorResponse(e.getClass().getCanonicalName(),
            e.getMessage());
   }

   @ExceptionHandler
   @ResponseStatus(HttpStatus.BAD_REQUEST)
   public ErrorResponse handleIllegalArgumentException(IllegalArgumentException e) {
      return new ErrorResponse(
            e.getClass().getCanonicalName(),
            e.getMessage());
   }

   @ExceptionHandler
   @ResponseStatus(HttpStatus.BAD_REQUEST)
   public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
      List<String> errors = e.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(error -> String.format(
                  "Ошибка валидации поля '%s': %s (текущее значение: '%s')",
                  error.getField(),
                  error.getDefaultMessage(),
                  error.getRejectedValue() == null ? "пусто" : error.getRejectedValue()))
            .collect(Collectors.toList());

      return new ErrorResponse(
            "Ошибка валидации",
            String.join("\n", errors));
   }

   @ExceptionHandler(Exception.class)
   @MessageExceptionHandler(Exception.class)
   @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
   public ErrorResponse handleGlobalException(Exception e) {
      //e.printStackTrace();
      return new ErrorResponse(
              e.getClass().getCanonicalName(),
              e.getMessage());
   }
}
