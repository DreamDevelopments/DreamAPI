package com.github.dreamdevelopments.dreamapi.messages;

public enum MessageType {

    LEGACY,
    MODERN,
    EMPTY;

    public boolean isLegacy() {
        return this.equals(LEGACY);
    }

    public boolean isModern() {
        return this.equals(MODERN);
    }

    public boolean isEmpty() {
        return this.equals(EMPTY);
    }

}
