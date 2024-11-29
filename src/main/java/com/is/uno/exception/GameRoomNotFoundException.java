package com.is.uno.exception;

public class GameRoomNotFoundException extends RuntimeException {
    public GameRoomNotFoundException(String message) {
        super(message);
    }
}
