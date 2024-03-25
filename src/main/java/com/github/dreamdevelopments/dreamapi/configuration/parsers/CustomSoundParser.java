package com.github.dreamdevelopments.dreamapi.configuration.parsers;

import com.github.dreamdevelopments.dreamapi.configuration.Config;
import com.github.dreamdevelopments.dreamapi.effects.CustomSound;
import lombok.Getter;
import org.bukkit.Sound;
import org.jetbrains.annotations.NotNull;

/**
 * A class to parse {@link CustomSound} Objects from a {@link Config}.
 */
public class CustomSoundParser extends Parser<CustomSound> {

    @Getter
    private static CustomSoundParser instance;

    public CustomSoundParser() {
        super(CustomSound.class);
        instance = this;
    }

    @Override
    public CustomSound loadFromConfig(@NotNull Config config, @NotNull String path) {
        String stringValue = config.getString(path);
        if(stringValue == null || stringValue.isEmpty())
            return CustomSound.NONE;
        String[] rawData = stringValue.split(" ");
        Sound sound;
        try {
            sound = Sound.valueOf(rawData[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            Parser.warning(config, path, "Invalid sound: " + rawData[0]);
            return CustomSound.NONE;
        }
        float volume = 1, pitch = 1;
        if(rawData.length == 2)
            volume = Float.parseFloat(rawData[1]);
        else if(rawData.length >= 3)
            pitch = Float.parseFloat(rawData[2]);
        return new CustomSound(sound, volume, pitch);
    }

    @Override
    public void saveToConfig(@NotNull Config config, @NotNull String path, @NotNull CustomSound value) {
        config.set(path, (value.sound() != null ? value.sound().name() : "NULL") + " " + value.volume() + " " + value.pitch());
    }
}
