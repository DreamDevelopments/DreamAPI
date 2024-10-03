package com.github.dreamdevelopments.dreamapi.utils;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@Getter
public class GeyserUtils {

    private static GeyserUtils instance;

    private boolean isEnabled;

    public GeyserUtils() {
        instance = this;
        isEnabled = Bukkit.getPluginManager().isPluginEnabled("Geyser-Spigot");
    }

    public static boolean isBedrockPlayer(Player player) {
        if(instance.isEnabled) {
            return org.geysermc.api.Geyser.api().isBedrockPlayer(player.getUniqueId());
        }
        return false;
    }

}
