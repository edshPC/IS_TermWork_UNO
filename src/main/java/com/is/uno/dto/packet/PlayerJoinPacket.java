package com.is.uno.dto.packet;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerJoinPacket extends Packet {

    private String username;
    private String inGameName;
    private boolean ready;

    public PlayerJoinPacket() {
        super(PacketType.PLAYER_JOIN_PACKET);
    }
}
