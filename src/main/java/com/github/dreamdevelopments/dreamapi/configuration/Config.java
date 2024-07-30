package com.github.dreamdevelopments.dreamapi.configuration;

import com.github.dreamdevelopments.dreamapi.configuration.parsers.*;
import com.github.dreamdevelopments.dreamapi.effects.CustomSound;
import com.github.dreamdevelopments.dreamapi.messages.Message;
import com.github.dreamdevelopments.dreamapi.ui.GuiType;
import com.github.dreamdevelopments.dreamapi.ui.elements.GuiItem;
import com.github.dreamdevelopments.dreamapi.utils.ReflectionUtils;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
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
import java.util.List;

/**
 * Abstract class that represents a configuration file.
 * This class is used to load and save configuration files easily.
 * It supports the use of {@link Parser} to load and save custom objects.
 * Use the annotation {@link ConfigValue} easily link a local variable to a value in the config.
 */
public abstract class Config extends YamlConfiguration{

    /**
     * The plugin that uses this config.
     */
    @Getter @NotNull
    protected final JavaPlugin plugin;

    /**
     * The path of the file for this config. It is saved in the plugin's data folder.
     */
    @Getter @NotNull
    private final String fileName;

    /**
     * The file object for this config.
     */
    protected final File configFile;

    /**
     * The base path for this config in the file.
     */
    @Getter @NotNull
    private final String defaultPath;


    /**
     * Create a new Config object.
     * @param plugin The plugin that uses this config
     * @param fileName The path to the config file. It will be saved in the plugin's data folder.
     */
    public Config(@NotNull JavaPlugin plugin, @NotNull String fileName) {
        this(plugin, fileName, "");
    }

    /**
     * Create a new Config object.
     * @param plugin The plugin that uses this config
     * @param fileName The path to the config file. It will be saved in the plugin's data folder.
     * @param defaultPath The base path for this config uses in the file.
     */
    public Config(@NotNull JavaPlugin plugin, @NotNull String fileName, @NotNull String defaultPath) {
        super();
        this.plugin = plugin;
        this.fileName = fileName;
        this.defaultPath = defaultPath;
        this.configFile = new File(plugin.getDataFolder() + File.separator + fileName);
        this.load();
    }

    /**
     * This function gets called when the config reloads.
     * It should be overridden to add custom behavior.
     * If you want to reload the config, use the {@link #load()} method.
     */
    protected void onReload() {}

    /**
     * Load or reload the configuration.
     * This creates the file if it doesn't exist, sets the values for the fields annotated with {@link ConfigValue} and calls the {@link #onReload()} method.
     */
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

        for (Field field : ReflectionUtils.getAllFields(this.getClass())) {
            ConfigValue annotation = field.getAnnotation(ConfigValue.class);
            if (annotation != null) {
                field.setAccessible(true);
                Class<?> fieldType = field.getType();
                try {
                    if(Parser.exists(fieldType)) {
                        Object value = Parser.getParser(fieldType).loadFromConfig(this, annotation.value());
                        field.set(this, value);
                    }
                    else
                        field.set(this, this.get(annotation.value()));
                } catch (Exception error) {
                    Bukkit.getLogger().warning("Error while loading " + this.fileName + " " + this.defaultPath + "." + annotation.value() + " field");
                    error.printStackTrace();
                }
            }
        }
        this.onReload();
    }

    /**
     * Attempt to save the configuration to the file using {@link #save(File)}.
     * If an error occurs, it will be printed to the console.
     */
    public void saveTry() {
        try {
            this.save(this.configFile);
        } catch (IOException error) {
            error.printStackTrace();
        }
    }

    /**
     * Get an ItemStack from the config using the {@link ItemStackParser}.
     * @param path The path to the ItemStack to get
     * @return The requested ItemStack
     */
    @Nullable
    public ItemStack getItemStack(@NotNull String path) {
        return ItemStackParser.getInstance().loadFromConfig(this, path);
    }

    /**
     * Get a Location from the config using the {@link LocationParser}.
     * @param path The path to the Location to get
     * @return The requested Location
     */
    @Nullable @Override
    public Location getLocation(@NotNull String path) {
        return LocationParser.getInstance().loadFromConfig(this, path);
    }

    /**
     * Get a CustomSound from the config using the {@link CustomSoundParser}.
     * @param path The path to the CustomSound to get
     * @return The requested CustomSound
     */
    @NotNull
    public CustomSound getCustomSound(@NotNull String path) {
        return CustomSoundParser.getInstance().loadFromConfig(this, path);
    }

/**
     * Get a GuiItem from the config using the {@link GuiItemParser}.
     * @param path The path to the GuiItem to get
     * @return The requested GuiItem
     */
    @NotNull
    public GuiItem getGuiItem(@NotNull String path) {
        return GuiItemParser.getInstance().loadFromConfig(this, path);
    }

    /**
     * Get a GuiType from the config using the {@link GuiTypeParser}.
     * @param path The path to the GuiType to get
     * @return The requested GuiType
     */
    @NotNull
    public GuiType getGuiType(@NotNull String path) {
        return GuiTypeParser.getInstance().loadFromConfig(this, path);
    }

    /**
     * Get a Message from the config using the {@link MessageParser}.
     * @param path The path to the Message to get
     * @return The requested Message
     */
    @NotNull
    public Message getMessage(@NotNull String path) {
        return Message.fromText(this.getString(path));
    }

    /**
     * Get a list of Messages from the config using the {@link MessageParser}.
     * @param path The path to the list of Messages to get
     * @return The requested list
     */
    @NotNull
    public List<Message> getMessageList(@NotNull String path) {
        return this.getStringList(path).stream().map(Message::fromText).toList();
    }

    /**
     * Get an array of integers from the config.
     * If the path points to an integer instead of an array, it will return an array of size 1 with that element.
     * @param path The path to the integer array to get
     * @return The requested integer array
     */
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

    @Override
    public void set(@NotNull String path, @Nullable Object value) {
        if(value != null && Parser.exists(value.getClass())) {
            Parser.getParser(value.getClass()).saveObjectToConfig(this, path, value);
            return;
        }
        super.set(this.getPath(path), value);
    }

    /**
     * Get the absolute path in the config from a relative path by adding the default path to it.
     * @param path The relative path
     * @return The absolute path
     */
    private String getPath(String path) {
        String[] pathLocations = path.split(String.valueOf(this.getRoot().options().pathSeparator()));
        String root = pathLocations.length > 0 ? pathLocations[0] : path;
        if(root.equals(this.defaultPath))
            return path;
        return this.defaultPath + "." + path;
    }

}
