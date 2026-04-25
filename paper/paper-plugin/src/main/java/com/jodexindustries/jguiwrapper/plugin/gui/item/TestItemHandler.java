package com.jodexindustries.jguiwrapper.plugin.gui.item;

import com.jodexindustries.jguiwrapper.paper.api.gui.handler.item.HandlerContext;
import com.jodexindustries.jguiwrapper.paper.api.gui.handler.item.ItemHandler;
import com.jodexindustries.jguiwrapper.paper.gui.advanced.GuiItemController;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class TestItemHandler implements ItemHandler<TestGuiLoader> {

    @Override
    public void load(@NotNull TestGuiLoader loader, @NotNull GuiItemController controller, @NotNull HandlerContext<Player> context) {
        controller.defaultClickHandler((e, c) -> {
            e.setCancelled(true);
            if (e.getClick() == ClickType.DOUBLE_CLICK) return;

            loader.click();
            c.updateItems(itemWrapper -> itemWrapper.update(e.getWhoClicked()));
        });

        controller.updateItems(wrapper -> wrapper.meta()
                        .displayName("&b&lDiamond Block")
                        .lore("&cPlayer: &6%player_name%", "&cOpen count: &6%open_count%", "&cClick count: &6%click_count%"), context.player());
    }
}
