package com.example.demo.dao;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.demo.domain.Game;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class GameDaoTest {

    @Autowired
    private GameDao gameDao;

    @BeforeEach
    void setUp() {
        gameDao.save("1번방", "player1");
    }

    @AfterEach
    void tearDown() {
        gameDao.clear();
    }

    @Test
    void 채널_ID로_게임을_조회할_수_있다() {
        Game foundGame = gameDao.findByChannelId("1번방");

        assertThat(foundGame.getPlayerCount()).isEqualTo(1);
    }

    @Test
    void 모든_게임을_조회할_수_있다() {
        gameDao.save("2번방", "player2");
        List<Game> games = gameDao.findAll();

        assertThat(games).hasSize(2);
    }

    @Test
    void 채널_ID로_참여_플레이어수를_조회할_수_있다() {
        gameDao.save("1번방", "player2");
        int playerCount = gameDao.countByChannelId("1번방");

        assertThat(playerCount).isEqualTo(2);
    }
}
