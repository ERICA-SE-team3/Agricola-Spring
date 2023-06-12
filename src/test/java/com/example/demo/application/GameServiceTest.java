package com.example.demo.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.demo.dao.GameDao;
import com.example.demo.domain.Game;
import com.example.demo.dto.ActionMessageResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GameServiceTest {

    @Autowired
    private GameService gameService;

    @Autowired
    private GameDao gameDao;

    @Autowired
    private ObjectMapper objectMapper;

    @AfterEach
    void tearDown() {
        gameDao.clear();
    }


    @Test
    void 게임방을_생성하고_플레이어가_참여할_수_있다() {
        gameService.createOrJoinRoom("1번방", "player1");
        Integer expected = 1;

        Integer actual = gameDao.countByChannelId("1번방");

        assertEquals(expected, actual);
    }

    @Test
    void 게임방을_생성하고_플레이어는_최대_4명_참여할_수_있다() {
        gameService.createOrJoinRoom("1번방", "player1");
        gameService.createOrJoinRoom("1번방", "player2");
        gameService.createOrJoinRoom("1번방", "player3");
        gameService.createOrJoinRoom("1번방", "player4");
        Integer expected = 4;

        Game foundGame = gameDao.findByChannelId("1번방");
        Integer actual = gameDao.countByChannelId("1번방");

        assertAll(
                () -> assertEquals(expected, actual),
                () -> assertTrue(foundGame.isFull())
        );
    }

    @Test
    void 게임방을_생성하고_같은_플레이어가_참여_요청을_하면_게임방의_유저수가_오르지_않는다() {
        gameService.createOrJoinRoom("1번방", "player1");
        gameService.createOrJoinRoom("1번방", "player2");
        gameService.createOrJoinRoom("1번방", "player3");
        gameService.createOrJoinRoom("1번방", "player1");
        Integer expected = 3;

        Game foundGame = gameDao.findByChannelId("1번방");
        Integer actual = gameDao.countByChannelId("1번방");

        assertAll(
                () -> assertEquals(expected, actual),
                () -> assertFalse(foundGame.isFull())
        );
    }

    @Test
    void 게임방에_4명이_있으면_게임방에_입장할_수_없다() {
        gameService.createOrJoinRoom("1번방", "player1");
        gameService.createOrJoinRoom("1번방", "player2");
        gameService.createOrJoinRoom("1번방", "player3");
        gameService.createOrJoinRoom("1번방", "player4");

        assertThrows(IllegalStateException.class, () -> gameService.createOrJoinRoom("1번방", "player5"),
                "해당 게임 룸에 참가할 수 없습니다.");
    }

    @Test
    void 게임방에서_유저가_나갈_수_있다() {
        gameService.createOrJoinRoom("1번방", "player1");
        gameService.createOrJoinRoom("1번방", "player2");
        Integer expected = 1;

        gameService.removeUser("player2");
        Integer actual = gameDao.countByChannelId("1번방");

        assertEquals(expected, actual);
    }

    @Test
    void 게임방에_대한_플레이어_수를_반환한다() throws JsonProcessingException {
        gameService.createOrJoinRoom("1번방", "player1");
        gameService.createOrJoinRoom("1번방", "player2");

        ActionMessageResponse response = gameService.getUserCountResponse("1번방");
        String expected = getData();

        assertAll(
                () -> assertThat(response.getType()).isEqualTo("userCountCheck"),
                () -> assertThat(response.getSender()).isEqualTo("server"),
                () -> assertThat(response.getChannelId()).isEqualTo("1번방"),
                () -> assertThat(response.getData()).isEqualTo(expected)
        );
    }

    private String getData() throws JsonProcessingException {
        Map<String, Integer> map = new HashMap<>();
        int userCount = gameDao.countByChannelId("1번방");
        map.put("userCount", userCount);
        String expected = objectMapper.writeValueAsString(map);
        return expected;
    }

    @Test
    void 게임방에_대한_카드를_분배한다() {
        gameService.createOrJoinRoom("1번방", "player1");
        gameService.createOrJoinRoom("1번방", "player2");
        gameService.createOrJoinRoom("1번방", "player3");
        gameService.createOrJoinRoom("1번방", "player4");

        ActionMessageResponse response = gameService.getUserCardsResponse("1번방");

        assertAll(
                () -> assertThat(response.getType()).isEqualTo("cardDeck"),
                () -> assertThat(response.getSender()).isEqualTo("server"),
                () -> assertThat(response.getChannelId()).isEqualTo("1번방")
        );
    }

    @Test
    void 플레이어가_가득차지_않으면_게임방에_대한_카드를_분배할_수_없다() {
        gameService.createOrJoinRoom("1번방", "player1");
        gameService.createOrJoinRoom("1번방", "player2");
        gameService.createOrJoinRoom("1번방", "player3");

        assertThrows(IllegalStateException.class, () -> gameService.getUserCardsResponse("1번방"), "해당 게임 룸에는 카드를 분배할 수 없습니다.");
    }
}
