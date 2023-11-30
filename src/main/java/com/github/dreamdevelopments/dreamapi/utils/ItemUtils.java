package com.github.dreamdevelopments.dreamapi.utils;

import com.github.dreamdevelopments.dreamapi.DreamAPI;
import com.github.dreamdevelopments.dreamapi.messages.Message;
import com.github.dreamdevelopments.dreamapi.messages.types.LegacyMessage;
import com.github.dreamdevelopments.dreamapi.messages.types.ModernMessage;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ItemUtils {

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

}
