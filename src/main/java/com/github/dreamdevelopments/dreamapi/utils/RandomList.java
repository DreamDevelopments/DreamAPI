package com.github.dreamdevelopments.dreamapi.utils;

import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

public class RandomList<T> {

    private static final Random random = new Random();

    private int totalWeight;
    private final NavigableMap<Integer, T> weighedElements;

    public RandomList() {
        this.weighedElements = new TreeMap<>();
    }

    public void addElement(T element, int chance) {
        if(chance <= 0)
            return;
        this.totalWeight += chance;
        this.weighedElements.put(this.totalWeight, element);
    }

    public T getRandomElement() {
        int number = random.nextInt(this.totalWeight);
        return this.weighedElements.higherEntry(number).getValue();
    }

}