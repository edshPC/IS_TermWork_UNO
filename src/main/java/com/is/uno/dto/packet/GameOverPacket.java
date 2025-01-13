package com.is.uno.dto.packet;

import com.is.uno.dto.api.GameStatDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GameOverPacket extends Packet {

    private List<GameStatDTO> stats; // Player username -> Score
    private String winner;
    private boolean gameOver;

    public GameOverPacket() {
        super(PacketType.GAME_OVER_PACKET);
    }
}
