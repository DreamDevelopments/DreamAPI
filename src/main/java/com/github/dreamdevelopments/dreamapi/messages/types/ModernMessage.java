package com.github.dreamdevelopments.dreamapi.messages.types;

import com.github.dreamdevelopments.dreamapi.messages.Message;
import com.github.dreamdevelopments.dreamapi.messages.utils.TextConverter;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

/**
 * Message object that uses the Adventure API used in Paper
 */
public class ModernMessage implements Message {

    static final MiniMessage minimessage = MiniMessage.miniMessage();

    @Getter
    private final Component message;

    private final int hashCode;

    /**
     * Creates a new message object that uses the Adventure API used in Paper
     * @param message The raw message
     */
    public ModernMessage(@NotNull String message) {
        this.message = minimessage.deserialize(TextConverter.legacyToModern(message));
        this.hashCode = message.hashCode();
    }

    @Override
    public void sendMessage(@NotNull Player player) {
        player.sendMessage(this.message);
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
        return this.message.toString();
    }

    @Override
    public int hashCode() {
        return this.hashCode;
    }
}
