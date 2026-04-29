package com.jodexindustries.jguiwrapper.paper.api.gui.types.advanced;

import com.jodexindustries.jguiwrapper.api.gui.LoadType;
import com.jodexindustries.jguiwrapper.api.gui.types.advanced.AdvancedGui;
import com.jodexindustries.jguiwrapper.api.gui.types.advanced.AdvancedGuiItemController;
import com.jodexindustries.jguiwrapper.api.gui.types.advanced.GuiDataLoader;
import com.jodexindustries.jguiwrapper.api.gui.types.advanced.item.ItemHandler;
import com.jodexindustries.jguiwrapper.api.gui.types.advanced.registry.DataRegistry;
import com.jodexindustries.jguiwrapper.api.text.SerializerType;
import com.jodexindustries.jguiwrapper.api.user.User;
import com.jodexindustries.jguiwrapper.api.utils.Pair;
import com.jodexindustries.jguiwrapper.paper.api.gui.types.PaperGuiBase;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.*;
import java.util.function.Consumer;

public class PaperAdvancedGui extends PaperGuiBase<PaperAdvancedGui> implements AdvancedGui<PaperAdvancedGui> {

    private final Map<String, AdvancedGuiItemController<PaperAdvancedGui, ?>> keyMap = new HashMap<>();
    private final Map<Integer, AdvancedGuiItemController<PaperAdvancedGui, ?>> slotMap = new HashMap<>();
    private final Map<Class<?>, GuiDataLoader<PaperAdvancedGui>> loaderMap = new HashMap<>();

    public PaperAdvancedGui(int size, @NotNull Component title, @Nullable SerializerType defaultSerializer) {
        super(size, title, defaultSerializer);
    }

    public PaperAdvancedGui(@NotNull InventoryType type, @NotNull Component title, @Nullable SerializerType defaultSerializer) {
        super(type, title, defaultSerializer);
    }

    public void loadData(@NotNull HumanEntity player) {
        loadData(API.user(player));
    }

    @Override
    public void loadData(@NotNull User user) {
        for (GuiDataLoader<PaperAdvancedGui> value : loaderMap.values()) {
            value.load(this, user);
        }
    }

    @NotNull
    @Override
    public <T extends GuiDataLoader<PaperAdvancedGui>> Optional<T> getTypedLoader(@NotNull Class<T> clazz) {
        GuiDataLoader<PaperAdvancedGui> loader = getLoader0(clazz);
        return loader == null ? Optional.empty() : Optional.of(clazz.cast(loader));
    }

    @NotNull
    @Override
    public Optional<GuiDataLoader<PaperAdvancedGui>> getLoader(@NotNull Class<?> clazz) {
        return Optional.ofNullable(getLoader0(clazz));
    }

    private GuiDataLoader<PaperAdvancedGui> getLoader0(Class<?> clazz) {
        return loaderMap.get(clazz);
    }

    @Override
    public void registerLoader(@NotNull Key key) {
        API.getRegistry().getLoader(key).ifPresent(loader -> loaderMap.put(loader.getClass(), (GuiDataLoader<PaperAdvancedGui>) loader));
    }

    @Override
    public void registerLoader(@NotNull GuiDataLoader<PaperAdvancedGui> loader) {
        loaderMap.put(loader.getClass(), loader);
    }

    @UnmodifiableView
    @NotNull
    @Override
    public Collection<GuiDataLoader<PaperAdvancedGui>> getLoaders() {
        return Collections.unmodifiableCollection(loaderMap.values());
    }

    @Override
    public void registerItem(@NotNull String key, @NotNull Consumer<AdvancedGuiItemController.Builder<PaperAdvancedGui>> builderConsumer) {
        AdvancedGuiItemController.Builder<PaperAdvancedGui> builder = new AdvancedGuiItemController.Builder<>(this);
        builderConsumer.accept(builder);
        registerItem(key, builder.build());
    }

    @Override
    public void registerItem(@NotNull String key, @NotNull AdvancedGuiItemController<PaperAdvancedGui, ?> controller) throws IllegalArgumentException {
        if (keyMap.containsKey(key)) return;

        if (!controller.gui().equals(this)) {
            throw new IllegalArgumentException("Controller belongs to a different GUI instance");
        }

        controller.drawSlots();
        resolveControllerHandler(controller);
        keyMap.put(key, controller);
    }

    @NotNull
    @Override
    public AdvancedGuiItemController.Builder<PaperAdvancedGui> createController() {
        return new AdvancedGuiItemController.Builder<>(this);
    }

    @Override
    public void unregister(@NotNull String key) {
        AdvancedGuiItemController<PaperAdvancedGui, ?> controller = keyMap.remove(key);
        if (controller != null) {
            controller.clear();
            controller.slots().forEach(slotMap::remove);
        }
    }

    @NotNull
    @Override
    public Optional<AdvancedGuiItemController<PaperAdvancedGui, ?>> getController(@NotNull String key) {
        return Optional.ofNullable(keyMap.get(key));
    }

    @NotNull
    @Override
    public Optional<AdvancedGuiItemController<PaperAdvancedGui, ?>> getController(int slot) {
        return Optional.ofNullable(slotMap.get(slot));
    }

    @UnmodifiableView
    @NotNull
    @Override
    public Collection<AdvancedGuiItemController<PaperAdvancedGui, ?>> getControllers() {
        return Collections.unmodifiableCollection(keyMap.values());
    }

    @Override
    public void attach(@NotNull AdvancedGuiItemController<PaperAdvancedGui, ?> controller, int slot) {
        slotMap.put(slot, controller);
    }

    @Override
    public void detach(int slot) {
        slotMap.remove(slot);
    }

    private void resolveControllerHandler(@NotNull AdvancedGuiItemController<PaperAdvancedGui, ?> controller) {
        Key key = controller.itemHandlerKey();
        if (key == null) return;

        Optional<DataRegistry> registry = API.getRegistry().getRegistry(key.namespace());
        Optional<Pair<ItemHandler<?>, Class<?>>> pair =
                registry.flatMap(r -> r.getHandler(key.value()));
        controller.attachResolvedHandler(pair.orElse(null));
    }

    @Override
    public void loadItemHandlers(@NotNull LoadType loadType, @Nullable User user) {
        for (AdvancedGuiItemController<PaperAdvancedGui, ?> controller : keyMap.values()) {
            controller.tryInvokeResolvedHandler(loadType, user);
        }
    }
}
