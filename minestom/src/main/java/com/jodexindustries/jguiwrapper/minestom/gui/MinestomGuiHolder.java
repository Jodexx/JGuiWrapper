package com.jodexindustries.jguiwrapper.minestom.gui;

import com.jodexindustries.jguiwrapper.api.gui.GuiHolder;
import com.jodexindustries.jguiwrapper.api.item.ItemWrapper;
import com.jodexindustries.jguiwrapper.minestom.gui.types.MinestomGuiBase;
import com.jodexindustries.jguiwrapper.minestom.item.MinestomItemWrapper;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.tag.Tag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;
import org.jspecify.annotations.NonNull;

public class MinestomGuiHolder implements GuiHolder {

    public static final Tag<MinestomGuiHolder> GUI_HOLDER_TAG = Tag.Transient("jguiwrapper");

    private final MinestomGuiBase<?> gui;
    private final MinestomInventory inventory;

    public MinestomGuiHolder(MinestomGuiBase<?> gui) {
        this(gui, null);
    }

    public MinestomGuiHolder(MinestomGuiBase<?> gui, @Nullable InventoryType type) {
        this.gui = gui;
        this.inventory = type != null ? new MinestomInventory(type, gui.title()) : new MinestomInventory(convertFromSize(gui.size()), gui.title());
        this.inventory.setTag(GUI_HOLDER_TAG, this);
    }

    @NotNull
    public MinestomInventory getInventory() {
        return this.inventory;
    }

    @Override
    public @NotNull MinestomGuiBase<?> gui() {
        return this.gui;
    }

    @Override
    public void setItem(@Range(from = 0L, to = 53L) int slot, @NonNull ItemWrapper item) {
        this.inventory.setItemStack(slot, MinestomItemWrapper.wrap(item).itemStack());
    }

    @Override
    public void clear(@Range(from = 0L, to = 53L) int slot) {
        this.inventory.setItemStack(slot, ItemStack.AIR);
    }

    private static InventoryType convertFromSize(int size) {
        return switch (size) {
            case 9 -> InventoryType.CHEST_1_ROW;
            case 18 -> InventoryType.CHEST_2_ROW;
            case 27 -> InventoryType.CHEST_3_ROW;
            case 36 -> InventoryType.CHEST_4_ROW;
            case 45 -> InventoryType.CHEST_5_ROW;
            default -> InventoryType.CHEST_6_ROW;
        };
    }
}
