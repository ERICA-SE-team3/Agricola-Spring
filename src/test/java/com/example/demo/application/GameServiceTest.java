//package com.example.demo.application;
//
//import static org.junit.jupiter.api.Assertions.assertAll;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertFalse;
//import static org.junit.jupiter.api.Assertions.assertIterableEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//import java.util.Map;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//
//@SpringBootTest
//class GameServiceTest {
//
//
//    @Test
//    void 게임방을_생성하고_플레이어가_참여할_수_있다() {
//        GameService gameService = new GameService();
//        gameService.createOrJoinRoom("1번방", "player1");
//        Integer expected = 1;
//
//        Integer actual = gameService.getUserCount("1번방");
//
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    void 게임방을_생성하고_플레이어는_최대_4명_참여할_수_있다() {
//        GameService gameService = new GameService();
//        gameService.createOrJoinRoom("1번방", "player1");
//        gameService.createOrJoinRoom("1번방", "player2");
//        gameService.createOrJoinRoom("1번방", "player3");
//        gameService.createOrJoinRoom("1번방", "player4");
//        Integer expected = 4;
//
//        Integer actual = gameService.getUserCount("1번방");
//
//        assertAll(
//                () -> assertEquals(expected, actual),
//                () -> assertTrue(gameService.isFull("1번방"))
//        );
//    }
//
//    @Test
//    void 게임방을_생성하고_같은_플레이어가_참여_요청을_하면_게임방의_유저수가_오르지_않는다() {
//        GameService gameService = new GameService();
//        gameService.createOrJoinRoom("1번방", "player1");
//        gameService.createOrJoinRoom("1번방", "player2");
//        gameService.createOrJoinRoom("1번방", "player3");
//        gameService.createOrJoinRoom("1번방", "player1");
//        Integer expected = 3;
//
//        Integer actual = gameService.getUserCount("1번방");
//
//        assertAll(
//                () -> assertEquals(expected, actual),
//                () -> assertFalse(gameService.isFull("1번방"))
//        );
//    }
//
//    @Test
//    void 게임방에_4명이_있으면_게임방에_입장할_수_없다() {
//        GameService gameService = new GameService();
//        gameService.createOrJoinRoom("1번방", "player1");
//        gameService.createOrJoinRoom("1번방", "player2");
//        gameService.createOrJoinRoom("1번방", "player3");
//        gameService.createOrJoinRoom("1번방", "player4");
//
//        assertThrows(IllegalStateException.class, () -> gameService.createOrJoinRoom("1번방", "player5"),
//                "해당 게임 룸에 참가할 수 없습니다.");
//    }
//
//    @Test
//    void 유저들에게_1부터_8번까지의_카드를_나누어준다() {
//        GameService gameService = new GameService();
//        gameService.createOrJoinRoom("1번방", "player1");
//        gameService.createOrJoinRoom("1번방", "player2");
//        gameService.createOrJoinRoom("1번방", "player3");
//        gameService.createOrJoinRoom("1번방", "player4");
//
//        List<Integer> cardDeckList = new ArrayList<>();
//        Map<Integer, List<Integer>> cardsPerUserMap = gameService.divideCards("1번방");
//        for (Integer playerNumber : cardsPerUserMap.keySet()) {
//            List<Integer> cardList = cardsPerUserMap.get(playerNumber);
//            cardDeckList.addAll(cardList);
//        }
//
//        Collections.sort(cardDeckList);
//        assertIterableEquals(cardDeckList, List.of(1, 2, 3, 4, 5, 6, 7, 8));
//    }
//
//    @Test
//    void 게임방에_4명이_차지_않으면_카드를_분배할_수_없다() {
//        GameService gameService = new GameService();
//        gameService.createOrJoinRoom("1번방", "player1");
//        gameService.createOrJoinRoom("1번방", "player2");
//        gameService.createOrJoinRoom("1번방", "player3");
//
//        assertThrows(IllegalStateException.class, () -> gameService.divideCards("1번방"), "해당 게임 룸에는 카드를 분배할 수 없습니다.");
//    }
//
//    @Test
//    void 게임방에서_유저가_나갈_수_있다() {
//        GameService gameService = new GameService();
//        gameService.createOrJoinRoom("1번방", "player1");
//        gameService.createOrJoinRoom("1번방", "player2");
//        Integer expected = 1;
//
//        gameService.removeUser("player2");
//        Integer actual = gameService.getUserCount("1번방");
//
//        assertEquals(expected, actual);
//    }
//}
