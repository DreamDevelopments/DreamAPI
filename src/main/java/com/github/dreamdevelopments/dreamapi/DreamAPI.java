package com.github.dreamdevelopments.dreamapi;

import com.github.dreamdevelopments.dreamapi.configuration.parsers.*;
import com.github.dreamdevelopments.dreamapi.ui.GuiManager;
import com.github.dreamdevelopments.dreamapi.utils.GeyserUtils;
import com.github.dreamdevelopments.dreamapi.utils.Metrics;
import com.github.dreamdevelopments.dreamapi.utils.PacketUtils;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

/**
 * The main class for the DreamAPI.
 * <p>
 * This is a singleton class, use {@link DreamAPI#getInstance()} in order to use the API.
 * <p>
 * If you are not using the API as a standalone plugin, you will have to initialize it using the {@link DreamAPI#initialize(JavaPlugin, String)} function.
 */
@Getter
public final class DreamAPI {

    private static DreamAPI instance;

    /**
     *  Get the instance of the DreamAPI.
     * @return The instance of the DreamAPI.
     */
    public static DreamAPI getInstance() {
        return instance;
    }

    private ServerType serverType;

    private boolean placeholderAPIEnabled;

    private boolean protocolLibEnabled;

    private JavaPlugin plugin;

    private String resourceName;

    /**
     * Initialize the DreamAPI.
     * <p>
     * This will enable all the features of the API.
     * If you want to customize what is enabled, use:
     * {@link DreamAPI#initialize(JavaPlugin, String, boolean, boolean, boolean, boolean, boolean)}.
     * @param plugin The plugin that uses the API.
     * @param resourceName The name of the resource that uses the API. It is used for display and metrics.
     * @return The new instance of the DreamAPI. If the API has already been initialized, it will return the existing instance.
     */
    public static DreamAPI initialize(@NotNull JavaPlugin plugin, @NotNull String resourceName) {
        return initialize(plugin, resourceName, true, true, true, true, true, true);
    }

    public static DreamAPI initialize(JavaPlugin plugin, String resourceName, boolean server, boolean handlers, boolean parsers, boolean gui, boolean packets) {
        return initialize(plugin, resourceName, server, handlers, parsers, gui, packets, false);
    }
    /**
     * Initialize the DreamAPI.
     * <p>
     * This will enable the features of the API that are set to true.
     *
     * @param plugin The plugin that uses the API.
     * @param resourceName The name of the resource that uses the API. It is used for display and metrics.
     * @param server Whether to initialize the server type.
     * @param handlers Whether to initialize the PlaceholderAPI handlers.
     * @param parsers Whether to initialize the parsers.
     * @param gui Whether to initialize the GUI API.
     * @param packets Whether to initialize the packets API.
     * @return The new instance of the DreamAPI. If the API has already been initialized, it will return the existing instance.
     */
    public static DreamAPI initialize(JavaPlugin plugin, String resourceName, boolean server, boolean handlers, boolean parsers, boolean gui, boolean packets, boolean metrics) {
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
        if(metrics)
            instance.initializeMetrics(plugin, resourceName);
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
        new GeyserUtils();
    }

    public void initializeParsers() {
        new CustomSoundParser();
        new GuiItemParser();
        new GuiTypeParser();
        new ItemStackParser();
        new LocationParser();
        new MessageParser();
    }

    public void initializePackets(JavaPlugin plugin) {
        protocolLibEnabled = Bukkit.getPluginManager().isPluginEnabled("ProtocolLib");
        if (!this.protocolLibEnabled)
            return;
        new PacketUtils(plugin);
    }

    public void initializeGui(JavaPlugin plugin) {
        GuiManager.initialize(plugin);
    }

    public void initializeMetrics(JavaPlugin javaPlugin, String resourceName) {
        new Metrics(plugin, resourceName, true);
    }

}
