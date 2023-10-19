package com.github.dreamdevelopments.dreamapi.messages.types;

import com.github.dreamdevelopments.dreamapi.handlers.PAPIHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public class PlaceholderLegacyMessage extends LegacyMessage {

    public PlaceholderLegacyMessage(@NotNull String message) {
        super(message);
    }

    @Override
    public void sendMessage(@NotNull Player player) {
        player.sendMessage(PAPIHandler.replacePlaceholders(this.getMessage(), player));
    }

    @Override
    public Inventory createInventory(InventoryHolder owner, int size) {
        return Bukkit.createInventory(owner, size, PAPIHandler.replacePlaceholders(this.getMessage(), (Player)owner));
    }

    @Override
    public Inventory createInventory(InventoryHolder owner, InventoryType type) {
        return Bukkit.createInventory(owner, type, PAPIHandler.replacePlaceholders(this.getMessage(), (Player)owner));
    }

}
