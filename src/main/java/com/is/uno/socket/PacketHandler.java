package com.is.uno.socket;

import com.is.uno.dto.packet.Packet;
import com.is.uno.dto.packet.TextPacket;
import com.is.uno.model.User;
import com.is.uno.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PacketHandler {

    private final MessageService messageService;

    public Packet handleTextPacket(TextPacket packet, User user) {
        if (user != null) {
            packet.setSender(user.getUsername());

        }
        else packet.setSender("Unknown");
        return packet;
    }

}
