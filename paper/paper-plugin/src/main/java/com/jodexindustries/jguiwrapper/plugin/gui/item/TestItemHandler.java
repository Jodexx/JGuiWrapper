package com.jodexindustries.jguiwrapper.plugin.gui.item;

import com.jodexindustries.jguiwrapper.api.gui.handler.item.HandlerContext;
import com.jodexindustries.jguiwrapper.api.gui.handler.item.ItemHandler;
import com.jodexindustries.jguiwrapper.gui.advanced.GuiItemController;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class TestItemHandler implements ItemHandler<TestGuiLoader> {

    @Override
    public void load(@NotNull TestGuiLoader loader, @NotNull GuiItemController controller, @NotNull HandlerContext context) {
        controller.defaultClickHandler((e, c) -> {
            e.setCancelled(true);
            loader.click();
            c.updateItems(itemWrapper -> itemWrapper.update(e.getWhoClicked()));
        });

        controller.updateItems(wrapper -> wrapper.lore("&cPlayer: &6%player_name%", "&cOpen count: &6%open_count%", "&cClick count: &6%click_count%"),
                (OfflinePlayer) context.player());
    }
}
