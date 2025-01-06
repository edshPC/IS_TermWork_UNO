package com.is.uno.dto.packet;

import com.is.uno.core.GamePlayer;
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

    public static PlayerActionPacket create(GamePlayer player, Action action) {
        PlayerActionPacket packet = new PlayerActionPacket();
        packet.setUsername(player.getUsername());
        packet.setAction(action);
        return packet;
    }

}
