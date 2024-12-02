package com.is.uno.dto.packet;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerActionPacket extends Packet {

    private String username;
    private Action action;

    public PlayerActionPacket() {
        super(PacketType.PLAYER_ACTION_PACKET);
    }
}
