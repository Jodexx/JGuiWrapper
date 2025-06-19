package com.jodexindustries.jguiwrapper.gui.advanced;

import com.jodexindustries.jguiwrapper.api.gui.handler.InventoryHandler;
import com.jodexindustries.jguiwrapper.gui.SimpleGui;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

public interface AdvancedGuiClickHandler extends InventoryHandler<InventoryClickEvent> {

    @Override
    default void handle(InventoryClickEvent event, SimpleGui gui) {
        GuiItemController controller = null;

        if (gui instanceof AdvancedGui advancedGui) controller = advancedGui.getController(event.getRawSlot());

        this.handle(event, controller);
    }

    void handle(@NotNull InventoryClickEvent event, GuiItemController controller);
}
