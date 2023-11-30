package com.github.dreamdevelopments.dreamapi.messages.types;

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

    /**
     * Creates a new message object that uses the Bukkit API
     * @param message The raw message
     */
    public LegacyMessage(@NotNull String message) {
        this.message = LegacyText.replaceAllColorCodes(TextConverter.modernToLegacy(message));
    }

    @Override
    public MessageType getType() {
        return MessageType.LEGACY;
    }

    @Override
    public void sendMessage(@NotNull Player player) {
        player.sendMessage(message);
    }

    @Override
    public Inventory createInventory(InventoryHolder owner, int size) {
        return Bukkit.createInventory(owner, size, this.message);
    }

    @Override
    public Inventory createInventory(InventoryHolder owner, InventoryType type) {
        return Bukkit.createInventory(owner, type, this.message);
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
