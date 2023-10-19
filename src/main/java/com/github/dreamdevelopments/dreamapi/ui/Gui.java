package com.github.dreamdevelopments.dreamapi.ui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public abstract class Gui {

    protected Player player;
    protected Inventory inventory;
    private final GuiType guiType;

    public Gui(GuiType guiType) {
        this.guiType = guiType;
    }

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
        if(this.onOpen(player)) {
            Inventory inventory = this.guiType.createInventory(player);
            this.inventory = inventory;
            player.openInventory(inventory);
            this.registerInventory(player);
        }
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

}