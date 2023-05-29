package com.example.demo.message;

import java.util.ArrayList;
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
public class CardMessage {

    private String user;
    private List<Integer> jobCards;
    private List<Integer> facilityCards;

    @Override
    public String toString() {
        return "CardMessage{" +
                "user='" + user + '\'' +
                ", jobCards=" + jobCards +
                ", facilityCards=" + facilityCards +
                '}';
    }
}
