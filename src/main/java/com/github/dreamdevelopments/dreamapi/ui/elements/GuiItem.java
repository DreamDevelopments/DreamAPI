package com.github.dreamdevelopments.dreamapi.ui.elements;

import com.github.dreamdevelopments.dreamapi.handlers.PAPIHandler;
import com.github.dreamdevelopments.dreamapi.ui.Gui;
import com.github.dreamdevelopments.dreamapi.utils.ItemUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Map;

public record GuiItem(ItemStack item, int[] slots) {

    public void addToInventory(Gui gui) {
        ItemStack newItem = PAPIHandler.replacePlaceholders(
                ItemUtils.replacePlaceholders(this.item, gui.getPlaceholders()),
                gui.getPlayer()
        );
        if(newItem.getType().equals(Material.PLAYER_HEAD)) {
            SkullMeta meta = (SkullMeta) newItem.getItemMeta();
            String owner = meta.getOwningPlayer().getName();
            if(owner.contains("%")) {
                for (Map.Entry<String, String> placeholder : gui.getPlaceholders().entrySet())
                    owner = owner.replace(placeholder.getKey(), placeholder.getValue());
                meta.setOwningPlayer(gui.getPlayer().getServer().getOfflinePlayer(owner));
                newItem.setItemMeta(meta);
            }
        }
        for(int slot : this.slots)
            gui.getInventory().setItem(slot, newItem);
    }

    @Deprecated
    public void addToInventory(Inventory inventory, Player player) {
        ItemStack newItem = PAPIHandler.replacePlaceholders(this.item, player);
        for(int slot : this.slots)
            inventory.setItem(slot, newItem);
    }

    public boolean isClicked(int slot) {
        for(int itemSlot : this.slots)
            if(itemSlot == slot)
                return true;
        return false;
    }

}