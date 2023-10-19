package com.github.dreamdevelopments.dreamapi.ui;

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
        Bukkit.createInventorY
    }

    @Getter
    private final HashMap<Player, Gui> openInventories;

    private GuiManager() {
        this.openInventories = new HashMap<>();
    }

}