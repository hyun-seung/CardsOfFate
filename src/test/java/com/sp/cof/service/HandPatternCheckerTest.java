package com.sp.cof.service;

import com.sp.cof.domain.card.Card;
import com.sp.cof.service.patternchecker.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HandPatternCheckerTest {

    /***
     * OnePair : 같은 숫자가 1쌍 있을 경우
     */
    @Test
    void testOnePairTrueCase() {
        HandPatternChecker checker = new OnePairChecker();
        List<Card> hand = List.of(
                Card.LUNARIA_FATE,
                Card.LUNARIA_NINE,
                Card.IGNIS_FATE,
                Card.NOCTIS_ONE,
                Card.FORTUNA_TEN
        );
        assertTrue(checker.matches(hand));
    }

    @Test
    void testOnePairFalseCase() {
        HandPatternChecker checker = new OnePairChecker();
        List<Card> hand = List.of(
                Card.LUNARIA_FATE,
                Card.LUNARIA_NINE,
                Card.IGNIS_WEAVER,
                Card.NOCTIS_ONE,
                Card.FORTUNA_TEN
        );
        assertFalse(checker.matches(hand));
    }

    /***
     * TwoPair : 같은 숫자가 2쌍 있을 경우
     */
    @Test
    void testTwoPairTrueCase() {
        HandPatternChecker checker = new TwoPairChecker();
        List<Card> hand = List.of(
                Card.LUNARIA_FATE,
                Card.LUNARIA_NINE,
                Card.IGNIS_FATE,
                Card.NOCTIS_ONE,
                Card.FORTUNA_NINE
        );
        assertTrue(checker.matches(hand));
    }

    @Test
    void testTwoPairFalseCase() {
        HandPatternChecker checker = new TwoPairChecker();
        List<Card> hand = List.of(
                Card.LUNARIA_FATE,
                Card.LUNARIA_NINE,
                Card.IGNIS_FATE,
                Card.NOCTIS_ONE,
                Card.FORTUNA_TEN
        );
        assertFalse(checker.matches(hand));
    }

    /***
     * ThreeOfAKind : 같은 숫자가 3개 있을 경우
     */
    @Test
    void testThreeOfAKindTrueCase() {
        HandPatternChecker checker = new ThreeOfAKindChecker();
        List<Card> hand = List.of(
                Card.LUNARIA_FATE,
                Card.LUNARIA_NINE,
                Card.IGNIS_FATE,
                Card.NOCTIS_ONE,
                Card.FORTUNA_FATE
        );
        assertTrue(checker.matches(hand));
    }

    @Test
    void testThreeOfAKindFalseCase() {
        HandPatternChecker checker = new ThreeOfAKindChecker();
        List<Card> hand = List.of(
                Card.LUNARIA_FATE,
                Card.LUNARIA_NINE,
                Card.IGNIS_FATE,
                Card.NOCTIS_ONE,
                Card.FORTUNA_NINE
        );
        assertFalse(checker.matches(hand));
    }

    /***
     * Straight : 숫자가 연속으로 5개 연결되는 경우
     */
    @Test
    void testStraightTrueCase() {
        HandPatternChecker checker = new StraightChecker();
        List<Card> hand = List.of(
                Card.LUNARIA_TEN,
                Card.LUNARIA_NINE,
                Card.IGNIS_EIGHT,
                Card.NOCTIS_SEVEN,
                Card.FORTUNA_SIX
        );
        assertTrue(checker.matches(hand));
    }

    // 12, 13, 1, 2, 3 은 Straight가 아님.
    @Test
    void testStraightFalseCase() {
        HandPatternChecker checker = new StraightChecker();
        List<Card> hand = List.of(
                Card.LUNARIA_ONE,
                Card.LUNARIA_TWO,
                Card.IGNIS_THREE,
                Card.NOCTIS_FATE,
                Card.FORTUNA_WEAVER
        );
        assertFalse(checker.matches(hand));
    }

    /***
     * Flush : 문양이 5개 같은 경우
     */
    @Test
    void testFlushTrueCase() {
        HandPatternChecker checker = new FlushChecker();
        List<Card> hand = List.of(
                Card.LUNARIA_TEN,
                Card.LUNARIA_NINE,
                Card.LUNARIA_EIGHT,
                Card.LUNARIA_SEVEN,
                Card.LUNARIA_SIX
        );
        assertTrue(checker.matches(hand));
    }

    @Test
    void testFlushFalseCase() {
        HandPatternChecker checker = new FlushChecker();
        List<Card> hand = List.of(
                Card.LUNARIA_TEN,
                Card.LUNARIA_NINE,
                Card.LUNARIA_EIGHT,
                Card.LUNARIA_SEVEN,
                Card.IGNIS_SIX
        );
        assertFalse(checker.matches(hand));
    }

    /***
     * FullHouse : 같은 숫자 3개 + 2개인 경우
     */
    @Test
    void testFullHouseTrueCase() {
        HandPatternChecker checker = new FullHouseChecker();
        List<Card> hand = List.of(
                Card.LUNARIA_TEN,
                Card.IGNIS_TEN,
                Card.FORTUNA_TEN,
                Card.LUNARIA_ONE,
                Card.NOCTIS_ONE
        );
        assertTrue(checker.matches(hand));
    }

    @Test
    void testFullHouseFalseCase() {
        HandPatternChecker checker = new FullHouseChecker();
        List<Card> hand = List.of(
                Card.LUNARIA_TEN,
                Card.IGNIS_TEN,
                Card.FORTUNA_TEN,
                Card.NOCTIS_TEN,
                Card.NOCTIS_ONE
        );
        assertFalse(checker.matches(hand));
    }

    /**
     * FourOfAKind : 같은 숫자가 4개 있을 경우
     */
    @Test
    void testFourOfAKindTrueCase() {
        HandPatternChecker checker = new FourOfAKindChecker();
        List<Card> hand = List.of(
                Card.LUNARIA_TEN,
                Card.IGNIS_TEN,
                Card.FORTUNA_TEN,
                Card.NOCTIS_TEN,
                Card.NOCTIS_ONE
        );
        assertTrue(checker.matches(hand));
    }

    @Test
    void testFourOfAKindFalseCase() {
        HandPatternChecker checker = new FourOfAKindChecker();
        List<Card> hand = List.of(
                Card.LUNARIA_TEN,
                Card.IGNIS_TEN,
                Card.FORTUNA_TEN,
                Card.NOCTIS_ONE,
                Card.NOCTIS_ONE
        );
        assertFalse(checker.matches(hand));
    }

    /**
     * StraightFlush : 같은 문양인 숫자가 연속으로 5개 있을 경우
     */
    @Test
    void testStraightFlushTrueCase() {
        HandPatternChecker checker = new StraightFlushChecker();
        List<Card> hand = List.of(
                Card.LUNARIA_TEN,
                Card.LUNARIA_THREAD,
                Card.LUNARIA_WEAVER,
                Card.LUNARIA_FATE,
                Card.LUNARIA_NINE
        );
        assertTrue(checker.matches(hand));
    }

    // 10, 11, 12, 13, 1 은 Straight가 아님
    @Test
    void testStraightFlushFalseCase() {
        HandPatternChecker checker = new StraightFlushChecker();
        List<Card> hand = List.of(
                Card.LUNARIA_TEN,
                Card.LUNARIA_THREAD,
                Card.LUNARIA_WEAVER,
                Card.LUNARIA_FATE,
                Card.LUNARIA_ONE
        );
        assertFalse(checker.matches(hand));
    }

    /**
     * RoyalStraightFlush : 10, J, Q, K, A가 같은 문양일 경우
     */
    @Test
    void testRoyalStraightFlushTrueCase() {
        HandPatternChecker checker = new RoyalStraightFlushChecker();
        List<Card> hand = List.of(
                Card.LUNARIA_TEN,
                Card.LUNARIA_THREAD,
                Card.LUNARIA_WEAVER,
                Card.LUNARIA_FATE,
                Card.LUNARIA_ONE
        );
        assertTrue(checker.matches(hand));
    }

    // 10, 11, 12, 13, 1 은 Straight가 아님
    @Test
    void testRoyalStraightFlushFalseCase() {
        HandPatternChecker checker = new RoyalStraightFlushChecker();
        List<Card> hand = List.of(
                Card.LUNARIA_TEN,
                Card.LUNARIA_THREAD,
                Card.LUNARIA_WEAVER,
                Card.LUNARIA_FATE,
                Card.LUNARIA_NINE
        );
        assertFalse(checker.matches(hand));
    }
}