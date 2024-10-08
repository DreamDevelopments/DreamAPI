package com.github.dreamdevelopments.dreamapi.configuration.parsers;

import com.github.dreamdevelopments.dreamapi.configuration.Config;
import com.github.dreamdevelopments.dreamapi.messages.Message;
import com.github.dreamdevelopments.dreamapi.utils.ColorUtils;
import com.github.dreamdevelopments.dreamapi.utils.ItemUtils;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;
import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Objects;

/**
 * A class to parse {@link ItemStack} Objects from a {@link Config}.
 */
public final class ItemStackParser extends Parser<ItemStack> {

    @Getter
    private static ItemStackParser instance;

    /**
     * Initialize this parser.
     */
    public ItemStackParser() {
        super(ItemStack.class);
        instance = this;
    }

    @Override
    public ItemStack loadFromConfig(@NotNull Config config, @NotNull String path) {
        if (!config.contains(path))
            return null;
        Material material = Material.STONE;
        String rawMaterial = Objects.requireNonNull(config.getString(path + ".material"));
        try {
            material = Material.valueOf(rawMaterial.toUpperCase());
        } catch (IllegalArgumentException e) {
            Parser.warning(config, path, "Invalid material: " + rawMaterial);
        }

        ItemStack item = new ItemStack(material);
        if (config.contains(path + ".amount"))
            item.setAmount(config.getInt(path + ".amount"));

        ItemMeta itemMeta = item.getItemMeta();
        assert itemMeta != null;

        //TODO: Add support for MiniMessages

        if (config.contains(path + ".name")) {
            ItemUtils.setItemName(itemMeta, config.getMessage(path + ".name"));
        }
        if (config.contains(path + ".lore")) {
            List<Message> lore = config.getMessageList(path + ".lore");
            ItemUtils.setItemLore(itemMeta, lore);
        }
        if (config.contains(path + ".color")) {
            if (material.toString().contains("LEATHER")) {
                LeatherArmorMeta leatherMeta = (LeatherArmorMeta) itemMeta;
                leatherMeta.setColor(ColorUtils.colorFromHex(config.getString(path + ".color")));
            }
            else if(material.toString().contains("POTION")) {
                PotionMeta potionMeta = (PotionMeta) itemMeta;
                potionMeta.setColor(ColorUtils.colorFromHex(config.getString(path + ".color")));
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

        if(config.contains(path + ".item_flags")) {
            for(String flag : config.getStringList(path + ".item_flags")) {
                try {
                    itemMeta.addItemFlags(ItemFlag.valueOf(flag.toUpperCase()));
                } catch (IllegalArgumentException e) {
                    Parser.warning(config, path + ".item_flags", "Invalid item flag: " + flag);
                }
            }
        }

        if (material.equals(Material.PLAYER_HEAD)) {
            SkullMeta skullMeta = (SkullMeta) itemMeta;
            if(config.contains(path + ".owner")) {
                PlayerProfile profile = Bukkit.getServer().createPlayerProfile(config.getString(path + ".owner"));
                skullMeta.setOwnerProfile(profile);
            }
            else if(config.contains(path + ".texture")){
                PlayerProfile profile = Bukkit.getServer().createPlayerProfile("CustomHead");
                PlayerTextures textures = profile.getTextures();
                String texture = config.getString(path + ".texture");
                assert texture != null;
                String url = null;
                if(texture.startsWith("http://textures.minecraft.net/texture/"))
                    url = texture;
                else if(texture.startsWith("eyJ"))
                    url = ItemUtils.getTextureURL(texture);
                else
                    Parser.warning(config, path, "Invalid skull texture. Use either a \"http://textures.minecraft.net/texture/\" url or a base64 encoded texture.");

                if(url != null) {
                    try {
                        textures.setSkin(new URL(url));
                        skullMeta.setOwnerProfile(profile);
                    } catch (MalformedURLException e) {
                        Parser.warning(config, path,
                                "Invalid texture url \"" + url + "\". The texture url must start with \"http://textures.minecraft.net/texture/\"."
                        );
                    }
                }
            }
        }
        item.setItemMeta(itemMeta);
        return item;
    }

    @Override
    public void saveToConfig(@NotNull Config config, @NotNull String path, @NotNull ItemStack value) {
        //TODO: Implement saveToConfig method
    }

}
