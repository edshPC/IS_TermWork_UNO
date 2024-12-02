package com.is.uno.dto.packet;

import com.is.uno.dto.api.CardDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TakeCardPacket extends Packet {

    private CardDTO card;

    public TakeCardPacket() {
        super(PacketType.TAKE_CARD_PACKET);
    }
}
