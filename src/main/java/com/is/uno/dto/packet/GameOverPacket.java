package com.is.uno.dto.packet;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class GameOverPacket extends Packet {

    private Map<String, Long> stats; // Player username -> Score
    private String winner;

    public GameOverPacket() {
        super(PacketType.GAME_OVER_PACKET);
    }
}
