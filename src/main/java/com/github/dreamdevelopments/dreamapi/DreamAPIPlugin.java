package com.github.dreamdevelopments.dreamapi;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * The main class for the DreamAPI plugin.
 * This will be used only if DreamAPI is installed as a standalone plugin.
 */
public class DreamAPIPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        DreamAPI.initialize(this, "dreamAPI");
    }
}
