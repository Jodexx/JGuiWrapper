package com.jodexindustries.jguiwrapper.paper.gui.advanced;

import com.jodexindustries.jguiwrapper.api.gui.GuiHandler;
import com.jodexindustries.jguiwrapper.api.gui.event.GuiClickEvent;
import org.jetbrains.annotations.NotNull;

public interface AdvancedGuiClickHandler extends GuiHandler<GuiClickEvent<AdvancedGui>, AdvancedGui> {

    @Override
    default void handle(@NotNull GuiClickEvent<AdvancedGui> event, @NotNull AdvancedGui gui) {
        gui.getController(event.rawSlot()).ifPresent(controller -> this.handle(event, controller));
    }

    void handle(@NotNull GuiClickEvent<AdvancedGui> event, GuiItemController controller);
}
