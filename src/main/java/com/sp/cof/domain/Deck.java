package com.sp.cof.domain;

import com.sp.cof.domain.entity.Card;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class Deck {

    private final Deque<Card> cards;

    public Deck(long seed) {
        this.cards = createShuffledDeck(seed);
    }

    private Deque<Card> createShuffledDeck(long seed) {
        List<Card> cardList = Arrays.asList(Card.values());
        Collections.shuffle(cardList, new Random(seed));
        return new ArrayDeque<>(cardList);
    }

    public Card draw() {
        return cards.pollFirst();
    }

    public int size() {
        return cards.size();
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }
}
