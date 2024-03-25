package com.github.dreamdevelopments.dreamapi.configuration.parsers;

import com.github.dreamdevelopments.dreamapi.configuration.Config;
import com.github.dreamdevelopments.dreamapi.ui.elements.GuiItem;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

public class GuiItemParser extends Parser<GuiItem> {

    @Getter
    private static GuiItemParser instance;

    public GuiItemParser() {
        super(GuiItem.class);
        instance = this;
    }

    @Override
    public GuiItem loadFromConfig(@NotNull Config config, @NotNull String path) {
        return new GuiItem(
                config.getItemStack(path),
                config.getSlotList(path + ".slot")
        );
    }

    @Override
    public void saveToConfig(@NotNull Config config, @NotNull String path, @NotNull GuiItem value) {
        config.set(path, value.item());
        if(value.slots().length == 1) {
            config.set(path + ".slot", value.slots()[0]);
        }
        else {
            ArrayList<Integer> slots = new ArrayList<>();
            Arrays.stream(value.slots()).forEach(slots::add);
            config.set(path + ".slot", slots);
        }
    }
}
