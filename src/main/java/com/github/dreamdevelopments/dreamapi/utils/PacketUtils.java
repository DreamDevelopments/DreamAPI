package com.github.dreamdevelopments.dreamapi.utils;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.*;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.github.dreamdevelopments.dreamapi.messages.Message;
import com.github.dreamdevelopments.dreamapi.messages.types.LegacyMessage;
import com.github.dreamdevelopments.dreamapi.messages.types.ModernMessage;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PacketUtils {

    @Getter
    private static PacketUtils instance;

    private JavaPlugin plugin;

    private final boolean enabled;

    public static boolean isEnabled() {
        return instance != null && instance.enabled;
    }

    private ProtocolManager protocolManager;

    private final HashMap<UUID, InventoryPlayer> playerInventories = new HashMap<>();

    public PacketUtils(@NotNull JavaPlugin plugin) {
        instance = this;
        this.plugin = plugin;
        if(Bukkit.getPluginManager().getPlugin("ProtocolLib") == null) {
            enabled = false;
            return;
        }
        enabled = true;
        this.protocolManager = ProtocolLibrary.getProtocolManager();
        this.registerPacketListeners();
    }

    public void registerPacketListeners() {
        protocolManager.addPacketListener(getOpenWindowPacketListener());
        protocolManager.addPacketListener(getCloseWindowPacketListener());
    }

    private PacketListener getOpenWindowPacketListener() {
        return new PacketAdapter(plugin, ListenerPriority.HIGH, PacketType.Play.Server.OPEN_WINDOW) {
            @Override
            public void onPacketSending(PacketEvent event) {
                final UUID uuid = event.getPlayer().getUniqueId();

                final int windowId = event.getPacket().getIntegers().read(0);

                InventoryPlayer oldInventory = playerInventories.get(uuid);
                if(oldInventory != null && oldInventory.windowId() == windowId)
                    return;

                final Object containerType = event.getPacket().getStructures().readSafely(0);
                String titleJson = event.getPacket().getChatComponents().read(0).getJson();

                // Create our custom holder object (defined at the end of this class) and put it in a HashMap
                InventoryPlayer player = new InventoryPlayer(windowId, containerType, titleJson);
                playerInventories.put(uuid, player);
            }
        };
    }

    private PacketListener getCloseWindowPacketListener() {
        return new PacketAdapter(plugin, ListenerPriority.HIGH, PacketType.Play.Client.CLOSE_WINDOW) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                final UUID uuid = event.getPlayer().getUniqueId();

                playerInventories.remove(uuid);
            }
        };
    }

    public static void updateTitlePlaceholders(Player player, HashMap<String, String> placeholders) {
        InventoryPlayer inventoryPlayer = getInventoryPlayer(player);
        if (inventoryPlayer == null)
            return;
        final int windowId = inventoryPlayer.windowId();
        if(windowId == 0)
            return;
        final Object windowType = inventoryPlayer.containerType();
        String titleJson = inventoryPlayer.originalTitle();

        for(Map.Entry<String, String> entry : placeholders.entrySet()) {
            titleJson = titleJson.replace(entry.getKey(), entry.getValue());
        }
        PacketUtils.getInstance().sendOpenScreenPacket(player, windowId, windowType, titleJson);
        player.updateInventory();
    }

    public static void setInventoryTitle(Player player, String titleJson) {
        InventoryPlayer inventoryPlayer = getInventoryPlayer(player);
        if (inventoryPlayer == null)
            return;
        final int windowId = inventoryPlayer.windowId();
        if(windowId == 0)
            return;
        final Object windowType = inventoryPlayer.containerType();
        PacketUtils.getInstance().sendOpenScreenPacket(player, windowId, windowType, titleJson);
        player.updateInventory();
    }
    
    public static void setInventoryTitle(Player player, Message message) {
        WrappedChatComponent component = switch (message.getType()) {
            case MODERN -> WrappedChatComponent.fromJson(((ModernMessage) message).getMessage().toString());
            case LEGACY -> WrappedChatComponent.fromLegacyText(((LegacyMessage) message).getMessage().toString());
            default -> WrappedChatComponent.fromJson("");
        };
        InventoryPlayer inventoryPlayer = getInventoryPlayer(player);
        if (inventoryPlayer == null)
            return;
        final int windowId = inventoryPlayer.windowId();
        if(windowId == 0)
            return;
        final Object windowType = inventoryPlayer.containerType();
        PacketUtils.getInstance().sendOpenScreenPacket(player, windowId, windowType, component);
        player.updateInventory();
    }
    
    public static String getInventoryTitle(Player player) {
        InventoryPlayer inventoryPlayer = getInventoryPlayer(player);
        if (inventoryPlayer == null)
            return null;
        final int windowId = inventoryPlayer.windowId();
        if(windowId == 0)
            return null;
        final Object windowType = inventoryPlayer.containerType();
        String titleJson = inventoryPlayer.originalTitle();
        return titleJson;
    }

    private static InventoryPlayer getInventoryPlayer(Player player) {
        final InventoryType type = player.getOpenInventory().getType();
        if (type == InventoryType.CRAFTING || type == InventoryType.CREATIVE)
            return null;
        return PacketUtils.getInstance().playerInventories.getOrDefault(player.getUniqueId(), null);
    }

    private void sendOpenScreenPacket(Player player, int windowId, Object windowType, String titleJson) {
        sendOpenScreenPacket(player, windowId, windowType, WrappedChatComponent.fromJson(titleJson));
    }

    private void sendOpenScreenPacket(Player player, int windowId, Object windowType, WrappedChatComponent title) {

        PacketContainer openScreen = new PacketContainer(PacketType.Play.Server.OPEN_WINDOW);
        openScreen.getIntegers().write(0, windowId);
        openScreen.getStructures().write(0, (InternalStructure) windowType);
        openScreen.getChatComponents().write(0, title);

        try {
            protocolManager.sendServerPacket(player, openScreen);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    record InventoryPlayer(int windowId, Object containerType, String originalTitle) { }


}
