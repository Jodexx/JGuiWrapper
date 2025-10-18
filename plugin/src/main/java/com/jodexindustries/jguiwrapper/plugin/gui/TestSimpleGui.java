package com.jodexindustries.jguiwrapper.plugin.gui;

import com.jodexindustries.jguiwrapper.api.gui.handler.CancellableHandler;
import com.jodexindustries.jguiwrapper.gui.SimpleGui;
import net.kyori.adventure.text.Component;

public class TestSimpleGui extends SimpleGui {

    private final SizeLooper looper = new SizeLooper();

    private int clicks;

    public TestSimpleGui() {
        super(3123, "&cExample");

        setClickHandlers(
                CancellableHandler.wrap((event, gui) ->
                        runTask(() ->
                                updateMenu(event.getWhoClicked(), type(), looper.nextSize(), Component.text(clicks++), true)), true)
        );

        onOpen(event -> event.getPlayer().sendMessage(event.getInventory().getType().defaultTitle()));

        onClose(event -> event.getPlayer().sendMessage(event.getReason().name()));

        onDrag(event -> event.getWhoClicked().sendMessage(event.getType().name()));
    }

    @SuppressWarnings("unused")
    private static class SizeLooper {
        private int currentSize = 54;
        private boolean increasing = true;

        public int nextSize() {
            if (increasing) {
                currentSize += 9;
                if (currentSize > 54) {
                    currentSize = 45;
                    increasing = false;
                }
            } else {
                currentSize -= 9;
                if (currentSize < 9) {
                    currentSize = 18;
                    increasing = true;
                }
            }
            return currentSize;
        }

    }

}
