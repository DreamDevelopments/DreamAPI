package com.github.dreamdevelopments.dreamapi.effects;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public record CustomSound(@Nullable Sound sound, float volume, float pitch) {

    public static final CustomSound NONE = new CustomSound(null, 0, 0);

    public void playSound(Player player) {
        if(this.sound == null)
            return;
        player.playSound(player.getLocation(), this.sound, this.volume, this.pitch);
    }

}
