package com.github.dreamdevelopments.dreamapi.configuration;

import com.github.dreamdevelopments.dreamapi.configuration.parsers.*;
import com.github.dreamdevelopments.dreamapi.effects.CustomSound;
import com.github.dreamdevelopments.dreamapi.messages.Message;
import com.github.dreamdevelopments.dreamapi.ui.GuiType;
import com.github.dreamdevelopments.dreamapi.ui.elements.GuiItem;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;

public abstract class Config extends YamlConfiguration{

    @Getter
    protected final JavaPlugin plugin;

    @Getter
    private final String fileName;
    protected final File configFile;

    @Getter
    private final String defaultPath;

    public Config(JavaPlugin plugin, String fileName) {
        this(plugin, fileName, "");
    }

    public Config(JavaPlugin plugin, String fileName, String defaultPath) {
        super();
        this.plugin = plugin;
        this.fileName = fileName;
        this.defaultPath = defaultPath;
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
            InputStream defaultInputStream = this.plugin.getResource(this.fileName);
            if(defaultInputStream != null) {
                super.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(defaultInputStream)));
            }
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
    public Message getMessage(@NotNull String path) {
        return Message.fromText(this.getString(path));
    }

    public int[] getSlotList(@NotNull String path) {
        if(this.isInt(path))
            return new int[]{this.getInt(path)};
        return this.getIntegerList(path).stream().mapToInt(Integer::intValue).toArray();
    }

    @Override
    @Contract("_, !null -> !null")
    @Nullable
    public Object get(@NotNull String path, @Nullable Object def) {
        return super.get(this.getPath(path), def);
    }

    @Override
    @Nullable
    protected Object getDefault(@NotNull String path) {
        return super.getDefault(this.getPath(path));
    }

    private String getPath(String path) {
        String[] pathLocations = path.split(String.valueOf(this.getRoot().options().pathSeparator()));
        String root = pathLocations.length > 0 ? pathLocations[0] : path;
        if(root.equals(this.defaultPath))
            return path;
        return this.defaultPath + "." + path;
    }

}
