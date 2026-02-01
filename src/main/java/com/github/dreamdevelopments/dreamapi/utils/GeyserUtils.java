package com.github.dreamdevelopments.dreamapi.utils;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Utilities for GeyserMC (Bedrock version cross-play).
 */
@Getter
public class GeyserUtils {

    private static GeyserUtils instance;

    private boolean isEnabled;

    /**
     * Initialize the GeyserUtils.
     */
    public GeyserUtils() {
        instance = this;
        isEnabled = Bukkit.getPluginManager().isPluginEnabled("Geyser-Spigot");
    }

    /**
     * Check if a player is a Bedrock player.
     * @param player The player to check.
     * @return True if the player is a Bedrock player, false if it is a Java player or Geyser is not installed.
     */
    public static boolean isBedrockPlayer(@NotNull Player player) {
        if(instance.isEnabled) {
            return org.geysermc.api.Geyser.api().isBedrockPlayer(player.getUniqueId());
        }
        return false;
    }

}
