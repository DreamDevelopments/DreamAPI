package com.github.dreamdevelopments.dreamapi.messages;

import com.github.dreamdevelopments.dreamapi.DreamAPI;
import com.github.dreamdevelopments.dreamapi.messages.types.*;
import com.github.dreamdevelopments.dreamapi.handlers.PAPIHandler;
import org.bukkit.command.CommandSender;
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

    //TODO: Add new line character inside message

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
            case SPIGOT -> new LegacyMessage(message, hasPlaceholders && PAPIHandler.hasPlaceholders(message));
            case PAPER -> new ModernMessage(message, hasPlaceholders && PAPIHandler.hasPlaceholders(message));
        };
    }

    /**
     * Get the type of this message
     * @return The message type
     */
    MessageType getType();

    /**
     * Sends the message to a player
     * @param receiver The receiver of the message
     */
    void sendMessage(@NotNull CommandSender receiver);

    /**
     * Sends the message in the actionbar to a player
     * @param player The player that receives the message
     */
    void sendActionbar(@NotNull Player player);

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
     * Concatenate a string with this message
     * @param text The string to concatenate
     * @param atEnd Whether to concatenate at the end or the beginning
     * @return The concatenated message
     */
    Message concat(String text, boolean atEnd);

    /**
     * Concatenate two messages together
     * @param message The message to concatenate
     * @return The concatenated message
     */
    Message concat(Message message);

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

    /**
     * Check if this message is equal to another message
     * @param message The message to compare
     * @return Whether the messages are equal
     */
    boolean equals(Message message);

}
