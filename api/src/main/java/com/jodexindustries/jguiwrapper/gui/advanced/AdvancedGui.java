package com.jodexindustries.jguiwrapper.gui.advanced;

import com.jodexindustries.jguiwrapper.gui.SimpleGui;
import net.kyori.adventure.text.Component;
import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

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

    public void registerItem(@NotNull String key, @NotNull Consumer<GuiItemController.Builder> builderConsumer) {
        GuiItemController.Builder builder = new GuiItemController.Builder(this);

        builderConsumer.accept(builder);

        registerItem(key, builder.build());
    }

    public void registerItem(@NotNull String key, @NotNull GuiItemController controller) {
        if (keyMap.containsKey(key)) return;

        keyMap.put(key, controller);
        controller.redraw();
    }

    public GuiItemController getController(@NotNull String key) {
        return keyMap.get(key);
    }

    public GuiItemController getController(int slot) {
        return keyMap.values().stream()
                .filter(controller -> controller.slots().contains(slot))
                .findFirst()
                .orElse(null);
    }

    public Collection<GuiItemController> getControllers() {
        return Collections.unmodifiableCollection(keyMap.values());
    }

}
