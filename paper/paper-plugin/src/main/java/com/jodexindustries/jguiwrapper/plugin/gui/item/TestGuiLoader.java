package com.jodexindustries.jguiwrapper.plugin.gui.item;

import com.jodexindustries.jguiwrapper.api.text.PlaceholderEngine;
import com.jodexindustries.jguiwrapper.api.user.User;
import com.jodexindustries.jguiwrapper.paper.gui.advanced.GuiDataLoader;
import com.jodexindustries.jguiwrapper.paper.gui.advanced.AdvancedGui;
import com.jodexindustries.jguiwrapper.paper.gui.advanced.GuiItemController;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class TestGuiLoader implements GuiDataLoader {

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
    public void load(@NotNull AdvancedGui gui, @NotNull User user) {
        openCount++;

        for (GuiItemController controller : gui.getControllers()) {
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
