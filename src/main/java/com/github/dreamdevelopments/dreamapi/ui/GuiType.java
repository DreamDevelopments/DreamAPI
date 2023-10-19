package com.github.dreamdevelopments.dreamapi.ui;

import com.github.dreamdevelopments.dreamapi.messages.Message;
import com.github.dreamdevelopments.dreamapi.ui.elements.Button;
import com.github.dreamdevelopments.dreamapi.ui.elements.GuiItem;
import lombok.Getter;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.Set;

@Getter
public class GuiType {

    private final Message title;
    private final Set<Button> buttons;
    private final Set<GuiItem> items;
    private final InventoryType inventoryType;
    private final int size;

    public GuiType(Message title, int size, Set<GuiItem> items, Set<Button> buttons) {
        this.title = title;
        this.buttons = buttons;
        this.items = items;
        this.size = size;
        this.inventoryType = null;
    }

    public GuiType(Message title, InventoryType inventoryType, Set<GuiItem> items, Set<Button> buttons) {
        this.title = title;
        this.buttons = buttons;
        this.items = items;
        this.inventoryType = inventoryType;
        this.size = -1;
    }

    public Inventory createInventory(InventoryHolder owner) {
        return this.inventoryType == null ? this.title.createInventory(owner, this.size) : this.title.createInventory(owner, this.inventoryType);
    }

}
