package com.github.dreamdevelopments.dreamapi.ui.elements;

import com.github.dreamdevelopments.dreamapi.handlers.PAPIHandler;
import com.github.dreamdevelopments.dreamapi.ui.Gui;
import com.github.dreamdevelopments.dreamapi.utils.ItemUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public record GuiItem(ItemStack item, int[] slots) {

    public void addToInventory(Gui gui) {
        ItemStack newItem = PAPIHandler.replacePlaceholders(
                ItemUtils.replacePlaceholders(this.item, gui.getPlaceholders()),
                gui.getPlayer()
        );
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