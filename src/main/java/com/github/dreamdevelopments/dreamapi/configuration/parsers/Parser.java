package com.github.dreamdevelopments.dreamapi.configuration.parsers;

import com.github.dreamdevelopments.dreamapi.configuration.Config;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public abstract class Parser<T> {

    private static final HashMap<Class<?>, Parser<?>> parsers = new HashMap<>();

    public static Parser<?> getParser(Class<?> clazz) {
        return parsers.get(clazz);
    }

    public static boolean exists(Class<?> clazz) {
        return parsers.containsKey(clazz);
    }

    public Parser(@NotNull Class<?> classType) {
        parsers.put(classType, this);
    }

    public abstract T loadFromConfig(@NotNull Config config, @NotNull String path);

}
