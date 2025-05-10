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
import java.util.Objects;

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

    public GameInfo getGameInfo(String gameId) {
        Deck deck = deckRepository.findById(gameId);
        if (Objects.isNull(deck)) {
            throw new IllegalArgumentException("해당 게임 ID의 덱이 존재하지 않습니다. gameId : " + gameId);
        }

        List<Card> hand = deck.getCurrentHand();
        return new GameInfo(gameId, hand);
    }
}
