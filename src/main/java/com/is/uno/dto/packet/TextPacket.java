package com.is.uno.dto.packet;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TextPacket extends Packet {

    private String text;
    private String sender;
    private TextType textType = TextType.PLAYER;

    public TextPacket() {
        super(PacketType.TEXT_PACKET);
    }

    public static TextPacket createSystem(String text) {
        TextPacket packet = new TextPacket();
        packet.text = text;
        packet.textType = TextType.SYSTEM;
        return packet;
    }

    public enum TextType {
        PLAYER,
        SYSTEM
    }

}
