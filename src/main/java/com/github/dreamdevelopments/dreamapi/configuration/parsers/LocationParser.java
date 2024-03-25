package com.github.dreamdevelopments.dreamapi.configuration.parsers;

import com.github.dreamdevelopments.dreamapi.configuration.Config;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

/**
 * A class to parse {@link Location} Objects from a {@link Config}.
 */
public class LocationParser extends Parser<Location> {

    @Getter
    private static LocationParser instance;

    /**
     * Initialize this parser.
     */
    public LocationParser() {
        super(Location.class);
        instance = this;
    }

    @Override
    public Location loadFromConfig(@NotNull Config config, @NotNull String path) {
        String raw = config.getString(path);
        assert raw != null;
        String[] rawData = raw.split(" ");
        World world = Bukkit.getWorld(rawData[0]);
        if(world == null) {
            Parser.warning(config, path, "Invalid world in location: " + raw);
            return null;
        }
        double x, y, z;
        try {
            x = Double.parseDouble(rawData[1]);
            y = Double.parseDouble(rawData[2]);
            z = Double.parseDouble(rawData[3]);
        } catch (NumberFormatException e) {
            Parser.warning(config, path, "Invalid coordinates in location: " + raw);
            return null;
        }
        if(rawData.length == 4)
            return new Location(world, x, y, z);
        float yaw, pitch;
        try {
            yaw = Float.parseFloat(rawData[4]);
            pitch = Float.parseFloat(rawData[5]);
        } catch (NumberFormatException e) {
            Parser.warning(config, path, "Invalid yaw and pitch in location: " + raw);
            return null;
        }
        return new Location(world, x, y, z, yaw, pitch);
    }

    @Override
    public void saveToConfig(@NotNull Config config, @NotNull String path, @NotNull Location value) {
        String stringValue = value.getWorld().getName() + " " + value.getX() + " " + value.getY() + " " + value.getZ();
        if(value.getYaw() != 0)
            stringValue += " " + value.getYaw();
        if(value.getPitch() != 0)
            stringValue += " " + value.getPitch();
        config.set(path, stringValue);
    }
}
