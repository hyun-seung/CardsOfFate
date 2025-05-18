package com.sp.cof.repository.deck;

import com.sp.cof.domain.Deck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class InMemoryDeckRepositoryTest {

    private InMemoryDeckRepository deckRepository;

    @BeforeEach
    void setUp() {
        deckRepository = new InMemoryDeckRepository();
    }

    @DisplayName("Deck 저장 후 gameId로 조회하면 저장한 Deck을 반환한다")
    @Test
    void saveAndFindByGameId_shouldReturnSavedDeck() {
        // given
        String gameId = "123-abc";
        long seed = 1234L;
        Deck deck = new Deck(seed);

        // when
        deckRepository.save(gameId, deck);
        Deck result = deckRepository.findByGameId(gameId);

        // then
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(deck);
    }

    @DisplayName("존재하지 않는 gameId로 조회하면 null을 반환한다")
    @Test
    void findByGameId_withUnknownId_shouldReturnNull() {
        // given
        String unknownGameId = "unknown";

        // when
        Deck result = deckRepository.findByGameId(unknownGameId);

        // then
        assertThat(result).isNull();
    }
}