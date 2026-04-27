package com.jodexindustries.jguiwrapper.api.gui.event;

import com.jodexindustries.jguiwrapper.api.gui.Gui;
import com.jodexindustries.jguiwrapper.api.user.User;
import org.jetbrains.annotations.NotNull;

public class GuiClickEvent<T extends Gui> extends CancellableGuiEvent<T> {

    private final int rawSlot;
    private final boolean playerInventory;
    private final InventoryAction action;
    private final ClickType clickType;

    public GuiClickEvent(@NotNull Object handle, @NotNull T gui, @NotNull User user, int rawSlot, boolean playerInventory, InventoryAction action, ClickType clickType) {
        super(handle, gui, user);
        this.rawSlot = rawSlot;
        this.playerInventory = playerInventory;
        this.action = action;
        this.clickType = clickType;
    }

    public int rawSlot() {
        return rawSlot;
    }

    public boolean playerInventory() {
        return playerInventory;
    }

    public InventoryAction action() {
        return action;
    }

    public ClickType clickType() {
        return clickType;
    }


    // Source - Bukkit
    public enum InventoryAction {
        NOTHING, PICKUP_ALL, PICKUP_SOME, PICKUP_HALF, PICKUP_ONE, PLACE_ALL, PLACE_SOME, PLACE_ONE, SWAP_WITH_CURSOR, DROP_ALL_CURSOR, DROP_ONE_CURSOR, DROP_ALL_SLOT, DROP_ONE_SLOT, MOVE_TO_OTHER_INVENTORY, HOTBAR_MOVE_AND_READD, HOTBAR_SWAP, CLONE_STACK, COLLECT_TO_CURSOR, UNKNOWN
    }

    // Source - Bukkit
    public enum ClickType {
        LEFT, SHIFT_LEFT, RIGHT, SHIFT_RIGHT, WINDOW_BORDER_LEFT, WINDOW_BORDER_RIGHT, MIDDLE, NUMBER_KEY, DOUBLE_CLICK, DROP, CONTROL_DROP, CREATIVE, SWAP_OFFHAND, UNKNOWN;

        public boolean isKeyboardClick() {
            return (this == ClickType.NUMBER_KEY) || (this == ClickType.DROP) || (this == ClickType.CONTROL_DROP);
        }

        public boolean isCreativeAction() {
            return (this == ClickType.MIDDLE) || (this == ClickType.CREATIVE);
        }

        public boolean isRightClick() {
            return (this == ClickType.RIGHT) || (this == ClickType.SHIFT_RIGHT);
        }

        public boolean isLeftClick() {
            return (this == ClickType.LEFT) || (this == ClickType.SHIFT_LEFT) || (this == ClickType.DOUBLE_CLICK) || (this == ClickType.CREATIVE);
        }

        public boolean isShiftClick() {
            return (this == ClickType.SHIFT_LEFT) || (this == ClickType.SHIFT_RIGHT) || (this == ClickType.CONTROL_DROP);
        }
    }
}
