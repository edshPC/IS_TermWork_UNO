package com.is.uno.socket;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class UpdateController {

    @MessageMapping("/room/{roomId}")
    @SendTo("/topic/updates/{roomId}")
    public String sendMessage(@DestinationVariable Long roomId,
                              String message) {
        return roomId + ": " + message;
    }
}

