package com.jodexindustries.jguiwrapper.api.gui.handler.item;

import com.jodexindustries.jguiwrapper.api.gui.GuiDataLoader;
import com.jodexindustries.jguiwrapper.gui.advanced.GuiItemController;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings({"unused"})
public interface ItemHandler<T> {

    @SuppressWarnings("unchecked")
    default void load(@NotNull GuiDataLoader loader, @NotNull GuiItemController controller, @NotNull HandlerContext context) {
        load(((T) loader), controller, context);
    }

    void load(@NotNull T loader, @NotNull GuiItemController controller, @NotNull HandlerContext context);
}
