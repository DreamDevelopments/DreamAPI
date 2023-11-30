package com.github.dreamdevelopments.dreamapi.messages.types;

import com.github.dreamdevelopments.dreamapi.handlers.PAPIHandler;
import com.github.dreamdevelopments.dreamapi.messages.Message;
import com.github.dreamdevelopments.dreamapi.messages.MessageType;
import com.github.dreamdevelopments.dreamapi.messages.utils.TextConverter;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
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
@Getter
public class ModernMessage implements Message {

    static final MiniMessage minimessage = MiniMessage.miniMessage();

    private final Component message;

    private final String rawMessage;

    private final boolean hasPlaceholders;

    /**
     * Creates a new message object that uses the Adventure API used in Paper
     * @param message The raw message
     */
    public ModernMessage(@NotNull String message) {
        this(message, false);
    }

    /**
     * Creates a new message object that uses the Adventure API used in Paper
     * @param message The raw message
     * @param hasPlaceholders Whether the message contains placeholders
     */
    public ModernMessage(@NotNull String message, boolean hasPlaceholders) {
        message = TextConverter.legacyToModern(message);
        this.message = minimessage.deserialize(message).decoration(TextDecoration.ITALIC, false);
        this.rawMessage = message;
        this.hasPlaceholders = hasPlaceholders;
    }

    /**
     * Creates a new message object that uses the Adventure API used in Paper
     * @param component the adventure api component for the message
     */
    public ModernMessage(@NotNull Component component) {
        this.message = component;
        this.rawMessage = minimessage.serialize(component);
        this.hasPlaceholders = false;
    }

    @Override
    public MessageType getType() {
        return MessageType.MODERN;
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

    private Component getModifiedMessage(Player player) {
        if(hasPlaceholders)
            return ModernMessage.minimessage.deserialize(PAPIHandler.replacePlaceholders(this.rawMessage, player));
        return this.message;
    }

    @Override
    public Message replaceText(String oldText, String newText) {
        return new ModernMessage(this.rawMessage.replace(oldText, newText), this.hasPlaceholders);

    }

    @Override
    public Message concat(String text, boolean atEnd) {
        return new ModernMessage(atEnd ? this.rawMessage + text : text + this.rawMessage, this.hasPlaceholders);
    }

    @Override
    public Message concat(Message message) {
        return new ModernMessage(this.rawMessage + ((ModernMessage)message).rawMessage, this.hasPlaceholders);
    }

    @Override
    public Message clone() {
        return new ModernMessage(this.rawMessage, this.hasPlaceholders);
    }

    @Override
    public String toString() {
        return this.rawMessage;
    }

    @Override
    public int hashCode() {
        return this.rawMessage.hashCode();
    }

    @Override
    public boolean equals(Message message) {
        return message.getType().equals(this.getType()) && ((ModernMessage)message).getMessage().equals(this.getMessage());
    }
}
