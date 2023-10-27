package com.github.dreamdevelopments.dreamapi.configuration.parsers;

import com.github.dreamdevelopments.dreamapi.configuration.Config;
import com.github.dreamdevelopments.dreamapi.messages.Message;
import org.jetbrains.annotations.NotNull;

public class MessageParser extends Parser<Message> {

    private static MessageParser instance;

    public static MessageParser getInstance() {
        if(instance == null)
            instance = new MessageParser();
        return instance;
    }

    private MessageParser() {
        super(Message.class);
    }

    @Override
    public Message loadFromConfig(@NotNull Config config, @NotNull String path) {
        return Message.fromText(config.getString(path));
    }
}
