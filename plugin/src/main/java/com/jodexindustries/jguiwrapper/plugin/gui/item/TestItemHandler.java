package com.jodexindustries.jguiwrapper.plugin.gui.item;

import com.jodexindustries.jguiwrapper.api.gui.handler.item.HandlerContext;
import com.jodexindustries.jguiwrapper.api.gui.handler.item.ItemHandler;
import com.jodexindustries.jguiwrapper.gui.advanced.GuiItemController;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class TestItemHandler implements ItemHandler<TestGuiLoader> {

    @Override
    public void load(@NotNull TestGuiLoader loader, @NotNull GuiItemController controller, @NotNull HandlerContext context) {
        controller.updateItemWrappers(itemWrapper -> itemWrapper.displayName("Open count: " + loader.getOpenCount()));
    }
}
