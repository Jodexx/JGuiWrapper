package com.jodexindustries.jguiwrapper.api.gui.types.advanced;

import com.jodexindustries.jguiwrapper.api.gui.types.SimpleGui;
import com.jodexindustries.jguiwrapper.api.gui.LoadType;
import com.jodexindustries.jguiwrapper.api.user.User;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Consumer;

public interface AdvancedGui<G extends SimpleGui<G> & AdvancedGui<G>> {

    void loadData(@NotNull User user);

    void registerLoader(@NotNull Key key);

    void registerLoader(@NotNull GuiDataLoader<G> loader);

    @NotNull
    Optional<GuiDataLoader<G>> getLoader(@NotNull Class<?> clazz);

    @NotNull
    <T extends GuiDataLoader<G>> Optional<T> getTypedLoader(@NotNull Class<T> clazz);

    @UnmodifiableView
    @NotNull
    Collection<GuiDataLoader<G>> getLoaders();

    void registerItem(@NotNull String key, @NotNull Consumer<AdvancedGuiItemController.Builder<G>> builderConsumer);

    void registerItem(@NotNull String key, @NotNull AdvancedGuiItemController<G, ?> controller) throws IllegalArgumentException;

    @NotNull
    AdvancedGuiItemController.Builder<G> createController();

    void unregister(@NotNull String key);

    @NotNull
    Optional<AdvancedGuiItemController<G, ?>> getController(@NotNull String key);

    @NotNull
    Optional<AdvancedGuiItemController<G, ?>> getController(int slot);

    @UnmodifiableView
    @NotNull
    Collection<AdvancedGuiItemController<G, ?>> getControllers();

    void loadItemHandlers(@NotNull LoadType loadType, @Nullable User user);

    void attach(@NotNull AdvancedGuiItemController<G, ?> controller, int slot);

    void detach(int slot);
}

