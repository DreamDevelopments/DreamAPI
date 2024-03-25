package com.github.dreamdevelopments.dreamapi.utils;

import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

/**
 * A list that allows you to get a random element based on the chance of each element.
 * @param <T> The type of the elements in the list
 */
public class RandomList<T> {

    private static final Random random = new Random();

    private int totalWeight;
    private final NavigableMap<Integer, T> weighedElements;

    /**
     * Create a new random list.
     */
    public RandomList() {
        this.weighedElements = new TreeMap<>();
    }

    /**
     * Add an element to the list.
     * @param element The element to add
     * @param chance The chance of the element being selected
     */
    public void addElement(T element, int chance) {
        if(chance <= 0)
            return;
        this.totalWeight += chance;
        this.weighedElements.put(this.totalWeight, element);
    }

    /**
     * Get a random element from the list.
     * @return The random element
     */
    public T getRandomElement() {
        int number = random.nextInt(this.totalWeight);
        return this.weighedElements.higherEntry(number).getValue();
    }

}