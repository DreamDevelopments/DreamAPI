package com.github.dreamdevelopments.dreamapi.configuration.parsers;

import com.github.dreamdevelopments.dreamapi.configuration.Config;
import com.github.dreamdevelopments.dreamapi.effects.CustomSound;
import com.github.dreamdevelopments.dreamapi.messages.Message;
import com.github.dreamdevelopments.dreamapi.ui.GuiType;
import com.github.dreamdevelopments.dreamapi.ui.elements.GuiItem;
import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class GuiTypeParser extends Parser<GuiType> {

    private static GuiTypeParser instance;

    public static GuiTypeParser getInstance() {
        if(instance == null)
            instance = new GuiTypeParser();
        return instance;
    }

    private GuiTypeParser() {
        super(GuiType.class);
    }

    @Override
    public GuiType loadFromConfig(@NotNull Config config, @NotNull String path) {
        Message title = config.getMessage(path + ".title");
        Set<GuiItem> items  = new HashSet<>();
        if(config.contains(path + ".items")) {
            String itemPath;
            for (String itemName : config.getConfigurationSection(path + ".items").getKeys(false)) {
                itemPath = path + ".items." + itemName;
                items.add(new GuiItem(config.getItemStack(itemPath), config.getSlotList(itemPath + ".slot")));
            }
        }
        CustomSound openSound = config.getCustomSound(path + ".openSound");
        CustomSound clickSound = config.getCustomSound(path + ".clickSound");

        if(config.contains(path + ".type"))
            return new GuiType(title, InventoryType.valueOf(config.getString(path + ".type")), items, new HashSet<>(), openSound, clickSound);

        return new GuiType(title, config.getInt(path + ".size"), items, new HashSet<>(), openSound, clickSound);
    }
}