package com.example.demo.application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RandomNumberGenerator {

    private static final List<Integer> numbers = IntStream.rangeClosed(1,8)
            .boxed()
            .collect(Collectors.toList());

    public static List<Integer> shuffle() {
        Collections.shuffle(numbers);
        return new ArrayList<>(numbers);
    }
}
