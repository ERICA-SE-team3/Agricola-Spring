package com.example.demo.ui;

import com.example.demo.application.Game;
import com.example.demo.message.ActionMessageRequest;
import com.example.demo.message.ActionMessageResponse;
import com.example.demo.message.CardMessage;
import com.example.demo.message.CardsMessage;
import com.example.demo.message.ReadyMessage;
import com.example.demo.message.UserCountMessage;
import java.util.ArrayList;
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
        sendUserCountMessage(message);
        sendCCardMessage(message);
    }

    private void sendUserCountMessage(ReadyMessage message) {
        ActionMessageResponse userCountMessage = new ActionMessageResponse(
                "userCountCheck",
                "server",
                message.getChannelId(),
                new UserCountMessage(game.getUserCount(message.getChannelId()))
        );
        simpMessageSendingOperations.convertAndSend("/sub/channel/" + message.getChannelId(), userCountMessage);
    }

    private void sendCCardMessage(ReadyMessage message) {
        if (game.isFull(message.getChannelId())) {
            List<CardMessage> cardMessages = new ArrayList<>();
            Map<Integer, List<Integer>> jobCardsPerUser = game.divideCards();
            Map<Integer, List<Integer>> facilityCardsPerUser = game.divideCards();
            for (Integer userNumber : jobCardsPerUser.keySet()) {
                CardMessage cardMessage = new CardMessage(String.valueOf(userNumber), jobCardsPerUser.get(userNumber),
                        facilityCardsPerUser.get(userNumber));
                cardMessages.add(cardMessage);
            }
            ActionMessageResponse userCardMessage = new ActionMessageResponse(
                    "cardDeck",
                    "server",
                    message.getChannelId(),
                    new CardsMessage(cardMessages)
            );
            simpMessageSendingOperations.convertAndSend("/sub/channel/" + message.getChannelId(), userCardMessage);
        }
    }
}
