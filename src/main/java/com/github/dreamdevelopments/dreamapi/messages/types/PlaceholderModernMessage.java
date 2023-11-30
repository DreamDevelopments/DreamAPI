package com.github.dreamdevelopments.dreamapi.messages.types;

import com.github.dreamdevelopments.dreamapi.messages.Message;
import com.github.dreamdevelopments.dreamapi.handlers.PAPIHandler;
import com.github.dreamdevelopments.dreamapi.messages.MessageType;
import com.github.dreamdevelopments.dreamapi.messages.utils.TextConverter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public class PlaceholderModernMessage implements Message {

    private final String rawMessage;

    public PlaceholderModernMessage(@NotNull String message) {
        this.rawMessage = TextConverter.legacyToModern(message);
    }

    @Override
    public MessageType getType() {
        return MessageType.MODERN;
    }

    @Override
    public void sendMessage(@NotNull Player player) {
        player.sendMessage(ModernMessage.minimessage.deserialize(PAPIHandler.replacePlaceholders(rawMessage, player)));
    }

    @Override
    public Inventory createInventory(InventoryHolder owner, int size) {
        return Bukkit.createInventory(owner, size, ModernMessage.minimessage.deserialize(PAPIHandler.replacePlaceholders(rawMessage, (Player)owner)));
    }

    @Override
    public Inventory createInventory(InventoryHolder owner, InventoryType type) {
        return Bukkit.createInventory(owner, type, ModernMessage.minimessage.deserialize(PAPIHandler.replacePlaceholders(rawMessage, (Player)owner)));
    }

    @Override
    public String toString() {
        return rawMessage;
    }

    @Override
    public int hashCode() {
        return this.rawMessage.hashCode();
    }
}