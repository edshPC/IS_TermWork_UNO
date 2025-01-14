package com.is.uno.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
      return new ErrorResponse(e.getClass().getCanonicalName(), e.getMessage());
   }

   @ExceptionHandler
   @ResponseStatus(HttpStatus.BAD_REQUEST)
   public ErrorResponse handleUserNotFoundException(UserNotFoundException e) {
      return new ErrorResponse(e.getClass().getCanonicalName(), e.getMessage());
   }

   @ExceptionHandler
   @ResponseStatus(HttpStatus.BAD_REQUEST)
   public ErrorResponse handleForbiddenException(ForbiddenException e) {
      return new ErrorResponse(e.getClass().getCanonicalName(), e.getMessage());
   }

   @ExceptionHandler
   @ResponseStatus(HttpStatus.BAD_REQUEST)
   public ErrorResponse handleIllegalArgumentException(IllegalArgumentException e) {
      return new ErrorResponse(e.getClass().getCanonicalName(), e.getMessage());
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

      return new ErrorResponse("Ошибка валидации", String.join("\n", errors));
   }

   @ExceptionHandler(GameRoomNotFoundException.class)
   @ResponseStatus(HttpStatus.NOT_FOUND)
   public ErrorResponse handleGameRoomNotFoundException(GameRoomNotFoundException e) {
      return new ErrorResponse(e.getClass().getCanonicalName(), e.getMessage());
   }

   @ExceptionHandler(AuthenticationException.class)
   @ResponseStatus(HttpStatus.UNAUTHORIZED)
   public ErrorResponse handleAuthenticationException(AuthenticationException e) {
      return new ErrorResponse(e.getClass().getCanonicalName(), e.getMessage());
   }

   @ExceptionHandler(Exception.class)
   @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
   public ErrorResponse handleGlobalException(Exception e) {
      return new ErrorResponse(e.getClass().getCanonicalName(), e.getMessage());
   }
}
