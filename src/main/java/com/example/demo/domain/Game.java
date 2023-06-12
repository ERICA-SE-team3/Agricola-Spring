package com.example.demo.domain;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Game {

    private static final int MAX_GAME_USERS = 4;

    private final Set<String> players = new HashSet<>();

    public void join(String sessionId) {
        if (isFull()) {
            throw new IllegalStateException("해당 게임 룸에 참가할 수 없습니다.");
        }
        players.add(sessionId);
    }

    public void quit(String sessionId) {
        players.remove(sessionId);
    }

    public Map<Integer, List<Integer>> divideCards() {
        if (getPlayerCount() != MAX_GAME_USERS) {
            throw new IllegalStateException("해당 게임 룸에는 카드를 분배할 수 없습니다.");
        }
        Map<Integer, List<Integer>> cardsPerUser = new HashMap<>();
        List<Integer> cards = RandomNumberGenerator.shuffle();
        for (int i = 1; i <= MAX_GAME_USERS; i++) {
            cardsPerUser.put(i, cards.subList(2 * (i - 1), 2 * i));
        }
        return cardsPerUser;
    }

    public boolean isFull() {
        return players.size() == MAX_GAME_USERS;
    }

    public int getPlayerCount() {
        return players.size();
    }
}
