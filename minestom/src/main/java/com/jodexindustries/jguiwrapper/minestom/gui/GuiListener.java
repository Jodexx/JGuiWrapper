package com.jodexindustries.jguiwrapper.minestom.gui;

import com.jodexindustries.jguiwrapper.api.gui.event.CancellableGuiEvent;
import com.jodexindustries.jguiwrapper.api.gui.event.GuiClickEvent;
import com.jodexindustries.jguiwrapper.api.gui.event.GuiCloseEvent;
import com.jodexindustries.jguiwrapper.api.gui.event.GuiDragEvent;
import com.jodexindustries.jguiwrapper.minestom.MinestomGuiApi;
import com.jodexindustries.jguiwrapper.minestom.gui.types.MinestomGuiBase;
import com.jodexindustries.jguiwrapper.minestom.user.MinestomUser;
import com.jodexindustries.jguiwrapper.minestom.utils.GuiUtils;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.event.trait.InventoryEvent;
import net.minestom.server.inventory.AbstractInventory;
import net.minestom.server.inventory.PlayerInventory;
import net.minestom.server.inventory.click.Click;
import net.minestom.server.item.ItemStack;

import static com.jodexindustries.jguiwrapper.api.gui.event.GuiClickEvent.*;
import static com.jodexindustries.jguiwrapper.api.gui.event.GuiClickEvent.ClickType;

public class GuiListener {

    public static void register(GlobalEventHandler handler) {
        EventNode<InventoryEvent> eventNode = EventNode.type("jguiwrapper", EventFilter.INVENTORY);
        eventNode.addListener(InventoryPreClickEvent.class, e -> {
            AbstractInventory inventory = e.getInventory();
            MinestomGuiHolder holder = GuiUtils.getHolder(inventory);
            if (holder == null) return;

            MinestomGuiBase<?> gui = holder.gui();

            CancellableGuiEvent event;

            Player player = e.getPlayer();
            MinestomUser user = MinestomGuiApi.get().user(player);

            Click click = e.getClick();
            if (click instanceof Click.Drag drag) {
                event = new GuiDragEvent(
                        e,
                        gui,
                        user,
                        drag.slots()
                );
                gui.onDrag((GuiDragEvent) event);
            } else {
                boolean playerInventory = e.getInventory() instanceof PlayerInventory;

                ItemStack cursor = player.getInventory().getCursorItem();
                ItemStack clicked;

                if (click.slot() == -999) {
                    clicked = ItemStack.AIR;
                } else {
                    clicked = e.getInventory().getItemStack(click.slot());
                }

                event = new GuiClickEvent(
                        e,
                        gui,
                        user,
                        click.slot(),
                        playerInventory,
                        toWrapped(click, cursor, clicked, cursor.maxStackSize()),
                        toWrapped(click)
                );

                gui.onClick((GuiClickEvent) event);
            }

            if(event.isCancelled()) e.setCancelled(true);
        });

        eventNode.addListener(InventoryCloseEvent.class, e -> {
            if (!e.isFromClient()) return;

            AbstractInventory inventory = e.getInventory();
            MinestomGuiHolder holder = GuiUtils.getHolder(inventory);
            if (holder == null) return;

            MinestomUser user = MinestomGuiApi.get().user(e.getPlayer());

            MinestomGuiBase<?> gui = holder.gui();
            GuiCloseEvent event = new GuiCloseEvent(e, gui, user);
            gui.onClose(event);
        });

        handler.addChild(eventNode);
    }

    public static ClickType toWrapped(Click click) {
        return switch (click) {

            case Click.Left _ -> ClickType.LEFT;
            case Click.Right _ -> ClickType.RIGHT;
            case Click.Middle _ -> ClickType.MIDDLE;

            case Click.LeftShift _ -> ClickType.SHIFT_LEFT;
            case Click.RightShift _ -> ClickType.SHIFT_RIGHT;

            case Click.Double _ -> ClickType.DOUBLE_CLICK;

            case Click.DropSlot drop ->
                    drop.all() ? ClickType.CONTROL_DROP : ClickType.DROP;

            case Click.LeftDropCursor _,
                 Click.RightDropCursor _,
                 Click.MiddleDropCursor _ -> ClickType.DROP;

            case Click.HotbarSwap _ -> ClickType.NUMBER_KEY;

            case Click.OffhandSwap _ -> ClickType.SWAP_OFFHAND;

            default -> ClickType.UNKNOWN;
        };
    }

    public static InventoryAction toWrapped(
            Click click,
            ItemStack cursor,
            ItemStack slot,
            int maxStack
    ) {
        boolean cursorEmpty = cursor.isAir();
        boolean slotEmpty = slot.isAir();

        return switch (click) {

            case Click.Left _ -> {
                if (!cursorEmpty || slotEmpty) {
                    if (!cursorEmpty && slotEmpty) yield InventoryAction.PLACE_ALL;

                    if (!cursorEmpty) {
                        if (canStack(cursor, slot)) {
                            if (slot.amount() + cursor.amount() <= maxStack) {
                                yield InventoryAction.PLACE_ALL;
                            } else {
                                yield InventoryAction.PLACE_SOME;
                            }
                        } else {
                            yield InventoryAction.SWAP_WITH_CURSOR;
                        }
                    }

                    yield InventoryAction.NOTHING;
                } else {
                    yield InventoryAction.PICKUP_ALL;
                }
            }

            case Click.Right _ -> {
                if (cursorEmpty && !slotEmpty) yield InventoryAction.PICKUP_HALF;
                if (!cursorEmpty && slotEmpty) yield InventoryAction.PLACE_ONE;

                if (!cursorEmpty) {
                    if (canStack(cursor, slot)) {
                        if (slot.amount() < maxStack) {
                            yield InventoryAction.PLACE_ONE;
                        } else {
                            yield InventoryAction.NOTHING;
                        }
                    } else {
                        yield InventoryAction.SWAP_WITH_CURSOR;
                    }
                }

                yield InventoryAction.NOTHING;
            }

            case Click.LeftShift _, Click.RightShift _ ->
                    InventoryAction.MOVE_TO_OTHER_INVENTORY;

            case Click.Double _ ->
                    InventoryAction.COLLECT_TO_CURSOR;

            case Click.DropSlot drop ->
                    drop.all()
                            ? InventoryAction.DROP_ALL_SLOT
                            : InventoryAction.DROP_ONE_SLOT;

            case Click.LeftDropCursor _,
                 Click.RightDropCursor _,
                 Click.MiddleDropCursor _ ->
                    InventoryAction.DROP_ALL_CURSOR;

            case Click.HotbarSwap _, Click.OffhandSwap _ ->
                    InventoryAction.HOTBAR_SWAP;

            case Click.Middle _ ->
                    InventoryAction.CLONE_STACK;

            default -> InventoryAction.UNKNOWN;
        };
    }

    private static boolean canStack(ItemStack a, ItemStack b) {
        return a.isSimilar(b);
    }
}
