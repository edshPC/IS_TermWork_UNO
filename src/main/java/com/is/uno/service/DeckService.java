package com.is.uno.service;

import com.is.uno.dao.DeckRepository;
import com.is.uno.model.Deck;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeckService {
    private final DeckRepository deckRepository;

    public List<Deck> getActualDeck() {
        return deckRepository.findAll();
    }

}
