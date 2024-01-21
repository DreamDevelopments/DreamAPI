package com.github.dreamdevelopments.dreamapi.ui.elements;

import com.github.dreamdevelopments.dreamapi.ui.Gui;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.function.BiConsumer;

public class Button {

    @Getter
    private final GuiItem item;

    private final BiConsumer<InventoryClickEvent, Gui> onClick;

    public Button(GuiItem item, BiConsumer<InventoryClickEvent, Gui> onClick) {
        this.item = item;
        this.onClick = onClick;
    }

    public void update(Gui gui) {
        item.addToInventory(gui);
    }

    @Deprecated
    public void update(Inventory inventory, Player player) {
        item.addToInventory(inventory, player);
    }

    public void click(InventoryClickEvent event, Gui gui) {
        this.onClick.accept(event, gui);
    }

    public boolean isClicked(int slot) {
        return this.item.isClicked(slot);
    }


}
