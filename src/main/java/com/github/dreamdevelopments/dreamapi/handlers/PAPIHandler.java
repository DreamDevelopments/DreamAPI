package com.github.dreamdevelopments.dreamapi.handlers;

import com.github.dreamdevelopments.dreamapi.DreamAPI;
import lombok.Getter;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Utility class for managing PlaceholderAPI placeholders
 */
public class PAPIHandler {

    /**
     * Replaces PlaceholderAPI placeholders in a string
     * @param message The string to replace the placeholders in
     * @param player The player for which placeholders are considered
     * @return The string with placeholders replaced
     */
    public static String replacePlaceholders(String message, Player player) {
        if(DreamAPI.isPlaceholderAPIEnabled())
            return PlaceholderAPI.setPlaceholders(player, message);
        return message;
    }

    /**
     * Checks if a string has PlaceholderAPI placeholders
     * @param message The string to check
     * @return Whether the message contains placeholders or not
     */
    public static boolean hasPlaceholders(String message) {
        if(!DreamAPI.isPlaceholderAPIEnabled())
            return false;
        return PlaceholderAPI.containsPlaceholders(message);
    }

    public static ItemStack replacePlaceholders(ItemStack itemStack, Player player) {
        if(!DreamAPI.isPlaceholderAPIEnabled())
            return itemStack;
        ItemStack newItem = itemStack.clone();
        ItemMeta meta = newItem.getItemMeta();
        if(meta.hasDisplayName())
            meta.setDisplayName(PlaceholderAPI.setPlaceholders(player, meta.getDisplayName()));
        if(meta.hasLore())
            meta.setLore(PlaceholderAPI.setPlaceholders(player, meta.getLore()));
        //TODO: Add placeholders for skull owner
        newItem.setItemMeta(meta);
        return newItem;
    }

}
