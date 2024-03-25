package com.github.dreamdevelopments.dreamapi.handlers;

import com.github.dreamdevelopments.dreamapi.DreamAPI;
import me.clip.placeholderapi.PlaceholderAPI;
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
        if(DreamAPI.getInstance().isPlaceholderAPIEnabled())
            return PlaceholderAPI.setPlaceholders(player, message);
        return message;
    }

    /**
     * Checks if a string has PlaceholderAPI placeholders
     * @param message The string to check
     * @return Whether the message contains placeholders or not
     */
    public static boolean hasPlaceholders(String message) {
        if(!DreamAPI.getInstance().isPlaceholderAPIEnabled())
            return false;
        return PlaceholderAPI.containsPlaceholders(message);
    }

    /**
     * Replaces PlaceholderAPI placeholders in an ItemStack
     * @param itemStack The ItemStack to replace the placeholders in
     * @param player The player for which placeholders are considered
     * @return A clone of the itemStack with placeholders replaced
     */
    public static ItemStack replacePlaceholders(ItemStack itemStack, Player player) {
        if(!DreamAPI.getInstance().isPlaceholderAPIEnabled())
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
