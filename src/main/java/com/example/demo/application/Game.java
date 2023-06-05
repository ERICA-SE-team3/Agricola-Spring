package com.example.demo.application;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class Game {

    private static final int MAX_GAME_USERS = 4;

    private final Hashtable<String, Set<String>> games = new Hashtable<>();

    public void createOrJoinRoom(String channelId, String sessionId) {
        if (games.getOrDefault(channelId, new HashSet<>()).size() == MAX_GAME_USERS) {
            throw new IllegalStateException("해당 게임 룸에 참가할 수 없습니다.");
        }
        Set<String> users = games.getOrDefault(channelId, new HashSet<>());
        users.add(sessionId);
        games.put(channelId, users);
    }

    public Map<Integer, List<Integer>> divideCards(String channelId) {
        if (games.get(channelId).size() != MAX_GAME_USERS) {
            throw new IllegalStateException("해당 게임 룸에는 카드를 분배할 수 없습니다.");
        }
        Map<Integer, List<Integer>> cardsPerUser = new HashMap<>();
        List<Integer> cards = RandomNumberGenerator.shuffle();
        for (int i = 1; i <= MAX_GAME_USERS; i++) {
            cardsPerUser.put(i, cards.subList(2 * (i - 1), 2 * i));
        }
        return cardsPerUser;
    }

    public boolean isFull(String channelId) {
        if (games.get(channelId) == null) {
            return false;
        }
        return games.get(channelId).size() == MAX_GAME_USERS;
    }

    public void removeUser(String sessionId) {
        for (String key : games.keySet()) {
            Set<String> users = games.get(key);
            users.remove(sessionId);
        }
    }

    public Integer getUserCount(String channelId) {
        return games.get(channelId).size();
    }
}
