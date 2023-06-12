package com.example.demo.ui;

import com.example.demo.application.GameService;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Slf4j
@AllArgsConstructor
@Component
public class WebSocketInterceptor implements ChannelInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketInterceptor.class);

    private final GameService gameService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        StompCommand command = accessor.getCommand();
        String sessionId = accessor.getSessionId();

        if (Objects.requireNonNull(command).equals(StompCommand.SUBSCRIBE)) {
            String destination = accessor.getDestination();
            String[] split = destination.split("/");
            String channelId = split[split.length - 1];
            gameService.createOrJoinRoom(channelId, sessionId);
        }

        if (Objects.requireNonNull(command).equals(StompCommand.DISCONNECT)) {
            gameService.removeUser(sessionId);
        }

        return message;
    }
}
