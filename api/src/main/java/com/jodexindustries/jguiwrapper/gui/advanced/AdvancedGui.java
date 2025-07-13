package com.jodexindustries.jguiwrapper.gui.advanced;

import com.jodexindustries.jguiwrapper.api.registry.GuiDataLoader;
import com.jodexindustries.jguiwrapper.gui.SimpleGui;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;

public class AdvancedGui extends SimpleGui {

    private final Map<String, GuiItemController> keyMap = new HashMap<>();
    private final Map<Key, GuiDataLoader> loaderMap = new HashMap<>();

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

    public void loadData(HumanEntity player) {
        for (GuiDataLoader value : loaderMap.values()) {
            value.load(player);
        }
    }

    public Optional<GuiDataLoader> getLoader(Key key) {
        return Optional.ofNullable(loaderMap.get(key));
    }

    public void registerLoader(@NotNull Key key) {
        API.getRegistry().getLoader(key).ifPresent(loader -> loaderMap.put(key, loader));
    }

    public void registerLoader(final @NotNull String namespace, final @NotNull String id) {
        Key key = Key.key(namespace, id);
        registerLoader(key);
    }

    public Collection<GuiDataLoader> getLoaders() {
        return Collections.unmodifiableCollection(loaderMap.values());
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

    public Optional<GuiItemController> getController(@NotNull String key) {
        return Optional.ofNullable(keyMap.get(key));
    }

    public Optional<GuiItemController> getController(int slot) {
        return keyMap.values().stream()
                .filter(controller -> controller.slots().contains(slot))
                .findFirst();
    }

    public Collection<GuiItemController> getControllers() {
        return Collections.unmodifiableCollection(keyMap.values());
    }

}
