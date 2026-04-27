package com.jodexindustries.jguiwrapper.common.factory;

import com.jodexindustries.jguiwrapper.api.gui.Gui;
import com.jodexindustries.jguiwrapper.api.gui.factory.GuiCreator;
import com.jodexindustries.jguiwrapper.api.gui.factory.GuiFactory;
import com.jodexindustries.jguiwrapper.api.gui.factory.GuiOptions;
import com.jodexindustries.jguiwrapper.api.gui.factory.GuiType;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("unused")
public class RegistryGuiFactory implements GuiFactory {

    private final Map<String, GuiCreator<? extends Gui>> creators = new ConcurrentHashMap<>();

    @Override
    public @NotNull <T extends Gui> T create(@NotNull GuiType type, @NotNull GuiOptions options) {
        return create(type.name(), options);
    }

    @Override
    @SuppressWarnings("unchecked")
    public @NotNull <T extends Gui> T create(@NotNull String key, @NotNull GuiOptions options) {
        GuiCreator<? extends Gui> creator = creators.get(normalizeKey(key));
        if (creator == null) {
            throw new IllegalArgumentException("No GUI creator registered for key: " + key);
        }
        return (T) creator.create(options);
    }

    @Override
    public <T extends Gui> void register(@NotNull GuiType type, @NotNull GuiCreator<T> creator) {
        register(type.name(), creator);
    }

    @Override
    public <T extends Gui> void register(@NotNull String key, @NotNull GuiCreator<T> creator) {
        creators.put(normalizeKey(key), creator);
    }

    @Override
    public boolean unregister(@NotNull GuiType type) {
        return unregister(type.name());
    }

    @Override
    public boolean unregister(@NotNull String key) {
        return creators.remove(normalizeKey(key)) != null;
    }

    private static @NotNull String normalizeKey(@NotNull String key) {
        return key.toLowerCase(Locale.ROOT).trim();
    }
}
