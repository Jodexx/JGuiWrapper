package com.jodexindustries.jguiwrapper.gui.advanced;

import com.jodexindustries.jguiwrapper.api.gui.handler.InventoryHandler;
import com.jodexindustries.jguiwrapper.api.item.ItemWrapper;
import com.jodexindustries.jguiwrapper.gui.SimpleGui;
import net.kyori.adventure.text.Component;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class AdvancedGui extends SimpleGui {

    private final Map<String, GuiItemController> keyMap = new HashMap<>();

    public AdvancedGui(@NotNull String title) {
        super(title);
    }

    public AdvancedGui(int size, @NotNull String title) {
        super(size, title);
    }

    public AdvancedGui(@NotNull Component title) {
        super(title);
    }

    public AdvancedGui(InventoryType type, @NotNull Component title) {
        super(type, title);
    }

    public AdvancedGui(int size, @NotNull Component title) {
        super(size, title);
    }

    public void registerItem(@NotNull String key, @NotNull ItemWrapper itemWrapper, int slot, @Nullable InventoryHandler<InventoryClickEvent> clickHandler) {
        registerItem(key, new GuiItemController(this, itemWrapper, slot, clickHandler));
    }

    public void registerItem(@NotNull String key, @NotNull GuiItemController controller) {
        keyMap.put(key, controller);
        controller.redraw();
    }

    public GuiItemController getController(@NotNull String key) {
        return keyMap.get(key);
    }

    public GuiItemController getController(int slot) {
        return keyMap.values().stream().filter(guiItemController -> guiItemController.getSlot() == slot).findFirst().orElse(null);
    }

    public void swapControllers(@NotNull GuiItemController a, @NotNull GuiItemController b) {
        GuiItemController.swap(a, b);
    }

}
