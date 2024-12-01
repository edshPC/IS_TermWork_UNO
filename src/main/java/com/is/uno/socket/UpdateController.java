package com.is.uno.socket;

import com.is.uno.dto.packet.Packet;
import com.is.uno.dto.packet.TextPacket;
import com.is.uno.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class UpdateController {

    private final PacketHandler packetHandler;

    @MessageMapping("/room/{roomId}")
    @SendTo("/topic/updates/{roomId}")
    public Packet handlePacket(@DestinationVariable Long roomId,
                               Authentication authentication,
                               Packet packet) {
        User user = authentication != null ? (User) authentication.getPrincipal() : null;
        switch (packet.getType()) {
            case TEXT_PACKET:
                return packetHandler.handleTextPacket((TextPacket) packet, user);
            default:
                throw new IllegalArgumentException("Invalid packet type");
        }
    }
}
