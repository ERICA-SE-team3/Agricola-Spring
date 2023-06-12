package com.example.demo.ui;

import com.example.demo.application.GameService;
import com.example.demo.dto.ActionMessageResponse;
import com.example.demo.dto.MessageRequest;
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
    private final GameService gameService;

    @MessageMapping("/play")
    public void sendActionMessage(MessageRequest message) {
        logger.info("###pub start###");
        logger.info(message.toString());
        simpMessageSendingOperations.convertAndSend("/sub/channel/" + message.getChannelId(), message);
        logger.info("###pub finish###");
    }

    @MessageMapping("/hello")
    public void sendReadyMessage(MessageRequest message) {
        logger.info("###hello start###");
        logger.info(message.toString());
        sendUserCountMessage(message);
        sendCardMessage(message);
        logger.info("###hello finish###");
    }

    private void sendUserCountMessage(MessageRequest message) {
        ActionMessageResponse response = gameService.getUserCountResponse(message.getChannelId());
        simpMessageSendingOperations.convertAndSend("/sub/channel/" + message.getChannelId(), response);
    }

    private void sendCardMessage(MessageRequest message) {
        ActionMessageResponse response = gameService.getUserCardsResponse(message.getChannelId());
        simpMessageSendingOperations.convertAndSend("/sub/channel/" + message.getChannelId(), response);
    }
}
