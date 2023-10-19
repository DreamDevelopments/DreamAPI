package com.github.dreamdevelopments.dreamapi.configuration;

import com.github.dreamdevelopments.dreamapi.configuration.parsers.*;
import com.github.dreamdevelopments.dreamapi.effects.CustomSound;
import com.github.dreamdevelopments.dreamapi.messages.Message;
import com.github.dreamdevelopments.dreamapi.ui.GuiType;
import com.github.dreamdevelopments.dreamapi.ui.elements.GuiItem;
import com.github.dreamdevelopments.dreamapi.utils.Value;
import lombok.Getter;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

public abstract class Config extends YamlConfiguration{

    private final JavaPlugin plugin;

    @Getter
    private final String fileName;
    private final File configFile;

    public Config(JavaPlugin plugin, String fileName) {
        super();
        this.plugin = plugin;
        this.fileName = fileName;
        this.configFile = new File(plugin.getDataFolder() + File.separator + fileName);
        this.load();
    }

    protected void onReload() {}

    public void load() {
        if(!this.configFile.exists()) {
            this.plugin.saveResource(this.fileName, false);
        }
        try {
            super.load(this.configFile);
        } catch (IOException | InvalidConfigurationException error) {
            error.printStackTrace();
        }

        try {
            for (Field field : this.getClass().getDeclaredFields()) {
                ConfigValue annotation = field.getAnnotation(ConfigValue.class);
                if (annotation != null) {
                    field.setAccessible(true);
                    Class<?> fieldType = field.getType();
                    if(Parser.exists(fieldType))
                        field.set(this, Parser.getParser(fieldType).loadFromConfig(this, annotation.value()));
                    else
                        field.set(this, this.get(annotation.value()));
                }
            }
        } catch (IllegalAccessException error) {
            error.printStackTrace();
        }
        this.onReload();
    }

    @Nullable
    public ItemStack getItemStack(@NotNull String path) {
        return ItemStackParser.getInstance().loadFromConfig(this, path);
    }

    @NotNull
    public CustomSound getCustomSound(@NotNull String path) {
        return CustomSoundParser.getInstance().loadFromConfig(this, path);
    }

    @NotNull
    public GuiItem getGuiItem(@NotNull String path) {
        return GuiItemParser.getInstance().loadFromConfig(this, path);
    }

    @NotNull
    public GuiType getGuiType(@NotNull String path) {
        return GuiTypeParser.getInstance().loadFromConfig(this, path);
    }

    @NotNull
    public Value getValue(@NotNull String path) {
        return ValueParser.getInstance().loadFromConfig(this, path);
    }

    @NotNull
    public Message getMessage(@NotNull String path) {
        return Message.fromText(this.getString(path));
    }

    public int[] getSlotList(String path) {
        if(this.isInt(path))
            return new int[]{this.getInt(path)};
        return this.getIntegerList(path).stream().mapToInt(Integer::intValue).toArray();
    }

}
