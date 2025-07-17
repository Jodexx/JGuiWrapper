package com.jodexindustries.jguiwrapper.api.registry;

import com.jodexindustries.jguiwrapper.gui.advanced.GuiItemController;

public interface ItemHandler<T> {


    @SuppressWarnings("unchecked")
    default void load(GuiDataLoader loader, GuiItemController controller) {
        load(((T) loader), controller);
    }

    void load(T loader, GuiItemController controller);
}
