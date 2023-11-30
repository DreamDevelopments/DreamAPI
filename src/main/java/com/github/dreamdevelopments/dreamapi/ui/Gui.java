package com.github.dreamdevelopments.dreamapi.ui;

import com.github.dreamdevelopments.dreamapi.ui.elements.Button;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

@Getter
public abstract class Gui {

    private Inventory inventory;
    private final GuiType guiType;

    public Gui(GuiType guiType) {
        this.guiType = guiType;
    }

    public abstract boolean canOpen(@NotNull Player player);

    public abstract boolean onOpen(@NotNull Player player);
    public abstract boolean onClick(@NotNull InventoryClickEvent event);

    public abstract boolean onClose(@NotNull InventoryCloseEvent event);

    protected void registerInventory(@NotNull Player player) {
        GuiManager.getInstance().getOpenInventories().put(player, this);
    }

    protected void unregisterInventory(@NotNull Player player) {
        GuiManager.getInstance().getOpenInventories().remove(player, this);
    }

    public void open(@NotNull Player player) {
        if(this.canOpen(player)) {
            this.inventory = this.guiType.createInventory(player);
            if(!this.onOpen(player))
                return;
            player.openInventory(inventory);
            this.registerInventory(player);
        }
    }

    public void close(@NotNull InventoryCloseEvent event) {
        if(this.onClose(event))
            this.unregisterInventory(this.getPlayer());
    }

    public void click(@NotNull InventoryClickEvent event) {
        for(Button button : this.getGuiType().getButtons()) {
            if(button.isClicked(event.getSlot())) {
                button.click(event, this);
                break;
            }
        }

        if(!this.onClick(event)) {
            if(event.getClickedInventory() != null)
                if(event.getClickedInventory().equals(this.inventory) || event.isShiftClick())
                    event.setCancelled(true);
        }
    }

    public void update() {
        this.guiType.update(this.getInventory(), this.getPlayer());
    }

    public Player getPlayer() {
        return (Player)this.inventory.getHolder();
    }

}