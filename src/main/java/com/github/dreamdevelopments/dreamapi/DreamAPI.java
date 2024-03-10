package com.github.dreamdevelopments.dreamapi;

import com.github.dreamdevelopments.dreamapi.configuration.parsers.*;
import com.github.dreamdevelopments.dreamapi.ui.GuiManager;
import com.github.dreamdevelopments.dreamapi.utils.PacketUtils;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class DreamAPI {

    @Getter
    static ServerType serverType;

    @Getter
    static boolean placeholderAPIEnabled;

    public static void initialize(JavaPlugin plugin) {
        initializeServer();
        initializeHandlers();
        initializeParsers();
        initializeGui(plugin);
        initializePackets(plugin);
    }

    public static void initializeServer() {
        for(ServerType serverType : ServerType.values()) {
            if(Bukkit.getVersion().toLowerCase().contains(serverType.getName())) {
                DreamAPI.serverType = serverType;
            }
        }
        if(DreamAPI.serverType == null)
            DreamAPI.serverType = ServerType.PAPER;
    }

    public static void initializeHandlers() {
        placeholderAPIEnabled = Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;
    }

    public static void initializeParsers() {
        new CustomSoundParser();
        new GuiItemParser();
        new GuiTypeParser();
        new ItemStackParser();
        new MessageParser();
    }

    public static void initializePackets(JavaPlugin plugin) {
        new PacketUtils(plugin);
    }

    public static void initializeGui(JavaPlugin plugin) {
        GuiManager.initialize(plugin);
    }

}
