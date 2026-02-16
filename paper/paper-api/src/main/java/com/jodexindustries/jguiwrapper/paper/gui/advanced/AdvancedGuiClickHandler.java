package com.jodexindustries.jguiwrapper.paper.gui.advanced;

import com.jodexindustries.jguiwrapper.paper.api.gui.handler.InventoryHandler;
import com.jodexindustries.jguiwrapper.paper.gui.SimpleGui;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

public interface AdvancedGuiClickHandler extends InventoryHandler<InventoryClickEvent> {

    @Override
    default void handle(@NotNull InventoryClickEvent event, @NotNull SimpleGui gui) {
        if (gui instanceof AdvancedGui) {
            ((AdvancedGui) gui).getController(event.getRawSlot()).ifPresent(controller -> this.handle(event, controller));
        } else {
            this.handle(event, (GuiItemController) null);
        }
    }

    void handle(@NotNull InventoryClickEvent event, GuiItemController controller);
}
