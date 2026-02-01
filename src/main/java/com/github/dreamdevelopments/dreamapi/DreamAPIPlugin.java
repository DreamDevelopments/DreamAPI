package com.github.dreamdevelopments.dreamapi;

import com.github.dreamdevelopments.dreamapi.configuration.Config;
import com.github.dreamdevelopments.dreamapi.configuration.ConfigValue;
import com.github.dreamdevelopments.dreamapi.utils.Metrics;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * The main class for the DreamAPI plugin.
 * This will be used only if DreamAPI is installed as a standalone plugin.
 */
public class DreamAPIPlugin extends JavaPlugin {

    @Getter
    private static DreamAPIPlugin instance;

    @Getter
    private MainConfig mainConfig;

    @Override
    public void onEnable() {
        instance = this;

        mainConfig = new MainConfig(this, "config.yml");
        DreamAPI.initialize(this, mainConfig.implementation);
        try {
            Objects.requireNonNull(this.getCommand("verify")).setExecutor(new Metrics.VerifyCommand());
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * The main configuration for the DreamAPI plugin.
     */
    @Getter
    public static class MainConfig extends Config {

        //@ConfigValue("implementation")
        private String implementation = "godsurv";

        @ConfigValue("invisible-inventory-title")
        private String invisibleInventoryTitle;

        /**
         * Creates and loads the main config.
         * @param plugin The plugin that uses this config
         * @param fileName The path to the config file. It will be saved in the plugin's data folder.
         */
        public MainConfig(@NotNull JavaPlugin plugin, @NotNull String fileName) {
            super(plugin, fileName);
        }
    }
}
