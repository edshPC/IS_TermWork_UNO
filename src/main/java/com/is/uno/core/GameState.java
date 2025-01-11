package com.is.uno.core;

import com.is.uno.dto.api.CardDTO;
import com.is.uno.dto.packet.GameStatePacket;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameState {

    private CardDTO currentCard;
    private boolean orderReversed = false;
    private GamePlayer currentPlayer;
    private CardDeck deck;

    public void reverseOrder() {
        orderReversed = !orderReversed;
    }

    public GameStatePacket getGameStatePacket() {
        var pkt = new GameStatePacket();
        pkt.setCurrentPlayer(currentPlayer.getUsername());
        pkt.setCurrentCard(currentCard);
        pkt.setOrderReversed(orderReversed);
        return pkt;
    }

}
