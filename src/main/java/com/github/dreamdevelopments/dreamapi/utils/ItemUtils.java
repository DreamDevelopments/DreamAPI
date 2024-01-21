package com.github.dreamdevelopments.dreamapi.utils;

import com.github.dreamdevelopments.dreamapi.DreamAPI;
import com.github.dreamdevelopments.dreamapi.messages.Message;
import com.github.dreamdevelopments.dreamapi.messages.types.EmptyMessage;
import com.github.dreamdevelopments.dreamapi.messages.types.LegacyMessage;
import com.github.dreamdevelopments.dreamapi.messages.types.ModernMessage;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemUtils {

    /**
     * Set a Message as the name of an item
     * @param meta The ItemMeta of the item
     * @param name The message to set as the name
     */
    @SuppressWarnings("deprecation")
    public static void setItemName(@NotNull ItemMeta meta, @NotNull Message name) {
        if(name.getType().isEmpty())
            meta.setDisplayName("");
        if(DreamAPI.getServerType().isModern()) {
            meta.displayName(((ModernMessage) name).getMessage());
        }
        else
            meta.setDisplayName(name.toString());
    }

    /**
     * Get the name of an item as a Message
     * @param meta The ItemMeta of the item
     * @return The name of the item
     */
    @NotNull @SuppressWarnings("deprecation")
    public static Message getItemName(@NotNull ItemMeta meta) {
        if(DreamAPI.getServerType().isModern()) {
            Component name = meta.displayName();
            if(name == null)
                return EmptyMessage.getEmptyMessage();
            return new ModernMessage(name);
        }
        else {
            String name = meta.getDisplayName();
            return Message.fromText(name);
        }
    }

    /**
     * Set a list of Messages as the lore of an item
     * @param meta The ItemMeta of the item
     * @param lore The lore to set
     */
    public static void setItemLore(@NotNull ItemMeta meta, @NotNull List<Message> lore) {
        if(DreamAPI.getServerType().isModern())
            setModernItemLore(meta, lore);
        else
            setLegacyItemLore(meta, lore);
    }

    @SuppressWarnings("deprecation")
    private static void setLegacyItemLore(@NotNull ItemMeta meta, @NotNull List<Message> lore) {
        List<String> newLore = new ArrayList<>();
        for(Message line : lore)
            newLore.add(line.toString());
        meta.setLore(newLore);
    }

    private static void setModernItemLore(@NotNull ItemMeta meta, @NotNull List<Message> lore) {
        List<Component> newLore = new ArrayList<>();
        for(Message line : lore) {
            newLore.add(line.getType().isEmpty() ? Component.empty() : ((ModernMessage) line).getMessage());
        }
        meta.lore(newLore);
    }

    /**
     * Get the lore of an item as a list of Messages
     * @param meta The ItemMeta of the item
     * @return The lore of the item
     */
    @NotNull
    public static List<Message> getItemLore(@NotNull ItemMeta meta) {
        if(DreamAPI.getServerType().isModern())
            return getModernItemLore(meta);
        return getLegacyItemLore(meta);
    }

    @NotNull @SuppressWarnings("deprecation")
    private static List<Message> getLegacyItemLore(@NotNull ItemMeta meta) {
        List<Message> newLore = new ArrayList<>();
        List<String> lore = meta.getLore();
        if(lore == null)
            return newLore;
        for(String line : lore)
            newLore.add(new LegacyMessage(line, false));
        return newLore;
    }

    @NotNull
    private static List<Message> getModernItemLore(@NotNull ItemMeta meta) {
        List<Message> newLore = new ArrayList<>();
        List<Component> lore = meta.lore();
        if(lore == null)
            return newLore;
        for(Component line : lore)
            newLore.add(new ModernMessage(line));
        return newLore;
    }

    public static ItemStack replacePlaceholders(ItemStack itemStack, HashMap<String, String> placeholders) {
        ItemStack newItem = itemStack.clone();
        ItemMeta meta = newItem.getItemMeta();
        if (meta.hasDisplayName()) {
            String name = meta.getDisplayName();
            for (Map.Entry<String, String> placeholder : placeholders.entrySet()) {
                name = name.replace(placeholder.getKey(), placeholder.getValue());
            }
            meta.setDisplayName(name);
        }
        if (meta.hasLore()) {
            List<String> lore = meta.getLore();
            for (int i = 0; i < lore.size(); i++) {
                String line = lore.get(i);
                for (Map.Entry<String, String> placeholder : placeholders.entrySet()) {
                    line = line.replace(placeholder.getKey(), placeholder.getValue());
                }
                lore.set(i, line);
            }
            meta.setLore(lore);
        }
        return newItem;
    }

}
