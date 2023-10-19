package com.github.dreamdevelopments.dreamapi.configuration.parsers;
import com.github.dreamdevelopments.dreamapi.configuration.Config;
import com.github.dreamdevelopments.dreamapi.utils.Value;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public class ValueParser extends Parser<Value> {

    @Getter
    private static final ValueParser instance = new ValueParser();

    private ValueParser() {
        super(Value.class);
    }

    @Override
    public Value loadFromConfig(@NotNull Config config, @NotNull String path) {
        if(config.isInt(path)) {
            return new Value.Static(config.getInt(path));
        }
        else if(config.isString(path)) {
            String[] rawValues = config.getString(path).replace(" ", "").split(":");
            return new Value.Random(Integer.parseInt(rawValues[0]), Integer.parseInt(rawValues[1]));
        }
        return Value.ZERO;
    }
}