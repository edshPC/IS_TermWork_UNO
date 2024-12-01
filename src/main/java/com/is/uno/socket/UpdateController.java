package com.is.uno.socket;

import com.is.uno.dto.packet.Packet;
import com.is.uno.dto.packet.TextPacket;
import com.is.uno.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class UpdateController {

    private final SimpMessagingTemplate messagingTemplate;
    private final PacketHandler packetHandler;

    @MessageMapping("/room/{roomId}")
    //@SendTo("/topic/updates/{roomId}")
    public void handlePacket(@DestinationVariable Long roomId,
                             SimpMessageHeaderAccessor headerAccessor,
                             Packet packet) {
        Authentication authentication = (Authentication) headerAccessor.getUser();
        User user = authentication != null ? (User) authentication.getPrincipal() : null;
        Packet response;
        switch (packet.getType()) {
            case TEXT_PACKET -> response = packetHandler.handleTextPacket((TextPacket) packet, user);
            default -> throw new IllegalArgumentException("Invalid packet type");
        }
        if (response != null) {
            messagingTemplate.convertAndSend("/topic/updates/" + roomId, response);
        }
    }

}
