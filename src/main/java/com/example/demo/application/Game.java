package com.example.demo.application;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class Game {

    private static final int MAX_GAME_USERS = 4;

    private static final Hashtable<String, Integer> games = new Hashtable<>();

    public void createRoom(String channelId) {
        if (games.getOrDefault(channelId, 0) == MAX_GAME_USERS) {
            throw new IllegalStateException("해당 게임 룸에 참가할 수 없습니다.");
        }
        games.put(channelId, games.getOrDefault(channelId, 0) + 1);
    }

    public Map<Integer, List<Integer>> divideCards() {
        Map<Integer, List<Integer>> cardsPerUser = new HashMap<>();
        List<Integer> cards = RandomNumberGenerator.shuffle();
        for (int i = 1; i <= 4 ; i++) {
            cardsPerUser.put(i, cards.subList(2*(i-1), 2*i));
        }
        return cardsPerUser;
    }

    public boolean isFull(String channelId) {
        return games.get(channelId) == MAX_GAME_USERS;
    }

    public Integer getUserCount(String channelId) {
        return games.get(channelId);
    }
}
