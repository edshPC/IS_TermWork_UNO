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

}
