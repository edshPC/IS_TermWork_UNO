package com.is.uno.service;

import com.is.uno.config.Deck;
import com.is.uno.config.GameConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(GameConfig.class)
public class DeckService {

    private final GameConfig gameConfig;

    public Deck getActualDeck() {
        String activeDeck = gameConfig.getActiveDeck();
        return gameConfig.getDecks().get(activeDeck);
    }

}
