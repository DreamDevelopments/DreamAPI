package com.github.dreamdevelopments.dreamapi;

import org.bukkit.plugin.java.JavaPlugin;

public class DreamAPIPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        DreamAPI.initialize(this, "dreamAPI");
    }
}
