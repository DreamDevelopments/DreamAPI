package com.github.dreamdevelopments.dreamapi.configuration.parsers;

import com.github.dreamdevelopments.dreamapi.configuration.Config;
import com.github.dreamdevelopments.dreamapi.messages.Message;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

/**
 * A class to parse {@link Message} Objects from a {@link Config}.
 */
public class MessageParser extends Parser<Message> {

    @Getter
    private static MessageParser instance;

    public MessageParser() {
        super(Message.class);
        instance = this;
    }

    @Override
    public Message loadFromConfig(@NotNull Config config, @NotNull String path) {
        return Message.fromText(config.getString(path));
    }

    @Override
    public void saveToConfig(@NotNull Config config, @NotNull String path, @NotNull Message value) {
        config.set(path, value.toString());
    }
}
