package com.is.uno.socket;

import com.is.uno.core.GameCoreProvider;
import com.is.uno.dto.packet.Packet;
import com.is.uno.exception.ForbiddenException;
import com.is.uno.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class UpdateController {

    private final GameCoreProvider gameCoreProvider;

    //@SendTo("/topic/updates/{roomId}")
    @MessageMapping("/room/{roomId}")
    public void handlePacket(@DestinationVariable Long roomId,
                             SimpMessageHeaderAccessor headerAccessor,
                             Packet packet) {
        Authentication authentication = (Authentication) headerAccessor.getUser();
        if (authentication == null) {
            throw new ForbiddenException("You are not logged");
        }
        User user = (User) authentication.getPrincipal();

        PacketHandler packetHandler = gameCoreProvider.provideGameCore(roomId).getPacketHandler();
        packetHandler.handle(packet, user);
    }

}
