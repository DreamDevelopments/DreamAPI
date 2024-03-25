package com.github.dreamdevelopments.dreamapi;

import lombok.Getter;

@Getter
public enum ServerType {

    SPIGOT("spigot", false),
    PAPER("paper", true);

    private final String name;

    private final boolean modern;

    ServerType(String name, boolean modern) {
        this.name = name;
        this.modern = modern;
    }

}
