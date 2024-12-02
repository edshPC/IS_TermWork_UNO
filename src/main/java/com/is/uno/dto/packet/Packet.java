package com.is.uno.dto.packet;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = TextPacket.class, name = "TEXT_PACKET"),
})
public abstract class Packet {

    private final PacketType type;

    public enum PacketType {
        TEXT_PACKET,
        ACTION_PACKET,
        GAME_STATE_PACKET,
        GAME_OVER_PACKET,
        TAKE_CARD_PACKET,
        PUT_CARD_PACKET,
        PLAYER_JOIN_PACKET,
        PLAYER_ACTION_PACKET,
    }

}
