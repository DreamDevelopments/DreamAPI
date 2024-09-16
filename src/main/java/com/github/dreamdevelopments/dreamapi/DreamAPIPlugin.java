package com.github.dreamdevelopments.dreamapi;

import com.github.dreamdevelopments.dreamapi.utils.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

/**
 * The main class for the DreamAPI plugin.
 * This will be used only if DreamAPI is installed as a standalone plugin.
 */
public class DreamAPIPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        DreamAPI.initialize(this, "godsurv");
        try {
            Objects.requireNonNull(this.getCommand("verify")).setExecutor(new Metrics.VerifyCommand());
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
