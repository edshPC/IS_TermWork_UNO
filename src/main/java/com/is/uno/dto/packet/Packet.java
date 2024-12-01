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

    private final Type type;

    public enum Type {
        TEXT_PACKET,
    }

}
