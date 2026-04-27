package com.jodexindustries.jguiwrapper.plugin.gui.item;

import com.jodexindustries.jguiwrapper.api.gui.event.GuiClickEvent;
import com.jodexindustries.jguiwrapper.api.gui.types.advanced.AdvancedGuiItemController;
import com.jodexindustries.jguiwrapper.api.gui.types.advanced.item.HandlerContext;
import com.jodexindustries.jguiwrapper.api.gui.types.advanced.item.ItemHandler;
import org.jetbrains.annotations.NotNull;

public class TestItemHandler implements ItemHandler<TestGuiLoader> {

    @Override
    public void load(@NotNull TestGuiLoader loader, @NotNull AdvancedGuiItemController<?, ?> controller, @NotNull HandlerContext context) {
        controller.defaultClickHandler((e, c) -> {
            e.setCancelled(true);
            if (e.clickType() == GuiClickEvent.ClickType.DOUBLE_CLICK) return;

            loader.click();
            c.updateItems(itemWrapper -> itemWrapper.update(e.user()));
        });

        controller.updateItems(wrapper -> wrapper.meta()
                        .displayName("&b&lDiamond Block")
                        .lore("&cPlayer: &6%player_name%", "&cOpen count: &6%open_count%", "&cClick count: &6%click_count%"), context.user());
    }
}
