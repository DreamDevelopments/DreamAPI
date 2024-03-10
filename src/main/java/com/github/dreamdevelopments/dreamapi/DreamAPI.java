package com.github.dreamdevelopments.dreamapi;

import com.github.dreamdevelopments.dreamapi.configuration.parsers.*;
import com.github.dreamdevelopments.dreamapi.ui.GuiManager;
import com.github.dreamdevelopments.dreamapi.utils.PacketUtils;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

@Getter
public final class DreamAPI {

    @Getter
    private static DreamAPI instance;

    private ServerType serverType;

    private boolean placeholderAPIEnabled;

    private JavaPlugin plugin;

    private String resourceName;

    public static DreamAPI initialize(@NotNull JavaPlugin plugin, @NotNull String resourceName) {
        return initialize(plugin, resourceName, true, true, true, true, true);
    }

    public static DreamAPI initialize(JavaPlugin plugin, String resourceName, boolean server, boolean handlers, boolean parsers, boolean gui, boolean packets) {
        if(instance == null)
            instance = new DreamAPI();
        instance.plugin = plugin;
        if(server)
            instance.initializeServer();
        if(handlers)
            instance.initializeHandlers();
        if(parsers)
            instance.initializeParsers();
        if(gui)
            instance.initializeGui(plugin);
        if(packets)
            instance.initializePackets(plugin);
        return instance;
    }
    public void initializeServer() {
        for(ServerType serverType : ServerType.values()) {
            if(Bukkit.getVersion().toLowerCase().contains(serverType.getName())) {
                instance.serverType = serverType;
            }
        }
        if(instance.serverType == null)
            instance.serverType = ServerType.PAPER;
    }

    public void initializeHandlers() {
        instance.placeholderAPIEnabled = Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;
    }

    public void initializeParsers() {
        new CustomSoundParser();
        new GuiItemParser();
        new GuiTypeParser();
        new ItemStackParser();
        new MessageParser();
    }

    public void initializePackets(JavaPlugin plugin) {
        new PacketUtils(plugin);
    }

    public void initializeGui(JavaPlugin plugin) {
        GuiManager.initialize(plugin);
    }

}
