package com.jodexindustries.jguiwrapper.api.gui.types.advanced.item;

import com.jodexindustries.jguiwrapper.api.gui.types.advanced.AdvancedGuiItemController;
import com.jodexindustries.jguiwrapper.api.gui.types.advanced.GuiDataLoader;
import org.jetbrains.annotations.NotNull;

public interface ItemHandler<T> {

    default void load(@NotNull GuiDataLoader<?> loader, @NotNull AdvancedGuiItemController<?, ?> controller, @NotNull HandlerContext context) {
        load(((T) loader), controller, context);
    }

    void load(@NotNull T loader, @NotNull AdvancedGuiItemController<?, ?> controller, @NotNull HandlerContext context);
}

