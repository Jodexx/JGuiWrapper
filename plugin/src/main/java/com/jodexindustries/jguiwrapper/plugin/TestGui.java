package com.jodexindustries.jguiwrapper.plugin;

import com.jodexindustries.jguiwrapper.api.gui.handler.CancellableHandler;
import com.jodexindustries.jguiwrapper.gui.SimpleGui;

public class TestGui extends SimpleGui {

    private final SizeLooper looper = new SizeLooper();

    private int clicks;

    public TestGui() {
        super(3123, "&cExample");

        this.updateOnOpen = true;

        setClickHandlers(
                CancellableHandler.wrap(event -> {
                    title(String.valueOf(clicks++));
                    size(looper.nextSize());
                    updateMenu();
                }, true)
        );

        onOpen(event -> event.getPlayer().sendMessage(event.getInventory().getType().defaultTitle()));

        onClose(event -> event.getPlayer().sendMessage(event.getReason().name()));

        onDrag(event -> event.getWhoClicked().sendMessage(event.getType().name()));
    }

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
