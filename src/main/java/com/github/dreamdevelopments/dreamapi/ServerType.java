package com.github.dreamdevelopments.dreamapi;

import org.bukkit.Bukkit;

public enum ServerType {

    SPIGOT("spigot"),
    PAPER("paper");

    ServerType(String name) {
        if(Bukkit.getVersion().contains(name)) {
            DreamAPI.serverType = this;
        }
    }

}
