package com.github.dreamdevelopments.dreamapi;

import com.github.dreamdevelopments.dreamapi.configuration.parsers.ItemStackParser;
import com.github.dreamdevelopments.dreamapi.configuration.parsers.MessageParser;
import com.github.dreamdevelopments.dreamapi.ui.GuiManager;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public final class DreamAPI {

    @Getter
    static ServerType serverType;

    public void initialize(JavaPlugin plugin) {
        MessageParser.getInstance();
        ItemStackParser.getInstance();

        this.initializeGui(plugin);
    }

    public void initializeGui(JavaPlugin plugin) {
        GuiManager.initialize(plugin);
    }

}
