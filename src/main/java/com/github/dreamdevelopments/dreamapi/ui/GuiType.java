package com.github.dreamdevelopments.dreamapi.ui;

import com.github.dreamdevelopments.dreamapi.effects.CustomSound;
import com.github.dreamdevelopments.dreamapi.messages.Message;
import com.github.dreamdevelopments.dreamapi.ui.elements.Button;
import com.github.dreamdevelopments.dreamapi.ui.elements.GuiItem;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

@Getter
public class GuiType {

    @NotNull
    private final Message title;
    @NotNull
    private final Set<Button> buttons;
    @NotNull
    private final Set<GuiItem> items;
    @Nullable
    private final InventoryType inventoryType;
    private final int size;
    @NotNull
    private final CustomSound openSound;
    @NotNull
    private final CustomSound clickSound;

    public GuiType(@NotNull Message title, int size,
                   @NotNull Set<GuiItem> items, @NotNull Set<Button> buttons,
                   @NotNull CustomSound openSound, @NotNull CustomSound clickSound) {
        this.title = title;
        this.buttons = buttons;
        this.items = items;
        this.size = size;
        this.inventoryType = null;
        this.openSound = openSound;
        this.clickSound = clickSound;
    }

    public GuiType(@NotNull Message title, @NotNull InventoryType inventoryType,
                   @NotNull Set<GuiItem> items, @NotNull Set<Button> buttons,
                   @NotNull CustomSound openSound, @NotNull CustomSound clickSound) {
        this.title = title;
        this.buttons = buttons;
        this.items = items;
        this.inventoryType = inventoryType;
        this.size = -1;
        this.openSound = openSound;
        this.clickSound = clickSound;
    }

    public Inventory createInventory(Player owner) {
        Inventory inventory = this.inventoryType == null ? this.title.createInventory(owner, this.size) : this.title.createInventory(owner, this.inventoryType);
        for(GuiItem item : this.items)
            item.addToInventory(inventory, owner);
        for(Button button : this.buttons)
            button.update(inventory, owner);
        return inventory;
    }

}
