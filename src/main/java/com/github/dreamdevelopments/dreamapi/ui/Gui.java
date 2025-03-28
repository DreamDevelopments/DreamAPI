package com.github.dreamdevelopments.dreamapi.ui;

import com.github.dreamdevelopments.dreamapi.DreamAPI;
import com.github.dreamdevelopments.dreamapi.ui.elements.Button;
import com.github.dreamdevelopments.dreamapi.utils.PacketUtils;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class Gui {

    @Setter
    protected Inventory inventory;
    @Setter
    protected String title;
    protected Player player;
    private final GuiType guiType;
    private final HashMap<String, String> placeholders;

    public Gui(GuiType guiType) {
        this.guiType = guiType;
        this.placeholders = new HashMap<>();
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
        this.player = player;
        if(this.canOpen(player)) {
            if(this.inventory == null)
                this.guiType.createInventory(this);
            if(!this.onOpen(player))
                return;
            player.openInventory(inventory);
            this.registerInventory(player);
        }
        else
            this.player = null;
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
        this.guiType.update(this);
        this.updateTitle();
    }

    public void updateTitle() {
        if(DreamAPI.getInstance().isProtocolLibEnabled()) {
            PacketUtils.updateTitlePlaceholders(this.player, this.placeholders);
        }
        else {
            if (this.title == null)
                this.title = player.getOpenInventory().getOriginalTitle();
            String newTitle = this.title;
            for (Map.Entry<String, String> entry : this.getPlaceholders().entrySet())
                newTitle = newTitle.replace(entry.getKey(), entry.getValue());
            if (this.player.getOpenInventory().getTopInventory().equals(this.inventory)) {
                this.player.getOpenInventory().setTitle(newTitle);
            }
        }
    }

}