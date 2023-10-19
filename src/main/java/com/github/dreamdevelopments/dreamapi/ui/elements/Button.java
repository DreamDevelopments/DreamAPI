package com.github.dreamdevelopments.dreamapi.ui.elements;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.function.Consumer;

public class Button {

    @Getter
    private final GuiItem item;

    private final Consumer<InventoryClickEvent> onClick;

    public Button(GuiItem item, Consumer<InventoryClickEvent> onClick) {
        this.item = item;
        this.onClick = onClick;
    }

    public void update(Inventory inventory, Player player) {
        item.addToInventory(inventory, player);
    }

    public void click(InventoryClickEvent event) {
        this.onClick.accept(event);
    }

    public boolean isClicked(int slot) {
        return this.item.isClicked(slot);
    }


}
