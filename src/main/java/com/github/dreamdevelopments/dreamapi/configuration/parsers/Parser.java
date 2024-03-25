package com.github.dreamdevelopments.dreamapi.configuration.parsers;

import com.github.dreamdevelopments.dreamapi.configuration.Config;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

/**
 * A class to parse Objects from a {@link Config}.
 * @param <T> The type of Object to parse
 */
@Getter
public abstract class Parser<T> {

    private static final HashMap<Class<?>, Parser<?>> parsers = new HashMap<>();

    /**
     * Get the {@link Parser} used for a class.
     * @param clazz The class that the Parser is for
     * @return The Parser for the class or null if it doesn't exist
     */
    @Nullable
    public static Parser<?> getParser(Class<?> clazz) {
        return parsers.get(clazz);
    }

    /**
     * Check if a {@link Parser} exists for a class.
     * @param clazz The class to check
     * @return True if there is a Parser for this class.
     */
    public static boolean exists(Class<?> clazz) {
        return parsers.containsKey(clazz);
    }

    /**
     * Displays a warning message in the console if there was an error while loading an Object from the config.
     * @param config The {@link Config} used to load the Object
     * @param path The path to the Object that failed to load
     * @param message The error message
     */
    public static void warning(@NotNull Config config, @NotNull String path, @NotNull String message) {
        config.getPlugin().getLogger().warning(
                String.format("There was an error while loading %s in %s/%s config: %s",
                        path, config.getPlugin().getName(), config.getFileName(), message)
        );
    }

    private final Class<T> classType;

    /**
     * Create a new Parser for a class.
     * @param classType The class that the Parser is for.
     */
    public Parser(@NotNull Class<T> classType) {
        parsers.put(classType, this);
        this.classType = classType;
    }

    /**
     * Get a {@link T} Object from the config using this parser.
     * @param config The {@link Config} to get the Object from
     * @param path The path to the Object to get
     * @return The requested Object
     */
    public abstract T loadFromConfig(@NotNull Config config, @NotNull String path);

    /**
     * Save a {@link T} Object to the config using this parser.
     * @param config  The {@link Config} to save the Object in
     * @param path The path to save the Object to
     * @param value The Object to save
     */
    public abstract void saveToConfig(@NotNull Config config, @NotNull String path, @NotNull T value);

    /**
     * Save an Object to the config using this parser.
     * <p>
     * The value will be cast to {@link T} so the object must be of the type that the parser is for.
     * @param config  The {@link Config} to save the Object in
     * @param path The path to save the Object to
     * @param value The Object to save
     */
    public void saveObjectToConfig(@NotNull Config config, @NotNull String path, @NotNull Object value) {
        saveToConfig(config, path, classType.cast(value));
    }

}
