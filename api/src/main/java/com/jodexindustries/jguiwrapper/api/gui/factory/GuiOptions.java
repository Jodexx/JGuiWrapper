package com.jodexindustries.jguiwrapper.api.gui.factory;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Generic options used by GUI factories.
 */
@SuppressWarnings("unused")
public final class GuiOptions {

    private final int size;
    private final Component title;
    private final Map<String, Object> attributes;

    private GuiOptions(int size, @NotNull Component title, @NotNull Map<String, Object> attributes) {
        this.size = size;
        this.title = title;
        this.attributes = Map.copyOf(attributes);
    }

    public int size() {
        return size;
    }

    @NotNull
    public Component title() {
        return title;
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
            return new GuiOptions(size, title, attributes);
        }
    }
}
