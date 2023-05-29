package com.example.demo.message;

import java.util.List;
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
public class CardsMessage implements Data {

    List<CardMessage> cards;

    @Override
    public String toString() {
        return "CardsMessage{" +
                "cards=" + cards +
                '}';
    }
}
