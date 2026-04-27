package com.jodexindustries.jguiwrapper.api.gui.types.advanced;

import com.jodexindustries.jguiwrapper.api.gui.GuiHandler;
import com.jodexindustries.jguiwrapper.api.gui.types.SimpleGui;
import com.jodexindustries.jguiwrapper.api.gui.event.GuiClickEvent;
import org.jetbrains.annotations.NotNull;

public interface AdvancedGuiClickHandler<G extends SimpleGui<G> & AdvancedGui<G>> extends GuiHandler<GuiClickEvent<G>, G> {

    @Override
    default void handle(@NotNull GuiClickEvent<G> event, @NotNull G gui) {
        gui.getController(event.rawSlot()).ifPresent(controller -> this.handle(event, controller));
    }

    void handle(@NotNull GuiClickEvent<G> event, @NotNull AdvancedGuiItemController<G, ?> controller);
}

