package com.is.uno.core;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserEvent {

    private String username;
    private Type type;
    private Integer value;

    public UserEvent(String username, Type type) {
        this.username = username;
        this.type = type;
    }

    public enum Type {
        REGISTER,
        VIEW_STATISTICS,
        CREATE_ROOM,
        WIN,
        PLAY,
    }

}
