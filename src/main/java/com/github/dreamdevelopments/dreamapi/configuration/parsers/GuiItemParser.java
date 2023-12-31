package com.github.dreamdevelopments.dreamapi.configuration.parsers;

import com.github.dreamdevelopments.dreamapi.configuration.Config;
import com.github.dreamdevelopments.dreamapi.ui.elements.GuiItem;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

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
}
