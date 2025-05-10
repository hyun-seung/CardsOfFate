package com.sp.cof.domain;

import com.sp.cof.domain.card.Card;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class Deck {

    private final Deque<Card> cards;
    private final List<Card> drawnCards = new ArrayList<>();

    public Deck(long seed) {
        this.cards = createShuffledDeck(seed);
    }

    private Deque<Card> createShuffledDeck(long seed) {
        List<Card> cardList = Arrays.asList(Card.values());
        Collections.shuffle(cardList, new Random(seed));
        return new ArrayDeque<>(cardList);
    }

    public Card draw() {
        Card card = cards.pollFirst();
        if (Objects.nonNull(card)) {
            drawnCards.add(card);
        }
        return card;
    }

    public List<Card> getCurrentHand() {
        return Collections.unmodifiableList(drawnCards);
    }

    public int size() {
        return cards.size();
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    @Override
    public String toString() {
        return "Deck{" +
                "cards=" + cards +
                ", drawnCards=" + drawnCards +
                '}';
    }
}
