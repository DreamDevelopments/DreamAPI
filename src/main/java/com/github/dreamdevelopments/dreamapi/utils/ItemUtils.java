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

    @NotNull
    public static List<Message> getItemLore(@NotNull ItemMeta meta) {
        if(DreamAPI.getServerType().isModern())
            return getLegacyItemLore(meta);
        return getModernItemLore(meta);
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
