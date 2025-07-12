package com.jodexindustries.jguiwrapper.gui.advanced;

import com.jodexindustries.jguiwrapper.api.gui.handler.InventoryHandler;
import com.jodexindustries.jguiwrapper.gui.SimpleGui;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

public interface AdvancedGuiClickHandler extends InventoryHandler<InventoryClickEvent> {

    @Override
    default void handle(InventoryClickEvent event, SimpleGui gui) {
        if (gui instanceof AdvancedGui advancedGui) {
            advancedGui.getController(event.getRawSlot()).ifPresent(controller -> {
                this.handle(event, controller);
            });
        } else {
            this.handle(event, (GuiItemController) null);
        }
    }

    void handle(@NotNull InventoryClickEvent event, GuiItemController controller);
}
