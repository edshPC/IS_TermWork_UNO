package com.is.uno.dto.packet;

import com.is.uno.dto.api.CardDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameStatePacket extends Packet {

    private CardDTO currentCard;
    private boolean orderReversed;
    private String currentPlayer;

    public GameStatePacket() {
        super(PacketType.GAME_STATE_PACKET);
    }

}
