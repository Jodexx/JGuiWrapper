package com.jodexindustries.jguiwrapper.plugin.gui.item;

import com.jodexindustries.jguiwrapper.api.text.PlaceholderEngine;
import com.jodexindustries.jguiwrapper.api.user.User;
import com.jodexindustries.jguiwrapper.api.gui.types.advanced.GuiDataLoader;
import com.jodexindustries.jguiwrapper.api.gui.types.advanced.AdvancedGuiItemController;
import com.jodexindustries.jguiwrapper.paper.api.gui.types.advanced.PaperAdvancedGui;
import org.jetbrains.annotations.NotNull;

public class TestGuiLoader implements GuiDataLoader<PaperAdvancedGui> {

    private final PlaceholderEngine placeholderEngine = PlaceholderEngine.of();

    private int openCount;
    private int clickCount;

    public TestGuiLoader() {
        placeholderEngine.register("%open_count%", (player) -> String.valueOf(openCount));
        placeholderEngine.register("%click_count%", (player) -> {
            if (clickCount >= 100) return ">= 100";

            return String.valueOf(clickCount);
        });
    }

    @Override
    public void load(@NotNull PaperAdvancedGui gui, @NotNull User user) {
        openCount++;

        for (AdvancedGuiItemController<PaperAdvancedGui, ?> controller : gui.getControllers()) {
            controller.updateItems(itemWrapper -> {
                if (itemWrapper.placeholderEngine() == null) {
                    itemWrapper.placeholderEngine(placeholderEngine);
                } else {
                    itemWrapper.placeholderEngine().addAll(placeholderEngine);
                }
            });
        }
    }

    public void click() {
        clickCount++;
    }

    public int getOpenCount() {
        return openCount;
    }
}
