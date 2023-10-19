package com.github.dreamdevelopments.dreamapi.configuration.parsers;

import com.github.dreamdevelopments.dreamapi.configuration.Config;
import com.github.dreamdevelopments.dreamapi.effects.CustomSound;
import com.github.dreamdevelopments.dreamapi.ui.elements.CustomInventory;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class CustomInvParser extends Parser<CustomInventory> {

    @Getter
    private static final CustomInvParser instance = new CustomInvParser();

    private final CustomSoundParser customSoundParser = CustomSoundParser.getInstance();
    private final ItemStackParser itemStackParser = ItemStackParser.getInstance();

    private CustomInvParser() {
        super(CustomInventory.class);
    }

    @Override
    public CustomInventory loadFromConfig(@NotNull Config config, @NotNull String path) {
        int size = config.getInt(path + ".size");
        //TODO: Add colors and placeholders to title and items
        String title = config.getString(path + ".title");
        ItemStack[] items  = new ItemStack[size];
        String itemPath;
        if(config.contains(path + ".Items")) {
            for (String itemName : config.getConfigurationSection(path + ".Items").getKeys(false)) {
                itemPath = path + ".Items." + itemName;
                int[] slots = config.getSlotList(itemPath + ".slot");
                ItemStack item = this.itemStackParser.loadFromConfig(config, itemPath);
                for (int i : slots) {
                    items[i] = item;
                }
            }
        }
        CustomSound openSound = customSoundParser.loadFromConfig(config, path + ".openSound");
        CustomSound clickSound = customSoundParser.loadFromConfig(config, path + ".clickSound");
        return new CustomInventory(size, title, items, openSound, clickSound);
    }
}
