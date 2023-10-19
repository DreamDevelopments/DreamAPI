package com.github.dreamdevelopments.dreamapi.messages.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for converting common legacy color codes to MiniMessage and vice versa.
 * It is used to allow the usage of both formats to some extent on all platforms.
 */
public class TextConverter {

    private final static HashMap<String, String> legacyToModern;

    static {
        legacyToModern = new HashMap<>();
        legacyToModern.put("&0", "<black>");
        legacyToModern.put("&1", "<dark_blue>");
        legacyToModern.put("&2", "<dark_green>");
        legacyToModern.put("&3", "<dark_aqua>");
        legacyToModern.put("&4", "<dark_red>");
        legacyToModern.put("&5", "<dark_purple>");
        legacyToModern.put("&6", "<gold>");
        legacyToModern.put("&7", "<gray>");
        legacyToModern.put("&8", "<dark_gray>");
        legacyToModern.put("&9", "<blue>");
        legacyToModern.put("&a", "<green>");
        legacyToModern.put("&b", "<aqua>");
        legacyToModern.put("&c", "<red>");
        legacyToModern.put("&d", "<light_purple>");
        legacyToModern.put("&e", "<yellow>");
        legacyToModern.put("&f", "<white>");
        legacyToModern.put("&k", "<obfuscated>");
        legacyToModern.put("&l", "<bold>");
        legacyToModern.put("&m", "<strikethrough>");
        legacyToModern.put("&n", "<underline>");
        legacyToModern.put("&o", "<italic>");
        legacyToModern.put("&r", "<reset>");
    }

    /**
     * Replaces all legacy color codes and hex codes outside tags with MiniMessage equivalents
     * @param message The string to replace the color codes in
     * @return The string with the color codes replaced
     */
    public static String legacyToModern(String message) {
        for(Map.Entry<String, String> pair : legacyToModern.entrySet()) {
            message = message.replace(pair.getKey(), pair.getValue());
        }
        return message;
    }

    /**
     * Replaces basic MiniMessage tags with legacy color codes
     * @param message The string to replace the color codes in
     * @return The string with the color codes replaced
     */
    public static String modernToLegacy(String message) {
        for(Map.Entry<String, String> pair : legacyToModern.entrySet()) {
            message = message.replace(pair.getValue(), pair.getKey());
        }
        return message;
    }

}
