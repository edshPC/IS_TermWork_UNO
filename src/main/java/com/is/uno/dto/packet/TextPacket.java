package com.is.uno.dto.packet;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TextPacket extends Packet {

    private String text;
    private String sender;

    public TextPacket() {
        super(PacketType.TEXT_PACKET);
    }
}
