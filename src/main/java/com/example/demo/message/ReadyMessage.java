package com.example.demo.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReadyMessage {

    private String channelId;
    private String sender;

    @Override
    public String toString() {
        return "ReadyMessage{" +
                "sender='" + sender + '\'' +
                '}';
    }
}
