package com.github.dreamdevelopments.dreamapi.utils;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.*;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.github.dreamdevelopments.dreamapi.DreamAPI;
import com.github.dreamdevelopments.dreamapi.DreamAPIPlugin;
import com.github.dreamdevelopments.dreamapi.messages.Message;
import com.github.dreamdevelopments.dreamapi.messages.types.LegacyMessage;
import com.github.dreamdevelopments.dreamapi.messages.types.ModernMessage;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class PacketUtils {

    @Getter
    private static PacketUtils instance;

    private JavaPlugin plugin;

    private final boolean enabled;

    public static boolean isEnabled() {
        return instance != null && instance.enabled;
    }

    private ProtocolManager protocolManager;

    @Getter
    private final HashMap<UUID, InventoryPlayer> playerInventories = new HashMap<>();

    private PacketContainer inventoryClearPacket;
    private List<ItemStack> emptyInventory;
    @Getter
    private final HashMap<UUID, InventoryData> hiddenInventoriesPlayers = new HashMap<>();

    private boolean hideInventories;

    public PacketUtils(@NotNull JavaPlugin plugin) {
        instance = this;
        this.plugin = plugin;
        if(Bukkit.getPluginManager().getPlugin("ProtocolLib") == null) {
            enabled = false;
            return;
        }
        enabled = true;
        this.protocolManager = ProtocolLibrary.getProtocolManager();

        hideInventories = DreamAPIPlugin.getInstance() != null;

        emptyInventory = new ArrayList<>(45);
        for(int i = 0; i < 45; i++) {
            emptyInventory.add(new ItemStack(Material.AIR));
        }
        inventoryClearPacket = new PacketContainer(PacketType.Play.Server.WINDOW_ITEMS);
        inventoryClearPacket.getIntegers().write(0, 0);
        inventoryClearPacket.getIntegers().write(1, -34);
        inventoryClearPacket.getItemListModifier().write(0, emptyInventory);
        inventoryClearPacket.getItemModifier().write(0, new ItemStack(Material.AIR));

        this.registerPacketListeners();
    }

    public void registerPacketListeners() {
        protocolManager.addPacketListener(getOpenWindowPacketListener());
        protocolManager.addPacketListener(getSetItemPacketListener());
        protocolManager.addPacketListener(getSetItemPacketListener2());
        protocolManager.addPacketListener(getWindowClickListener());
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

                if(hideInventories) {
                    if(titleJson.contains(DreamAPIPlugin.getInstance().getMainConfig().getInvisibleInventoryTitle())) {
                        Bukkit.getScheduler().runTaskLater(plugin, () -> {
                            if(event.getPlayer().getOpenInventory().getType() != InventoryType.CRAFTING)
                                hidePlayerInventory(event.getPlayer());
                        }, 1L);
                    }
                }

                InventoryPlayer player = new InventoryPlayer(windowId, containerType, titleJson);
                playerInventories.put(uuid, player);
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

        protocolManager.sendServerPacket(player, openScreen);
    }

    public static void hidePlayerInventory(Player player) {
        Bukkit.getScheduler().runTask(DreamAPI.getInstance().getPlugin(), () -> {
            PacketUtils.getInstance().hiddenInventoriesPlayers.put(player.getUniqueId(), new InventoryData(player.getOpenInventory().getTopInventory().getSize(), System.currentTimeMillis()));
            PacketUtils.getInstance().sendInventoryClearPacket(player);
        });
    }

    public static void restorePlayerInventory(Player player) {
        PacketUtils pu = PacketUtils.getInstance();
        JavaPlugin plugin = DreamAPI.getInstance().getPlugin();
        InventoryData data = pu.hiddenInventoriesPlayers.get(player.getUniqueId());
        if(data == null) {
            player.updateInventory();
            return;
        }
        Bukkit.getScheduler().runTask(plugin, () -> pu.sendInventoryClearPacket(player));
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            InventoryData dataNow = pu.hiddenInventoriesPlayers.get(player.getUniqueId());
            if(dataNow != null && dataNow.time == data.time) {
                pu.hiddenInventoriesPlayers.remove(player.getUniqueId());
                player.updateInventory();
            }
        }, 1L);
    }

    private void sendInventoryClearPacket(Player player) {
        protocolManager.sendServerPacket(player, inventoryClearPacket);
    }

    private PacketListener getSetItemPacketListener() {
        return new PacketAdapter(plugin, ListenerPriority.NORMAL, PacketType.Play.Server.SET_SLOT) {
            @Override
            public void onPacketSending(PacketEvent event) {
                if(!hiddenInventoriesPlayers.containsKey(event.getPlayer().getUniqueId()))
                    return;
                sendInventoryClearPacket(event.getPlayer());
                if(event.getPacket().getIntegers().getValues().get(2) >= hiddenInventoriesPlayers.get(event.getPlayer().getUniqueId()).size()) {
                    event.setCancelled(true);
                }
            }
        };
    }

    private PacketListener getSetItemPacketListener2() {
        return new PacketAdapter(plugin, ListenerPriority.NORMAL, PacketType.Play.Server.WINDOW_ITEMS) {
            @Override
            public void onPacketSending(PacketEvent event) {
                if(!hiddenInventoriesPlayers.containsKey(event.getPlayer().getUniqueId())) {
                    return;
                }
                if(event.getPacket().getIntegers().getValues().get(1)==-34) {
                    return;
                }
                int size = hiddenInventoriesPlayers.get(event.getPlayer().getUniqueId()).size;
                if(event.getPacket().getIntegers().getValues().get(0)==0) {
                    event.getPacket().getItemListModifier().write(0, emptyInventory);
                    //event.setCancelled(true);
                    return;
                }
                List<ItemStack> list = event.getPacket().getItemListModifier().read(0);
                if(list.size() > size) {
                    list = list.subList(0, size);
                    event.getPacket().getItemListModifier().write(0, list);
                }
            }
        };
    }

    private PacketListener getWindowClickListener() {
        return new PacketAdapter(plugin, ListenerPriority.NORMAL, PacketType.Play.Client.WINDOW_CLICK) {
            public void onPacketReceiving(PacketEvent event) {
                if (!hiddenInventoriesPlayers.containsKey(event.getPlayer().getUniqueId()))
                    return;
                if ((Integer) event.getPacket().getIntegers().getValues().get(2) >= hiddenInventoriesPlayers.get(event.getPlayer().getUniqueId()).size) {
                    event.setCancelled(true);
                    sendInventoryClearPacket(event.getPlayer());
                }
            }
        };
    }


    record InventoryPlayer(int windowId, Object containerType, String originalTitle) { }

    record InventoryData(int size, long time) { }


}
