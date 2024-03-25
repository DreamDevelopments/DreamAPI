package com.github.dreamdevelopments.dreamapi.configuration.parsers;

import com.github.dreamdevelopments.dreamapi.configuration.Config;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

@Getter
public abstract class Parser<T> {

    private static final HashMap<Class<?>, Parser<?>> parsers = new HashMap<>();

    public static Parser<?> getParser(Class<?> clazz) {
        return parsers.get(clazz);
    }

    public static boolean exists(Class<?> clazz) {
        return parsers.containsKey(clazz);
    }

    private final Class<T> classType;

    public Parser(@NotNull Class<T> classType) {
        parsers.put(classType, this);
        this.classType = classType;
    }

    public abstract T loadFromConfig(@NotNull Config config, @NotNull String path);

    public abstract void saveToConfig(@NotNull Config config, @NotNull String path, @NotNull T value);

    public void saveObjectToConfig(@NotNull Config config, @NotNull String path, @NotNull Object value) {
        saveToConfig(config, path, classType.cast(value));
    }

}
