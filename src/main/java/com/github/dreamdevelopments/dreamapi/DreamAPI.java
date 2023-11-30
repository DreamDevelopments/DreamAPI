package com.github.dreamdevelopments.dreamapi;

import com.github.dreamdevelopments.dreamapi.configuration.parsers.*;
import com.github.dreamdevelopments.dreamapi.ui.GuiManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class DreamAPI {

    @Getter
    static ServerType serverType;

    public static void initialize(JavaPlugin plugin) {
        initializeServer();
        initializeParsers();
        initializeGui(plugin);
    }

    public static void initializeServer() {
        for(ServerType serverType : ServerType.values()) {
            if(Bukkit.getVersion().toLowerCase().contains(serverType.getName())) {
                DreamAPI.serverType = serverType;
            }
        }
    }

    public static void initializeParsers() {
        new CustomSoundParser();
        new GuiItemParser();
        new GuiTypeParser();
        new ItemStackParser();
        new MessageParser();
    }

    public static void initializeGui(JavaPlugin plugin) {
        GuiManager.initialize(plugin);
    }

}
