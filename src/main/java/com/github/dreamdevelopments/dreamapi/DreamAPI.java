package com.github.dreamdevelopments.dreamapi;

import com.github.dreamdevelopments.dreamapi.configuration.parsers.ItemStackParser;
import com.github.dreamdevelopments.dreamapi.configuration.parsers.MessageParser;
import com.github.dreamdevelopments.dreamapi.ui.GuiManager;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public final class DreamAPI {

    @Getter
    static ServerType serverType;

    public static void initialize(JavaPlugin plugin) {
        initializeGui(plugin);
    }

    public static void initializeGui(JavaPlugin plugin) {
        GuiManager.initialize(plugin);
    }

}
