package com.github.dreamdevelopments.dreamapi.utils;

import org.bukkit.Color;
import org.jetbrains.annotations.NotNull;

/**
 * A utility class for handling hex colors.
 */
public class ColorUtils {

    /**
     * Converts a hex string to a color
     * @param hex The hex string (#RRGGBB)
     * @return The color
     */
    @NotNull
    public static Color colorFromHex(@NotNull String hex) {
        return Color.fromRGB(
                Integer.parseInt(hex.substring(1, 3), 16),
                Integer.parseInt(hex.substring(3, 5), 16),
                Integer.parseInt(hex.substring(5, 7), 16)
        );
    }

}
