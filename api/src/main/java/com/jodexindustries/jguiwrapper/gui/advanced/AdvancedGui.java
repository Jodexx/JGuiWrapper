package com.jodexindustries.jguiwrapper.gui.advanced;

import com.jodexindustries.jguiwrapper.api.gui.GuiDataLoader;
import com.jodexindustries.jguiwrapper.gui.SimpleGui;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.*;
import java.util.function.Consumer;

@SuppressWarnings({"unused"})
public class AdvancedGui extends SimpleGui {

    private final Map<String, GuiItemController> keyMap = new HashMap<>();
    public final Map<Integer, GuiItemController> slotMap = new HashMap<>();
    private final Map<Class<? extends GuiDataLoader>, GuiDataLoader> loaderMap = new HashMap<>();

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
            value.load(this, player);
        }
    }

    public <T extends GuiDataLoader> Optional<T> getTypedLoader(Class<T> clazz) {
        return Optional.of(clazz.cast(getLoader0(clazz)));
    }

    public Optional<GuiDataLoader> getLoader(Class<?> clazz) {
        return Optional.of(getLoader0(clazz));
    }

    private GuiDataLoader getLoader0(Class<?> clazz) {
        return loaderMap.get(clazz);
    }

    public void registerLoader(@NotNull Key key) {
        API.getRegistry().getLoader(key).ifPresent(loader -> loaderMap.put(loader.getClass(), loader));
    }

    public void registerLoader(GuiDataLoader loader) {
        loaderMap.put(loader.getClass(), loader);
    }

    @UnmodifiableView
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

    public void unregister(String key) {
        GuiItemController controller = keyMap.remove(key);
        if (controller != null) {
            controller.slots().forEach(slotMap::remove);
        }
    }

    public Optional<GuiItemController> getController(@NotNull String key) {
        return Optional.ofNullable(keyMap.get(key));
    }

    public Optional<GuiItemController> getController(int slot) {
        return Optional.ofNullable(slotMap.get(slot));
    }

    @UnmodifiableView
    public Collection<GuiItemController> getControllers() {
        return Collections.unmodifiableCollection(keyMap.values());
    }

}
