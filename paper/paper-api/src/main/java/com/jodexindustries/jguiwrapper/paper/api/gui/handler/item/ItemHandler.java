package com.jodexindustries.jguiwrapper.paper.api.gui.handler.item;

import com.jodexindustries.jguiwrapper.paper.api.gui.GuiDataLoader;
import com.jodexindustries.jguiwrapper.paper.gui.advanced.GuiItemController;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings({"unused", "unchecked"})
public interface ItemHandler<T> {

    default void load(@NotNull GuiDataLoader<?> loader, @NotNull GuiItemController controller, @NotNull HandlerContext<Player> context) {
        load(((T) loader), controller, context);
    }

    void load(@NotNull T loader, @NotNull GuiItemController controller, @NotNull HandlerContext<Player> context);
}
