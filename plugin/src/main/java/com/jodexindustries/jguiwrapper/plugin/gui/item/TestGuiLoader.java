package com.jodexindustries.jguiwrapper.plugin.gui.item;

import com.jodexindustries.jguiwrapper.api.gui.GuiDataLoader;
import com.jodexindustries.jguiwrapper.api.placeholder.PlaceholderEngine;
import com.jodexindustries.jguiwrapper.gui.advanced.AdvancedGui;
import com.jodexindustries.jguiwrapper.gui.advanced.GuiItemController;
import org.bukkit.entity.HumanEntity;

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
    public void load(AdvancedGui gui, HumanEntity player) {
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
