package com.example.demo.domain;

import java.util.HashSet;
import java.util.Set;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Game {

    private static final int MAX_GAME_USERS = 4;

    private final Set<String> players = new HashSet<>();

    public boolean isFull() {
        return players.size() == MAX_GAME_USERS;
    }

    public void join(String sessionId) {
        players.add(sessionId);
    }

    public int getPlayerCount() {
           return players.size();
    }

    public void quit(String sessionId) {
        players.remove(sessionId);
    }
}
