package com.github.dreamdevelopments.dreamapi.configuration.parsers;

import com.github.dreamdevelopments.dreamapi.configuration.Config;
import com.github.dreamdevelopments.dreamapi.effects.CustomSound;
import lombok.Getter;
import org.bukkit.Sound;
import org.jetbrains.annotations.NotNull;

public class CustomSoundParser extends Parser<CustomSound> {

    @Getter
    private final static CustomSoundParser instance = new CustomSoundParser();

    private CustomSoundParser() {
        super(CustomSound.class);
    }

    @Override
    public CustomSound loadFromConfig(@NotNull Config config, @NotNull String path) {
        String stringValue = config.getString(path);
        if(stringValue == null || stringValue.isEmpty())
            return CustomSound.NONE;
        String[] rawData = stringValue.split(" ");
        Sound sound = Sound.valueOf(rawData[0].toUpperCase());
        float volume = 1, pitch = 1;
        if(rawData.length == 2)
            volume = Float.parseFloat(rawData[1]);
        else if(rawData.length >= 3)
            pitch = Float.parseFloat(rawData[2]);
        return new CustomSound(sound, volume, pitch);
    }
}
