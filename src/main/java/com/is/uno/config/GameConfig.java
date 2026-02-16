package com.is.uno.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@Data
@ConfigurationProperties(prefix = "game")
public class GameConfig {
    private String activeDeck;
    private Map<String, Deck> decks;
}
