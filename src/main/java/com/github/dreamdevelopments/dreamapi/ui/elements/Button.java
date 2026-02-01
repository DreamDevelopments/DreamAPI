package com.github.dreamdevelopments.dreamapi.ui.elements;

import com.github.dreamdevelopments.dreamapi.ui.Gui;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.function.BiConsumer;

/** Represents a button in a {@link com.github.dreamdevelopments.dreamapi.ui.Gui}. This can be one or more items in the inventory. */
public class Button {

    @Getter
    private final GuiItem item;

    private final BiConsumer<InventoryClickEvent, Gui> onClick;

    /** Constructor
     *
     * @param item    The GuiItem representing the button.
     * @param onClick The action to perform when the button is clicked.
     */
    public Button(GuiItem item, BiConsumer<InventoryClickEvent, Gui> onClick) {
        this.item = item;
        this.onClick = onClick;
    }

    /**
     * Update the button in the given GUI.
     * @param gui The GUI
     */
    public void update(Gui gui) {
        item.addToInventory(gui);
    }

    /**
     * Deprecated method to update the button in a specific inventory for a player. Use update(Gui gui) instead.
     * @param inventory The inventory to update.
     * @param player    The player for whom the inventory belongs.
      */
    @Deprecated
    public void update(Inventory inventory, Player player) {
        item.addToInventory(inventory, player);
    }

    /** This function is called when the button is clicked.
    * It should not be called manually or overridden unless you know what you are doing.
    * To do something when the button is clicked, use the onClick parameter in the constructor
     * @param event The InventoryClickEvent that triggered the click.
     * @param gui   The GUI in which the button was clicked.
     */
    public void click(InventoryClickEvent event, Gui gui) {
        this.onClick.accept(event, gui);
    }

    /** Check if a slot corresponds to this button.
     *
     * @param slot The slot to check.
     * @return True if the slot is part of this button, false otherwise.
     */
    public boolean isClicked(int slot) {
        return this.item.isClicked(slot);
    }


}
