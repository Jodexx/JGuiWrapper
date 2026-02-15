package com.jodexindustries.jguiwrapper.gui.advanced;

import com.jodexindustries.jguiwrapper.api.gui.GuiDataLoader;
import com.jodexindustries.jguiwrapper.api.text.SerializerType;
import com.jodexindustries.jguiwrapper.gui.SimpleGui;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.*;
import java.util.function.Consumer;

@SuppressWarnings({"unused"})
public class AdvancedGui extends SimpleGui {

    private final Map<String, GuiItemController> keyMap = new HashMap<>();
    public final Map<Integer, GuiItemController> slotMap = new HashMap<>();
    private final Map<Class<? extends GuiDataLoader>, GuiDataLoader> loaderMap = new HashMap<>();

    /**
     * Constructs a GUI with the default size (54) and a string title.
     *
     * @param title The GUI title as a string
     */
    public AdvancedGui(@NotNull String title) {
        super(title);
    }

    /**
     * Constructs a GUI with a specific size and string title.
     *
     * @param size  The inventory size
     * @param title The GUI title as a string
     */
    public AdvancedGui(int size, @NotNull String title) {
        super(size, title);
    }

    /**
     * Constructs a GUI with the default CHEST type and a component title.
     *
     * @param title The GUI title as a Component
     */
    public AdvancedGui(@NotNull Component title) {
        super(title);
    }

    /**
     * Constructs a GUI with a specific inventory type and component title.
     *
     * @param type  The inventory type
     * @param title The GUI title as a Component
     */
    public AdvancedGui(@NotNull InventoryType type, @NotNull Component title) {
        super(type, title);
    }

    /**
     * Constructs a GUI with a specific size, optional type, and component title.
     *
     * @param size  The inventory size
     * @param title The GUI title as a Component
     */
    public AdvancedGui(int size, @NotNull Component title) {
        super(size, title);
    }

    /**
     * Constructs a GUI with a specific inventory type and component title with serializer.
     *
     * @param type              The inventory type.
     * @param title             The GUI title as a {@link Component}.
     * @param defaultSerializer The default serializer used for converting between plain strings and {@link Component}
     *                          instances. If {@code null}, the
     *                          {@link #defaultSerializer} will be used.
     */
    public AdvancedGui(@NotNull InventoryType type, @NotNull Component title, @Nullable SerializerType defaultSerializer) {
        super(type, title, defaultSerializer);
    }

    public void loadData(@NotNull HumanEntity player) {
        for (GuiDataLoader value : loaderMap.values()) {
            value.load(this, player);
        }
    }

    @NotNull
    public <T extends GuiDataLoader> Optional<T> getTypedLoader(@NotNull Class<T> clazz) {
        return Optional.of(clazz.cast(getLoader0(clazz)));
    }

    @NotNull
    public Optional<GuiDataLoader> getLoader(@NotNull Class<?> clazz) {
        return Optional.of(getLoader0(clazz));
    }

    private GuiDataLoader getLoader0(Class<?> clazz) {
        return loaderMap.get(clazz);
    }

    public void registerLoader(@NotNull Key key) {
        API.getRegistry().getLoader(key).ifPresent(loader -> loaderMap.put(loader.getClass(), loader));
    }

    public void registerLoader(@NotNull GuiDataLoader loader) {
        loaderMap.put(loader.getClass(), loader);
    }

    @UnmodifiableView
    @NotNull
    public Collection<GuiDataLoader> getLoaders() {
        return Collections.unmodifiableCollection(loaderMap.values());
    }

    public void registerItem(@NotNull String key, @NotNull Consumer<GuiItemController.Builder> builderConsumer) {
        GuiItemController.Builder builder = new GuiItemController.Builder(this);

        builderConsumer.accept(builder);

        registerItem(key, builder.build());
    }

    public void registerItem(@NotNull String key, @NotNull GuiItemController controller) throws IllegalArgumentException {
        if (keyMap.containsKey(key)) return;

        if (!controller.gui().equals(this)) {
            throw new IllegalArgumentException("Controller belongs to a different GUI instance");
        }

        controller.drawSlots();

        keyMap.put(key, controller);
    }

    @NotNull
    public GuiItemController.Builder createController() {
        return new GuiItemController.Builder(this);
    }

    public void unregister(@NotNull String key) {
        GuiItemController controller = keyMap.remove(key);
        if (controller != null) {
            controller.clear();
            controller.slots().forEach(slotMap::remove);
        }
    }

    @NotNull
    public Optional<GuiItemController> getController(@NotNull String key) {
        return Optional.ofNullable(keyMap.get(key));
    }

    @NotNull
    public Optional<GuiItemController> getController(int slot) {
        return Optional.ofNullable(slotMap.get(slot));
    }

    @UnmodifiableView
    @NotNull
    public Collection<GuiItemController> getControllers() {
        return Collections.unmodifiableCollection(keyMap.values());
    }
}
