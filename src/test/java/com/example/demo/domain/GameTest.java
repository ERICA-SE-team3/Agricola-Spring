package com.example.demo.domain;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class GameTest {

    @Test
    void 게임을_생성하고_플레이어가_참여할_수_있다() {
        Game game = new Game();
        game.join("player1");
        Integer expected = 1;

        Integer actual = game.getPlayerCount();

        assertEquals(expected, actual);
    }

    @Test
    void 게임에는_최대_4명_참여할_수_있다() {
        Game game = new Game();
        game.join("player1");
        game.join("player2");
        game.join("player3");
        game.join("player4");
        Integer expected = 4;

        Integer actual = game.getPlayerCount();

        assertAll(
                () -> assertEquals(expected, actual),
                () -> assertTrue(game.isFull())
        );
    }

    @Test
    void 게임을_생성하고_같은_플레이어가_참여_요청을_하면_게임의_유저수가_오르지_않는다() {
        Game game = new Game();
        game.join("player1");
        game.join("player2");
        game.join("player3");
        game.join("player1");
        Integer expected = 3;

        Integer actual = game.getPlayerCount();

        assertAll(
                () -> assertEquals(expected, actual),
                () -> assertFalse(game.isFull())
        );
    }

    @Test
    void 게임에_4명이_있으면_게임방에_입장할_수_없다() {
        Game game = new Game();
        game.join("player1");
        game.join("player2");
        game.join("player3");
        game.join("player4");

        assertThrows(IllegalStateException.class, () -> game.join("player5"),
                "해당 게임 룸에 참가할 수 없습니다.");
    }

    @Test
    void 유저들에게_1부터_8번까지의_카드를_나누어준다() {
        Game game = new Game();
        game.join("player1");
        game.join("player2");
        game.join("player3");
        game.join("player4");

        List<Integer> cardDeckList = new ArrayList<>();
        Map<Integer, List<Integer>> cardsPerUserMap = game.divideCards();
        for (Integer playerNumber : cardsPerUserMap.keySet()) {
            List<Integer> cardList = cardsPerUserMap.get(playerNumber);
            cardDeckList.addAll(cardList);
        }

        Collections.sort(cardDeckList);
        assertIterableEquals(cardDeckList, List.of(1, 2, 3, 4, 5, 6, 7, 8));
    }

    @Test
    void 게임에_4명이_차지_않으면_카드를_분배할_수_없다() {
        Game game = new Game();
        game.join("player1");
        game.join("player2");
        game.join("player3");

        assertThrows(IllegalStateException.class, game::divideCards, "해당 게임 룸에는 카드를 분배할 수 없습니다.");
    }

    @Test
    void 게임에서_유저가_나갈_수_있다() {
        Game game = new Game();
        game.join("player1");
        game.join("player2");
        Integer expected = 1;

        game.quit("player2");
        Integer actual = game.getPlayerCount();

        assertEquals(expected, actual);
    }
}
