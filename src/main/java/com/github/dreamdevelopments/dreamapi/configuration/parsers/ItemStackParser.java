package com.github.dreamdevelopments.dreamapi.configuration.parsers;

import com.github.dreamdevelopments.dreamapi.configuration.Config;
import com.github.dreamdevelopments.dreamapi.utils.ColorUtils;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public final class ItemStackParser extends Parser<ItemStack> {

    @Getter
    private static ItemStackParser instance;

    public ItemStackParser() {
        super(ItemStack.class);
        instance = this;
    }

    @Override
    public ItemStack loadFromConfig(@NotNull Config config, @NotNull String path) {
        if (!config.contains(path))
            return null;
        Material material = Material.STONE;
        try {
            material = Material.valueOf(Objects.requireNonNull(config.getString(path + ".material")).toUpperCase());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        ItemStack item = new ItemStack(material);
        if (config.contains(path + ".amount"))
            item.setAmount(config.getInt(path + ".amount"));

        ItemMeta itemMeta = item.getItemMeta();
        assert itemMeta != null;

        if (config.contains(path + ".name")) {
            itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(config.getString(path + ".name"))));
        }
        if (config.contains(path + ".lore")) {
            List<String> lore = config.getStringList(path + ".lore");
            for (int i = 0; i < lore.size(); i++) {
                lore.set(i, ChatColor.translateAlternateColorCodes('&', lore.get(i)));
            }
            itemMeta.setLore(lore);
        }
        if (config.contains(path + ".color")) {
            if (material.toString().contains("LEATHER")) {
                LeatherArmorMeta leatherMeta = (LeatherArmorMeta) itemMeta;
                leatherMeta.setColor(ColorUtils.colorFromHex(config.getString(path + ".color")));
            }
        }
        if (config.contains(path + ".enchants")) {
            for (String enchantString : config.getConfigurationSection(path + ".enchants").getKeys(false)) {
                Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft(enchantString.toLowerCase()));
                if (enchantment == null) {
                    continue;
                }
                int enchantmentLevel = config.getInt(path + ".enchants." + enchantString);
                itemMeta.addEnchant(enchantment, enchantmentLevel, true);
            }
        }
        if (config.contains(path + ".custom_model_data")) {
            itemMeta.setCustomModelData(config.getInt(path + ".custom_model_data"));
        }

        if (material.equals(Material.PLAYER_HEAD)) {
            SkullMeta skullMeta = (SkullMeta) itemMeta;
/*            if(this.getConfig().contains(path + ".owner")) {
                PlayerProfile profile = Bukkit.getServer().createPlayerProfile(this.getConfig().getString(path + ".owner"));
                skullMeta.setOwnerProfile(profile);
            }
            else if(this.getConfig().contains(path + ".texture")){
                GameProfile gameProfile = new GameProfile(UUID.randomUUID(), null);
                gameProfile.getProperties().put("textures", new Property("textures", this.getConfig().getString(path + ".texture")));
                try {
                    Field profileField = skullMeta.getClass().getDeclaredField("profile");
                    profileField.setAccessible(true);
                    profileField.set(skullMeta, gameProfile);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }*/
        }
        item.setItemMeta(itemMeta);
        return item;
    }

}
