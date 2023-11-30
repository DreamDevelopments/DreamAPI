package com.github.dreamdevelopments.dreamapi.messages.types;

import com.github.dreamdevelopments.dreamapi.handlers.PAPIHandler;
import com.github.dreamdevelopments.dreamapi.messages.Message;
import com.github.dreamdevelopments.dreamapi.messages.MessageType;
import com.github.dreamdevelopments.dreamapi.messages.utils.LegacyText;
import com.github.dreamdevelopments.dreamapi.messages.utils.TextConverter;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

/**
 * Message object that uses the Bukkit API
 */
@Getter
public class LegacyMessage implements Message {

    private final String message;
    private final boolean hasPlaceholders;

    /**
     * Creates a new message object that uses the Bukkit API
     * @param message The raw message
     */
    public LegacyMessage(@NotNull String message) {
        this(message, false);
    }

    /**
     * Creates a new message object that uses the Bukkit API
     * @param message The raw message
     * @param hasPlaceholders Whether the message contains placeholders
     */
    public LegacyMessage(@NotNull String message, boolean hasPlaceholders) {
        this.message = LegacyText.replaceAllColorCodes(TextConverter.modernToLegacy(message));
        this.hasPlaceholders = hasPlaceholders;
    }

    @Override
    public MessageType getType() {
        return MessageType.LEGACY;
    }

    @Override
    public void sendMessage(@NotNull Player player) {
        player.sendMessage(this.getModifiedMessage(player));
    }

    @Override
    public Inventory createInventory(InventoryHolder owner, int size) {
        return Bukkit.createInventory(owner, size, this.getModifiedMessage((Player)owner));
    }

    @Override
    public Inventory createInventory(InventoryHolder owner, InventoryType type) {
        return Bukkit.createInventory(owner, type, this.getModifiedMessage((Player)owner));
    }

    private String getModifiedMessage(Player player) {
        if(hasPlaceholders)
            return PAPIHandler.replacePlaceholders(this.message, player);
        return this.message;
    }

    @Override
    public String toString() {
        return this.message;
    }

    @Override
    public int hashCode() {
        return this.message.hashCode();
    }

}
