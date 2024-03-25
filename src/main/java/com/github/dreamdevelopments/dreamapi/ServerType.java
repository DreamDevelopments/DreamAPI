package com.github.dreamdevelopments.dreamapi;

import lombok.Getter;

/**
 * Represents the type of server software.
 * This is used to determine if the server is running a modern server software (paper-based)
 * or a legacy server software (spigot-based).
 */
@Getter
public enum ServerType {

    /**
     * Represents a spigot-based server.
     */
    SPIGOT("spigot", false),

    /**
     * Represents a paper-based server.
     */
    PAPER("paper", true);

    private final String name;

    private final boolean modern;

    ServerType(String name, boolean modern) {
        this.name = name;
        this.modern = modern;
    }

}
