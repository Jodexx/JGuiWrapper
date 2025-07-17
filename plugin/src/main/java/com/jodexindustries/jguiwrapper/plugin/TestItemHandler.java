package com.jodexindustries.jguiwrapper.plugin;

import com.jodexindustries.jguiwrapper.api.registry.ItemHandler;
import com.jodexindustries.jguiwrapper.gui.advanced.GuiItemController;

public class TestItemHandler implements ItemHandler<TestGuiLoader> {

    @Override
    public void load(TestGuiLoader loader, GuiItemController controller) {
        controller.updateAllItemWrappers(itemWrapper -> {
            itemWrapper.displayName("Open count: " + loader.getOpenCount());
        });
    }
}
