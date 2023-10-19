package com.github.dreamdevelopments.dreamapi.ui;

import com.github.dreamdevelopments.dreamapi.configuration.parsers.CustomSoundParser;
import com.github.dreamdevelopments.dreamapi.configuration.parsers.GuiItemParser;
import com.github.dreamdevelopments.dreamapi.configuration.parsers.GuiTypeParser;
import com.github.dreamdevelopments.dreamapi.configuration.parsers.ItemStackParser;
import com.github.dreamdevelopments.dreamapi.effects.CustomSound;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public final class GuiManager {

    @Getter
    private static GuiManager instance;

    public static void initialize(JavaPlugin plugin) {
        if(instance != null)
            return;
        instance = new GuiManager();
        Bukkit.getPluginManager().registerEvents(new GuiListener(), plugin);

        CustomSoundParser.getInstance();
        GuiItemParser.getInstance();
        GuiTypeParser.getInstance();
    }

    @Getter
    private final HashMap<Player, Gui> openInventories;

    private GuiManager() {
        this.openInventories = new HashMap<>();
    }

}