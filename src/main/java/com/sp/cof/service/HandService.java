package com.sp.cof.service;

import com.sp.cof.common.Constant;
import com.sp.cof.common.ErrorCode;
import com.sp.cof.domain.Deck;
import com.sp.cof.domain.card.Card;
import com.sp.cof.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class HandService {

    public List<Card> createInitialHand(Deck deck) {
        if (Objects.isNull(deck)) {
            throw new IllegalArgumentException("덱은 null일 수 없습니다");
        }

        List<Card> hand = new ArrayList<>();
        for (int i = 0; i < Constant.INITIAL_HAND_SIZE; i++) {
            Card card = deck.draw();
            if (Objects.isNull(card)) {
                throw new IllegalStateException("초기 손패 생성 중 카드가 부족합니다");
            }
            hand.add(card);
        }
        return hand;
    }

    public List<Card> updateHandAfterPlay(Deck deck, List<Card> currentHand, List<Card> usedCards) {
        validateHandUpdateInputs(deck, currentHand, usedCards);

        List<Card> updatedHand = new ArrayList<>(currentHand);
        updatedHand.removeAll(usedCards);

        int cardsToDrawn = usedCards.size();
        int actualDrawnCards = 0;

        log.info("손패 업데이트 시작 : {}장 제거, {}장 뽑기 시도", usedCards.size(), cardsToDrawn);

        for (int i = 0; i < cardsToDrawn; i++) {
            Card drawnCard = deck.draw();
            if (Objects.isNull(drawnCard)) {
                updatedHand.add(drawnCard);
                actualDrawnCards++;
            } else {
                log.warn("덱이 비어있어 카드를 뽑을 수 없습니다. {{}/{})", actualDrawnCards, cardsToDrawn);
                break;
            }
        }
        log.info("손패 업데이트 완료 : 현재 {}장 ({}장 뽑음)", updatedHand.size(), actualDrawnCards);
        return updatedHand;
    }

    private void validateHandUpdateInputs(Deck deck, List<Card> currentHand, List<Card> usedCards) {
        if (Objects.isNull(deck)) {
            throw new IllegalArgumentException("덱은 null일 수 없습니다");
        }
        if (Objects.isNull(currentHand)) {
            throw new BusinessException(ErrorCode.INVALID_CURRENT_HAND);
        }
        if (ObjectUtils.isEmpty(usedCards)) {
            throw new BusinessException(ErrorCode.INVALID_USED_CARDS);
        }
        if (!currentHand.containsAll(usedCards)) {
            throw new BusinessException(ErrorCode.CARDS_NOT_IN_HAND);
        }
    }
}
