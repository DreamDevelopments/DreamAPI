package com.github.dreamdevelopments.dreamapi;

import lombok.Getter;
import org.bukkit.Bukkit;

@Getter
public enum ServerType {

    SPIGOT("spigot"),
    PAPER("paper");

    private final String name;

    ServerType(String name) {
        this.name = name;
    }

}
