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
public class UserCountMessage implements Data {

    public Integer userCount;

    @Override
    public String toString() {
        return "Data{" +
                "userCount=" + userCount +
                '}';
    }
}
