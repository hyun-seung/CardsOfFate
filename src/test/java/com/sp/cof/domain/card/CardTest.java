package com.sp.cof.domain.card;

import com.sp.cof.domain.card.Card;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {

    @DisplayName("카드의 속성과 숫자값을 _로 이어서 표현한다.")
    @Test
    void testToString() {
        Card card1 = Card.FORTUNA_ONE;
        Card card2 = Card.IGNIS_THREAD;
        Card card3 = Card.LUNARIA_WEAVER;
        Card card4 = Card.NOCTIS_SEVEN;

        String card1String = card1.toString();
        String card2String = card2.toString();
        String card3String = card3.toString();
        String card4String = card4.toString();

        assertEquals("F_1", card1String);
        assertEquals("I_T", card2String);
        assertEquals("L_W", card3String);
        assertEquals("N_7", card4String);
    }
}