package com.example.demo.application;

import com.example.demo.dao.GameDao;
import com.example.demo.domain.Game;
import com.example.demo.dto.ActionMessageResponse;
import com.example.demo.dto.CardResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GameService {

    private final ObjectMapper objectMapper;
    private final GameDao gameDAO;

    public void createOrJoinRoom(String channelId, String sessionId) {
        gameDAO.save(channelId, sessionId);
    }

    public Map<Integer, List<Integer>> divideCards(String channelId) {
        Game game = gameDAO.findByChannelId(channelId);
        return game.divideCards();
    }

    public ActionMessageResponse getUserCountResponse(String channelId) {
        try {
            Map<String, Integer> map = new HashMap<>();
            int userCount = gameDAO.countByChannelId(channelId);
            map.put("userCount", userCount);
            String data = objectMapper.writeValueAsString(map);
            ActionMessageResponse response = new ActionMessageResponse(
                    "userCountCheck",
                    "server",
                    channelId,
                    data
            );
            return response;
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Json 변환 중 예외가 발생했습니다.");
        }
    }

    public ActionMessageResponse getUserCardsResponse(String channelId) {
        try {
            Game game = gameDAO.findByChannelId(channelId);
            if (!game.isFull()) {
                throw new IllegalStateException("게임 플레이어 수가 부족합니다.");
            }
            List<CardResponse> cardResponses = new ArrayList<>();
            Map<Integer, List<Integer>> jobCardsPerUser = divideCards(channelId);
            Map<Integer, List<Integer>> facilityCardsPerUser = divideCards(channelId);
            for (Integer userNumber : jobCardsPerUser.keySet()) {
                CardResponse cardResponse = new CardResponse(String.valueOf(userNumber),
                        jobCardsPerUser.get(userNumber),
                        facilityCardsPerUser.get(userNumber));
                cardResponses.add(cardResponse);
            }

            Map<String, List<CardResponse>> map = new HashMap<>();
            map.put("cards", cardResponses);
            String data = objectMapper.writeValueAsString(map);

            ActionMessageResponse userCardMessage = new ActionMessageResponse(
                    "cardDeck",
                    "server",
                    channelId,
                    data
            );
            return userCardMessage;
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Json 변환 중 예외가 발생했습니다.");
        }
    }

    public void removeUser(String sessionId) {
        List<Game> games = gameDAO.findAll();
        for (Game game : games) {
            game.quit(sessionId);
        }
    }
}
