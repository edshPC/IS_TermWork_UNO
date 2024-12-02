package com.is.uno.dto.packet;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = TextPacket.class, name = "TEXT_PACKET"),
        @JsonSubTypes.Type(value = ActionPacket.class, name = "ACTION_PACKET"),
        @JsonSubTypes.Type(value = GameStatePacket.class, name = "GAME_STATE_PACKET"),
        @JsonSubTypes.Type(value = GameOverPacket.class, name = "GAME_OVER_PACKET"),
        @JsonSubTypes.Type(value = TakeCardPacket.class, name = "TAKE_CARD_PACKET"),
        @JsonSubTypes.Type(value = PutCardPacket.class, name = "PUT_CARD_PACKET"),
        @JsonSubTypes.Type(value = PlayerJoinPacket.class, name = "PLAYER_JOIN_PACKET"),
        @JsonSubTypes.Type(value = PlayerActionPacket.class, name = "PLAYER_ACTION_PACKET"),
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
