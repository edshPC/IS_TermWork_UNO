package com.is.uno.dto.packet;

import com.is.uno.model.Color;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PutCardPacket extends Packet {

    private Long cardId;
    private Color newColor;

    public PutCardPacket() {
        super(PacketType.PUT_CARD_PACKET);
    }
}
