package com.github.dreamdevelopments.dreamapi.effects;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

/**
 * A class to represent a sound effect.
 * @param sound The {@link Sound} object to play
 * @param volume The volume of the sound
 * @param pitch The pitch of the sound
 */
public record CustomSound(@Nullable Sound sound, float volume, float pitch) {

    /**
     * A constant to represent no sound. This should be usually returned instead of null.
     */
    public static final CustomSound NONE = new CustomSound(null, 0, 0);

    /**
     * Plays the sound to the player.
     * @param player The player to play the sound to
     */
    public void playSound(Player player) {
        if(this.sound == null)
            return;
        player.playSound(player.getLocation(), this.sound, this.volume, this.pitch);
    }

}
