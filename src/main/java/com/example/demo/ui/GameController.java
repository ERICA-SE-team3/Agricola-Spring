package com.example.demo.ui;

import com.example.demo.application.Game;
import com.example.demo.message.ActionMessageRequest;
import com.example.demo.message.ActionMessageResponse;
import com.example.demo.message.CardMessage;
import com.example.demo.message.ReadyMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class GameController {

    private static final Logger logger = LoggerFactory.getLogger(GameController.class);

    private final SimpMessageSendingOperations simpMessageSendingOperations;
    private final ObjectMapper objectMapper;
    private final Game game;

    @MessageMapping("/play")
    public void message(ActionMessageRequest message) {
        logger.info("###pub start###");
        logger.info(message.toString());
        simpMessageSendingOperations.convertAndSend("/sub/channel/" + message.getChannelId(), message);
        logger.info("###pub finish###");
    }

    @MessageMapping("/hello")
    public void ready(ReadyMessage message) {
        try {
            sendUserCountMessage(message);
            sendCardMessage(message);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException();
        }
    }

    private void sendUserCountMessage(ReadyMessage message) throws JsonProcessingException {
        Map<String, Integer> map = new HashMap<>();
        map.put("userCount", game.getUserCount(message.getChannelId()));
        String data = objectMapper.writeValueAsString(map);
        ActionMessageResponse userCountMessage = new ActionMessageResponse(
                "userCountCheck",
                "server",
                message.getChannelId(),
                data
        );
        simpMessageSendingOperations.convertAndSend("/sub/channel/" + message.getChannelId(), userCountMessage);
    }

    private void sendCardMessage(ReadyMessage message) throws JsonProcessingException {
        if (game.isFull(message.getChannelId())) {
            List<CardMessage> cardMessages = new ArrayList<>();
            Map<Integer, List<Integer>> jobCardsPerUser = game.divideCards(message.getChannelId());
            Map<Integer, List<Integer>> facilityCardsPerUser = game.divideCards(message.getChannelId());
            for (Integer userNumber : jobCardsPerUser.keySet()) {
                CardMessage cardMessage = new CardMessage(String.valueOf(userNumber), jobCardsPerUser.get(userNumber),
                        facilityCardsPerUser.get(userNumber));
                cardMessages.add(cardMessage);
            }

            Map<String, List<CardMessage>> map = new HashMap<>();
            map.put("cards", cardMessages);
            String data = objectMapper.writeValueAsString(map);

            ActionMessageResponse userCardMessage = new ActionMessageResponse(
                    "cardDeck",
                    "server",
                    message.getChannelId(),
                    data
            );
            simpMessageSendingOperations.convertAndSend("/sub/channel/" + message.getChannelId(), userCardMessage);
        }
    }
}
