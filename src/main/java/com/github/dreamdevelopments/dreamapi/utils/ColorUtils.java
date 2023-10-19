package com.github.dreamdevelopments.dreamapi.utils;

import org.bukkit.Color;
import org.jetbrains.annotations.NotNull;

public class ColorUtils {

    @NotNull
    public static Color colorFromHex(@NotNull String hex) {
        return Color.fromRGB(
                Integer.parseInt(hex.substring(1, 3), 16),
                Integer.parseInt(hex.substring(3, 5), 16),
                Integer.parseInt(hex.substring(5, 7), 16)
        );
    }

}
