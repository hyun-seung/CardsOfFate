package com.sp.cof.service;

import com.sp.cof.common.Constant;
import com.sp.cof.domain.Deck;
import com.sp.cof.domain.record.GameInfo;
import com.sp.cof.domain.entity.Card;
import com.sp.cof.repository.DeckRepository;
import com.sp.cof.repository.InMemoryDeckRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class GameService {

    private final DeckRepository deckRepository;

    public GameService(InMemoryDeckRepository inMemoryDeckRepository) {
        deckRepository = new InMemoryDeckRepository();
    }

    public GameInfo startGame(String gameId, long seed) {
        Deck deck = new Deck(seed);

        List<Card> hand = new ArrayList<>();
        for (int i = 0; i < Constant.INITIAL_HAND_SIZE; i++) {
            hand.add(deck.draw());
        }

        deckRepository.save(gameId, deck);
        return new GameInfo(gameId, hand);
    }
}
