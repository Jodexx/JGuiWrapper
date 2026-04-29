package com.jodexindustries.jguiwrapper.api.gui.factory;

import com.jodexindustries.jguiwrapper.api.text.SerializerType;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Generic options used by GUI factories.
 */
public final class GuiOptions {

    private final int size;
    private final Component title;
    private final SerializerType serializer;
    private final Map<String, Object> attributes;

    private GuiOptions(int size, @NotNull Component title, @NotNull SerializerType serializer, @NotNull Map<String, Object> attributes) {
        this.size = size;
        this.title = title;
        this.serializer = serializer;
        this.attributes = Map.copyOf(attributes);
    }

    public int size() {
        return size;
    }

    @NotNull
    public Component title() {
        return title;
    }

    @Nullable
    public SerializerType serializer() {
        return serializer;
    }

    @NotNull
    public Map<String, Object> attributes() {
        return attributes;
    }

    @SuppressWarnings("unchecked")
    public <T> @Nullable T attribute(@NotNull String key) {
        return (T) attributes.get(key);
    }

    @NotNull
    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private int size = 54;
        private Component title = Component.empty();
        private SerializerType serializer; // nullable
        private final Map<String, Object> attributes = new HashMap<>();

        private Builder() {
        }

        @NotNull
        public Builder size(int size) {
            this.size = size;
            return this;
        }

        @NotNull
        public Builder title(@NotNull Component title) {
            this.title = title;
            return this;
        }

        public Builder serializer(@Nullable SerializerType serializer) {
            this.serializer = serializer;
            return this;
        }

        @NotNull
        public Builder attribute(@NotNull String key, @Nullable Object value) {
            if (value == null) {
                this.attributes.remove(key);
            } else {
                this.attributes.put(key, value);
            }
            return this;
        }

        @NotNull
        public GuiOptions build() {
            return new GuiOptions(size, title, serializer, attributes);
        }
    }
}
