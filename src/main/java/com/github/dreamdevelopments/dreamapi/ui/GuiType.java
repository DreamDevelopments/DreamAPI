package com.github.dreamdevelopments.dreamapi.ui;

import com.github.dreamdevelopments.dreamapi.effects.CustomSound;
import com.github.dreamdevelopments.dreamapi.messages.Message;
import com.github.dreamdevelopments.dreamapi.ui.elements.Button;
import com.github.dreamdevelopments.dreamapi.ui.elements.GuiItem;
import lombok.Getter;
import org.bukkit.event.inventory.InventoryType;
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

    public void createInventory(@NotNull Gui gui) {
        gui.setInventory(
                this.inventoryType == null ?
                        this.title.createInventory(gui.getPlayer(), this.size) :
                        this.title.createInventory(gui.getPlayer(), this.inventoryType)
        );
        this.update(gui);
    }

    public void update(Gui gui) {
        for(GuiItem item : this.items)
            item.addToInventory(gui);
        for(Button button : this.buttons)
            button.update(gui);
    }

}
