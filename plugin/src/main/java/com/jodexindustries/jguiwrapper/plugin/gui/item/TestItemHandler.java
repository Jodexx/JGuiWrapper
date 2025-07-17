package com.jodexindustries.jguiwrapper.plugin.gui.item;

import com.jodexindustries.jguiwrapper.api.registry.ItemHandler;
import com.jodexindustries.jguiwrapper.gui.advanced.GuiItemController;

public class TestItemHandler implements ItemHandler<TestGuiLoader> {

    @Override
    public void load(TestGuiLoader loader, GuiItemController controller) {
        controller.updateItemWrappers(itemWrapper -> {
            itemWrapper.displayName("Open count: " + loader.getOpenCount());
        });
    }
}
