package com.github.dreamdevelopments.dreamapi.messages.types;

import com.github.dreamdevelopments.dreamapi.handlers.PAPIHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public class PlaceholderModernMessage extends ModernMessage {


    /**
     * Creates a new message object that uses the Adventure API used in Paper
     *
     * @param message The raw message
     */
    public PlaceholderModernMessage(@NotNull String message) {
        super(message);
    }

    @Override
    public void sendMessage(@NotNull Player player) {
        player.sendMessage(ModernMessage.minimessage.deserialize(PAPIHandler.replacePlaceholders(this.getRawMessage(), player)));
    }

    @Override
    public Inventory createInventory(InventoryHolder owner, int size) {
        return Bukkit.createInventory(owner, size, ModernMessage.minimessage.deserialize(PAPIHandler.replacePlaceholders(this.getRawMessage(), (Player)owner)));
    }

    @Override
    public Inventory createInventory(InventoryHolder owner, InventoryType type) {
        return Bukkit.createInventory(owner, type, ModernMessage.minimessage.deserialize(PAPIHandler.replacePlaceholders(this.getRawMessage(), (Player)owner)));
    }

}