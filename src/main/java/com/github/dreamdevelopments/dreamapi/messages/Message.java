package com.github.dreamdevelopments.dreamapi.messages;

import com.github.dreamdevelopments.dreamapi.DreamAPI;
import com.github.dreamdevelopments.dreamapi.messages.types.*;
import com.github.dreamdevelopments.dreamapi.handlers.PAPIHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Interface used for storing and sending messages to players using different APIs
 */
public interface Message {

    /**
     * Create a new message based on the server type
     * @param message The raw message as a String
     * @return The message object
     */
    static Message fromText(@Nullable String message) {
        return fromText(message, true);
    }

    static Message fromText(@Nullable String message, boolean hasPlaceholders) {
        if(message == null || message.isEmpty())
            return EmptyMessage.getEmptyMessage();

        return switch (DreamAPI.getServerType()) {
            case SPIGOT -> new LegacyMessage(message, PAPIHandler.hasPlaceholders(message) && hasPlaceholders);
            case PAPER -> new ModernMessage(message, PAPIHandler.hasPlaceholders(message) && hasPlaceholders);
        };
    }

    /**
     * Get the type of this message
     * @return The message type
     */
    MessageType getType();

    /**
     * Sends the message to a player
     * @param player The player that receives the message
     */
    void sendMessage(@NotNull Player player);

    /**
     * Creates a custom player inventory with this message as the title
     * @param owner Inventory holder
     * @param size Size of the inventory
     * @return The inventory
     */
    Inventory createInventory(InventoryHolder owner, int size);

    /**
     * Creates a custom inventory with this message as the title
     * @param owner Inventory holder
     * @param type The inventory type
     * @return The inventory
     */
    Inventory createInventory(InventoryHolder owner, InventoryType type);

    /**
     * Replace all occurrences of a string with another string
     * @param oldText The string to replace
     * @param newText The string to replace with
     * @return A copy of the message with the string replaced
     */
    Message replaceText(String oldText, String newText);

    /**
     * Clone this message
     * @return Returns an exact copy of this message
     */
    Message clone();

    /**
     * Get the raw value of the message
     * @return Message as a string
     */
    @Override
    String toString();

    @Override
    int hashCode();

}
