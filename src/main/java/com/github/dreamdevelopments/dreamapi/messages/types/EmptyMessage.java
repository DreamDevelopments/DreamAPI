package com.github.dreamdevelopments.dreamapi.messages.types;

import com.github.dreamdevelopments.dreamapi.messages.Message;
import com.github.dreamdevelopments.dreamapi.messages.MessageType;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

/**
 * Message object that represents an empty message. Sending this message does nothing.
 */
public final class EmptyMessage implements Message {

    @Getter
    private final static EmptyMessage emptyMessage = new EmptyMessage();

    @Override
    public MessageType getType() {
        return MessageType.EMPTY;
    }

    @Override
    public void sendMessage(@NotNull Player player) {}

    @Override
    public Inventory createInventory(InventoryHolder owner, int size) {
        return Bukkit.createInventory(owner, size);
    }

    @Override
    public Inventory createInventory(InventoryHolder owner, InventoryType type) {
        return Bukkit.createInventory(owner, type);
    }

    @Override
    public Message clone() {
        return this;
    }

    @Override
    public String toString() {
        return "";
    }
}
