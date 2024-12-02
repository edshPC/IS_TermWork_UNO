package com.is.uno.socket;

import com.is.uno.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@RequiredArgsConstructor
public class MyWebSocketHandler extends TextWebSocketHandler {

    private final MessageService messageService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {

    }
}

