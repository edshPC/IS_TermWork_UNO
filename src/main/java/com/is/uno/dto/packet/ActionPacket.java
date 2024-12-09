package com.is.uno.dto.packet;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActionPacket extends Packet {

    private Action action;

    public ActionPacket() {
        super(PacketType.ACTION_PACKET);
    }

    public static ActionPacket create(Action action) {
        ActionPacket packet = new ActionPacket();
        packet.setAction(action);
        return packet;
    }

}
