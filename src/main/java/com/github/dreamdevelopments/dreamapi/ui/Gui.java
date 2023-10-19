package com.github.dreamdevelopments.dreamapi.ui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.jetbrains.annotations.NotNull;

public abstract class Gui {

    protected Player player;
    private GuiType guiType;

    public abstract boolean onOpen(@NotNull Player player);
    public abstract void onClick(@NotNull InventoryClickEvent event);

    public abstract boolean onClose(@NotNull InventoryCloseEvent event);

    protected void registerInventory(@NotNull Player player) {
        GuiManager.getInstance().getOpenInventories().put(player, this);
    }

    protected void unregisterInventory(@NotNull Player player) {
        GuiManager.getInstance().getOpenInventories().remove(player, this);
    }

    public void open(@NotNull Player player) {
        if(this.onOpen(player))
            this.registerInventory(player);
    }

    public void close(@NotNull InventoryCloseEvent event) {
        if(this.onClose(event))
            this.unregisterInventory(this.player);
    }

    public void click(@NotNull InventoryClickEvent event) {
        event.setCancelled(true);
        this.onClick(event);

    }

}