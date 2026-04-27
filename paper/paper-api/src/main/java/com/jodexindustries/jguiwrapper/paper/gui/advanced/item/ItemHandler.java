package com.jodexindustries.jguiwrapper.paper.gui.advanced.item;

import com.jodexindustries.jguiwrapper.paper.gui.advanced.GuiDataLoader;
import com.jodexindustries.jguiwrapper.paper.gui.advanced.GuiItemController;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings({"unused", "unchecked"})
public interface ItemHandler<T> {

    default void load(@NotNull GuiDataLoader loader, @NotNull GuiItemController controller, @NotNull HandlerContext context) {
        load(((T) loader), controller, context);
    }

    void load(@NotNull T loader, @NotNull GuiItemController controller, @NotNull HandlerContext context);
}
