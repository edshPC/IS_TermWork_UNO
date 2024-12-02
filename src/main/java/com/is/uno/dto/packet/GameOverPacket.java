package com.is.uno.dto.packet;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class GameOverPacket extends Packet {

    private Map<Long, Long> stats; // Player ID -> Score
    private Long winnerId;

    public GameOverPacket() {
        super(PacketType.GAME_OVER_PACKET);
    }
}
