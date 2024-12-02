package com.is.uno.core;

import com.is.uno.dto.api.CardDTO;
import lombok.Getter;

import java.util.Map;

@Getter
public class GameState {

    private CardDTO currentCard;
    private boolean orderReversed;
    private Long currentPlayerId;
    private CardDeck deck;
    private Map<Long, GamePlayer> players;

}
