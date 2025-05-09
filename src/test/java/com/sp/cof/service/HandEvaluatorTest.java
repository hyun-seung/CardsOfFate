package com.sp.cof.service;

import com.sp.cof.domain.entity.Card;
import com.sp.cof.domain.entity.HandRank;
import com.sp.cof.service.patternchecker.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HandEvaluatorTest {

    /***
     * HighCard : 조합이 없을 경우 제일 높은 카드로 처리
     */
    @Test
    void testHighCard() {
        HandEvaluator evaluator = new HandEvaluator();
        List<Card> hand = List.of(
                Card.LUNARIA_FATE,
                Card.LUNARIA_NINE,
                Card.IGNIS_TWO,
                Card.NOCTIS_ONE,
                Card.FORTUNA_TEN
        );
        assertEquals(HandRank.HIGH_CARD, evaluator.evulate(hand).rank());
    }

    /***
     * OnePair : 같은 숫자가 1쌍 있을 경우
     */
    @Test
    void testOnePair() {
        HandEvaluator evaluator = new HandEvaluator();
        List<Card> hand = List.of(
                Card.LUNARIA_FATE,
                Card.LUNARIA_NINE,
                Card.IGNIS_FATE,
                Card.NOCTIS_ONE,
                Card.FORTUNA_TEN
        );
        assertEquals(HandRank.ONE_PAIR, evaluator.evulate(hand).rank());
    }

    /***
     * TwoPair : 같은 숫자가 2쌍 있을 경우
     */
    @Test
    void testTwoPair() {
        HandEvaluator evaluator = new HandEvaluator();
        List<Card> hand = List.of(
                Card.LUNARIA_FATE,
                Card.LUNARIA_NINE,
                Card.IGNIS_FATE,
                Card.NOCTIS_ONE,
                Card.FORTUNA_NINE
        );
        assertEquals(HandRank.TWO_PAIR, evaluator.evulate(hand).rank());
    }

    /***
     * ThreeOfAKind : 같은 숫자가 3개 있을 경우
     */
    @Test
    void testThreeOfAKind() {
        HandEvaluator evaluator = new HandEvaluator();
        List<Card> hand = List.of(
                Card.LUNARIA_FATE,
                Card.LUNARIA_NINE,
                Card.IGNIS_FATE,
                Card.NOCTIS_ONE,
                Card.FORTUNA_FATE
        );
        assertEquals(HandRank.THREE_OF_A_KIND, evaluator.evulate(hand).rank());
    }

    /***
     * Straight : 숫자가 연속으로 5개 연결되는 경우
     */
    @Test
    void testStraight() {
        HandEvaluator evaluator = new HandEvaluator();
        List<Card> hand = List.of(
                Card.LUNARIA_TEN,
                Card.LUNARIA_NINE,
                Card.IGNIS_EIGHT,
                Card.NOCTIS_SEVEN,
                Card.FORTUNA_SIX
        );
        assertEquals(HandRank.STRAIGHT, evaluator.evulate(hand).rank());
    }

    /***
     * Flush : 문양이 5개 같은 경우
     */
    @Test
    void testFlush() {
        HandEvaluator evaluator = new HandEvaluator();
        List<Card> hand = List.of(
                Card.LUNARIA_TEN,
                Card.LUNARIA_NINE,
                Card.LUNARIA_EIGHT,
                Card.LUNARIA_SEVEN,
                Card.LUNARIA_FIVE
        );
        assertEquals(HandRank.FLUSH, evaluator.evulate(hand).rank());
    }

    /***
     * FullHouse : 같은 숫자 3개 + 2개인 경우
     */
    @Test
    void testFullHouse() {
        HandEvaluator evaluator = new HandEvaluator();
        List<Card> hand = List.of(
                Card.LUNARIA_TEN,
                Card.IGNIS_TEN,
                Card.FORTUNA_TEN,
                Card.LUNARIA_ONE,
                Card.NOCTIS_ONE
        );
        assertEquals(HandRank.FULL_HOUSE, evaluator.evulate(hand).rank());
    }

    /**
     * FourOfAKind : 같은 숫자가 4개 있을 경우
     */
    @Test
    void testFourOfAKind() {
        HandEvaluator evaluator = new HandEvaluator();
        List<Card> hand = List.of(
                Card.LUNARIA_TEN,
                Card.IGNIS_TEN,
                Card.FORTUNA_TEN,
                Card.NOCTIS_TEN,
                Card.NOCTIS_ONE
        );
        assertEquals(HandRank.FOUR_OF_A_KIND, evaluator.evulate(hand).rank());
    }

    /**
     * StraightFlush : 같은 문양인 숫자가 연속으로 5개 있을 경우
     */
    @Test
    void testStraightFlush() {
        HandEvaluator evaluator = new HandEvaluator();
        List<Card> hand = List.of(
                Card.LUNARIA_TEN,
                Card.LUNARIA_THREAD,
                Card.LUNARIA_WEAVER,
                Card.LUNARIA_FATE,
                Card.LUNARIA_NINE
        );
        assertEquals(HandRank.STRAIGHT_FLUSH, evaluator.evulate(hand).rank());
    }

    /**
     * RoyalStraightFlush : 10, J, Q, K, A가 같은 문양일 경우
     */
    @Test
    void testRoyalStraightFlush() {
        HandEvaluator evaluator = new HandEvaluator();
        List<Card> hand = List.of(
                Card.LUNARIA_TEN,
                Card.LUNARIA_THREAD,
                Card.LUNARIA_WEAVER,
                Card.LUNARIA_FATE,
                Card.LUNARIA_ONE
        );
        assertEquals(HandRank.ROYAL_STRAIGHT_FLUSH, evaluator.evulate(hand).rank());
    }

}