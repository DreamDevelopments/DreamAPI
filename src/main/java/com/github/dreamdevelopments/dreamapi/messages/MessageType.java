package com.github.dreamdevelopments.dreamapi.messages;

/**
 * Represents the type of messages available for the {@link Message} object.
 */
public enum MessageType {

    /**
     * Represents a {@link com.github.dreamdevelopments.dreamapi.messages.types.LegacyMessage}.
     */
    LEGACY,

    /**
     * Represents a {@link com.github.dreamdevelopments.dreamapi.messages.types.ModernMessage}.
     */
    MODERN,

    /**
     * Represents an {@link com.github.dreamdevelopments.dreamapi.messages.types.EmptyMessage}.
     */
    EMPTY;

    /**
     * Checks if the message type is a {@link com.github.dreamdevelopments.dreamapi.messages.types.LegacyMessage}.
     * @return True if the message type is legacy, false otherwise
     */
    public boolean isLegacy() {
        return this.equals(LEGACY);
    }

    /**
     * Checks if the message type is a {@link com.github.dreamdevelopments.dreamapi.messages.types.ModernMessage}.
     * @return True if the message type is modern, false otherwise
     */
    public boolean isModern() {
        return this.equals(MODERN);
    }

    /**
     * Checks if the message is an {@link com.github.dreamdevelopments.dreamapi.messages.types.EmptyMessage}.
     * @return True if the message type is empty, false otherwise
     */
    public boolean isEmpty() {
        return this.equals(EMPTY);
    }

}
