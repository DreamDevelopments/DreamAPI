package com.github.dreamdevelopments.dreamapi.ui;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class GuiListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Gui gui = GuiManager.getInstance().getOpenInventories().get((Player)event.getWhoClicked());
        if(gui == null)
            return;
        gui.click(event);
    }

    @EventHandler
    public void onInventoryClick(InventoryCloseEvent event) {
        Gui gui = GuiManager.getInstance().getOpenInventories().get((Player)event.getPlayer());
        if(gui == null)
            return;
        gui.close(event);
    }

}
