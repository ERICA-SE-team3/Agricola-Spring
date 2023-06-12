package com.example.demo.application;

import com.example.demo.dao.GameDAO;
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

    private static final int MAX_GAME_USERS = 4;

    private final ObjectMapper objectMapper;
    private final GameDAO gameDAO;

    public void createOrJoinRoom(String channelId, String sessionId) {
        Game game = gameDAO.findByChannelId(channelId);
        if (game.isFull()) {
            throw new IllegalStateException("해당 게임 룸에 참가할 수 없습니다.");
        }
        gameDAO.save(channelId, sessionId);
    }

    public Map<Integer, List<Integer>> divideCards(String channelId) {
        if (getUserCount(channelId) != MAX_GAME_USERS) {
            throw new IllegalStateException("해당 게임 룸에는 카드를 분배할 수 없습니다.");
        }
        Map<Integer, List<Integer>> cardsPerUser = new HashMap<>();
        List<Integer> cards = RandomNumberGenerator.shuffle();
        for (int i = 1; i <= MAX_GAME_USERS; i++) {
            cardsPerUser.put(i, cards.subList(2 * (i - 1), 2 * i));
        }
        return cardsPerUser;
    }

    public void removeUser(String sessionId) {
        List<Game> games = gameDAO.findAll();
        for (Game game : games) {
            game.quit(sessionId);
        }
    }

    public Integer getUserCount(String channelId) {
        return gameDAO.countByChannelId(channelId);
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
                CardResponse cardResponse = new CardResponse(String.valueOf(userNumber), jobCardsPerUser.get(userNumber),
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
}
