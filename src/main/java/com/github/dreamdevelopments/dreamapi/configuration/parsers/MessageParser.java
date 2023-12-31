package com.github.dreamdevelopments.dreamapi.configuration.parsers;

import com.github.dreamdevelopments.dreamapi.configuration.Config;
import com.github.dreamdevelopments.dreamapi.messages.Message;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

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
}
