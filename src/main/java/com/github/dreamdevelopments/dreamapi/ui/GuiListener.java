package com.github.dreamdevelopments.dreamapi.ui;

import com.github.dreamdevelopments.dreamapi.DreamAPIPlugin;
import com.github.dreamdevelopments.dreamapi.utils.PacketUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

public class GuiListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Gui gui = GuiManager.getInstance().getOpenInventories().get((Player)event.getWhoClicked());
        if(gui == null)
            return;
        gui.click(event);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if(PacketUtils.getInstance().getHiddenInventoriesPlayers().containsKey(event.getPlayer().getUniqueId()))
                PacketUtils.restorePlayerInventory((Player) event.getPlayer());

        PacketUtils.getInstance().getPlayerInventories().remove(event.getPlayer().getUniqueId());

        Gui gui = GuiManager.getInstance().getOpenInventories().get((Player)event.getPlayer());
        if(gui != null)
            gui.close(event);
    }


}
