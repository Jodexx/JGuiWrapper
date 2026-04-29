package com.jodexindustries.jguiwrapper.minestom.gui.types.advanced;

import com.jodexindustries.jguiwrapper.api.gui.LoadType;
import com.jodexindustries.jguiwrapper.api.gui.types.advanced.AdvancedGui;
import com.jodexindustries.jguiwrapper.api.gui.types.advanced.AdvancedGuiItemController;
import com.jodexindustries.jguiwrapper.api.gui.types.advanced.GuiDataLoader;
import com.jodexindustries.jguiwrapper.api.gui.types.advanced.item.ItemHandler;
import com.jodexindustries.jguiwrapper.api.gui.types.advanced.registry.DataRegistry;
import com.jodexindustries.jguiwrapper.api.text.SerializerType;
import com.jodexindustries.jguiwrapper.api.user.User;
import com.jodexindustries.jguiwrapper.api.utils.Pair;
import com.jodexindustries.jguiwrapper.minestom.MinestomGuiApi;
import com.jodexindustries.jguiwrapper.minestom.gui.types.MinestomGuiBase;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.minestom.server.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public class MinestomAdvancedGui extends MinestomGuiBase<MinestomAdvancedGui> implements AdvancedGui<MinestomAdvancedGui> {

    private final Map<String, AdvancedGuiItemController<MinestomAdvancedGui, ?>> keyMap = new HashMap<>();
    private final Map<Integer, AdvancedGuiItemController<MinestomAdvancedGui, ?>> slotMap = new HashMap<>();
    private final Map<Class<?>, GuiDataLoader<MinestomAdvancedGui>> loaderMap = new HashMap<>();

    public MinestomAdvancedGui(int size, @NotNull Component title, @Nullable SerializerType defaultSerializer) {
        super(size, title, defaultSerializer);
    }

    public MinestomAdvancedGui(@NotNull InventoryType type, @NotNull Component title, @Nullable SerializerType defaultSerializer) {
        super(type, title, defaultSerializer);
    }

    @Override
    public void loadData(@NotNull User user) {
        for (GuiDataLoader<MinestomAdvancedGui> value : loaderMap.values()) {
            value.load(this, user);
        }
    }

    @Override
    public void registerLoader(@NotNull Key key) {
        MinestomGuiApi.get().getRegistry()
                .getLoader(key)
                .ifPresent(loader -> loaderMap.put(loader.getClass(), (GuiDataLoader<MinestomAdvancedGui>) loader));
    }

    @Override
    public void registerLoader(@NotNull GuiDataLoader<MinestomAdvancedGui> loader) {
        loaderMap.put(loader.getClass(), loader);
    }

    @Override
    public @NotNull Optional<GuiDataLoader<MinestomAdvancedGui>> getLoader(@NotNull Class<?> clazz) {
        return Optional.ofNullable(loaderMap.get(clazz));
    }

    @Override
    public @NotNull <T extends GuiDataLoader<MinestomAdvancedGui>> Optional<T> getTypedLoader(@NotNull Class<T> clazz) {
        GuiDataLoader<MinestomAdvancedGui> loader = loaderMap.get(clazz);
        return loader == null ? Optional.empty() : Optional.of(clazz.cast(loader));
    }

    @Override
    public @UnmodifiableView @NotNull Collection<GuiDataLoader<MinestomAdvancedGui>> getLoaders() {
        return Collections.unmodifiableCollection(loaderMap.values());
    }

    @Override
    public void registerItem(@NotNull String key, @NotNull Consumer<AdvancedGuiItemController.Builder<MinestomAdvancedGui>> builderConsumer) {
        AdvancedGuiItemController.Builder<MinestomAdvancedGui> builder = new AdvancedGuiItemController.Builder<>(this);
        builderConsumer.accept(builder);
        registerItem(key, builder.build());
    }

    @Override
    public void registerItem(@NotNull String key, @NotNull AdvancedGuiItemController<MinestomAdvancedGui, ?> controller) throws IllegalArgumentException {
        if (keyMap.containsKey(key)) return;

        if (!controller.gui().equals(this)) {
            throw new IllegalArgumentException("Controller belongs to a different GUI instance");
        }

        controller.drawSlots();
        resolveControllerHandler(controller);
        keyMap.put(key, controller);
    }

    @Override
    public @NotNull AdvancedGuiItemController.Builder<MinestomAdvancedGui> createController() {
        return new AdvancedGuiItemController.Builder<>(this);
    }

    @Override
    public void unregister(@NotNull String key) {
        AdvancedGuiItemController<MinestomAdvancedGui, ?> controller = keyMap.remove(key);
        if (controller != null) {
            controller.clear();
            controller.slots().forEach(slotMap::remove);
        }
    }

    @Override
    public @NotNull Optional<AdvancedGuiItemController<MinestomAdvancedGui, ?>> getController(@NotNull String key) {
        return Optional.ofNullable(keyMap.get(key));
    }

    @Override
    public @NotNull Optional<AdvancedGuiItemController<MinestomAdvancedGui, ?>> getController(int slot) {
        return Optional.ofNullable(slotMap.get(slot));
    }

    @Override
    public @UnmodifiableView @NotNull Collection<AdvancedGuiItemController<MinestomAdvancedGui, ?>> getControllers() {
        return Collections.unmodifiableCollection(keyMap.values());
    }

    @Override
    public void loadItemHandlers(@NotNull LoadType loadType, @Nullable User user) {
        for (AdvancedGuiItemController<MinestomAdvancedGui, ?> controller : keyMap.values()) {
            controller.tryInvokeResolvedHandler(loadType, user);
        }
    }

    @Override
    public void attach(@NotNull AdvancedGuiItemController<MinestomAdvancedGui, ?> controller, int slot) {
        slotMap.put(slot, controller);
    }

    @Override
    public void detach(int slot) {
        slotMap.remove(slot);
    }

    private void resolveControllerHandler(@NotNull AdvancedGuiItemController<MinestomAdvancedGui, ?> controller) {
        Key key = controller.itemHandlerKey();
        if (key == null) return;

        Optional<DataRegistry> registry = MinestomGuiApi.get().getRegistry().getRegistry(key.namespace());
        Optional<Pair<ItemHandler<?>, Class<?>>> pair =
                registry.flatMap(r -> r.getHandler(key.value()));
        controller.attachResolvedHandler(pair.orElse(null));
    }
}
