package com.example.demo.dao;

import com.example.demo.domain.Game;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class GameDAO {

    private final Hashtable<String, Game> games = new Hashtable<>();

    public void save(String channelId, String sessionId) {
        Game game = games.getOrDefault(channelId, new Game());
        game.join(sessionId);
        games.put(channelId, game);
    }

    public Game findByChannelId(String channelId) {
        return games.getOrDefault(channelId, new Game());
    }

    public List<Game> findAll() {
        return new ArrayList<>(games.values());
    }

    public int countByChannelId(String channelId) {
        return games.getOrDefault(channelId, new Game()).getPlayerCount();
    }
}
