package com.github.dreamdevelopments.dreamapi.ui.elements;

import com.github.dreamdevelopments.dreamapi.effects.CustomSound;
import org.bukkit.inventory.ItemStack;

public record CustomInventory(int size, String title, ItemStack[] contents, CustomSound openSound, CustomSound clickSound) {
}