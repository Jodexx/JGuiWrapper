package com.jodexindustries.jguiwrapper.api.item;

import com.jodexindustries.jguiwrapper.api.GuiApi;
import com.jodexindustries.jguiwrapper.api.text.PlaceholderEngine;
import com.jodexindustries.jguiwrapper.api.text.SerializerType;
import com.jodexindustries.jguiwrapper.api.user.User;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@SuppressWarnings({"unused", "BooleanMethodIsAlwaysInverted"})
public class ItemWrapper implements Cloneable {

    private String id;
    private int amount;
    private boolean canUpdate = true;
    private boolean autoFlushUpdate;
    private PlaceholderEngine placeholderEngine;
    private ItemMeta meta;

    private transient boolean updated = true;

    public ItemWrapper(@NotNull final String id, final int amount, @Nullable final SerializerType serializer) {
        this.id = id;
        this.amount = amount;
        this.meta = new ItemMeta(this, serializer);
    }

    public ItemWrapper(@NotNull final String id, final int amount) {
        this(id, amount, null);
    }

    public ItemWrapper(@NotNull final String id, @Nullable final SerializerType serializer) {
        this(id, 1, serializer);
    }

    public ItemWrapper(@NotNull final String id) {
        this(id, null);
    }

    public void update() {
        update(null);
    }

    public void update(@Nullable User user) {
        if (!canUpdate) return;

        // temporary components
        Component displayName = this.meta.displayName();
        List<Component> lore = this.meta.lore();
        if (placeholderEngine != null) {
            if (displayName != null) displayName = placeholderEngine.process(displayName, user);
            if (lore != null) lore = placeholderEngine.process(lore, user);
        }

        updateMeta(displayName, lore);
        updated = true;
    }

    protected void updateMeta(@Nullable Component displayName, @Nullable List<Component> lore) {}

    protected void flushUpdate() {
        if (autoFlushUpdate) {
            update();
        } else {
            updated = false;
        }
    }

    @NotNull
    public final String id() {
        return id;
    }

    public final void id(@NotNull String id) {
        this.id = id;
        flushUpdate();
    }

    public final int amount() {
        return amount;
    }

    public final void amount(int amount) {
        this.amount = amount;
        flushUpdate();
    }

    public final void canUpdate(boolean canUpdate) {
        this.canUpdate = canUpdate;
    }

    public final boolean canUpdate() {
        return this.canUpdate;
    }

    public final void autoFlushUpdate(boolean autoFlushUpdate) {
        this.autoFlushUpdate = autoFlushUpdate;
    }

    public final boolean autoFlushUpdate() {
        return this.autoFlushUpdate;
    }

    public void placeholderEngine(@Nullable PlaceholderEngine placeholderEngine) {
        this.placeholderEngine = placeholderEngine;
    }

    @UnknownNullability
    public PlaceholderEngine placeholderEngine() {
        return this.placeholderEngine;
    }

    @NotNull
    public ItemMeta meta() {
        return meta;
    }

    public void meta(@NotNull ItemMeta meta) {
        this.meta = meta;
    }

    public boolean isUpdated() {
        return updated;
    }

    public static Builder builder(@NotNull final String id) {
        return builder(id, null);
    }

    public static Builder builder(@NotNull final String id, @Nullable final SerializerType serializer) {
        return new Builder(id, serializer);
    }

    @Override
    public ItemWrapper clone() {
        try {
            ItemWrapper clone = (ItemWrapper) super.clone();
            clone.meta = meta.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public static class Builder extends BuilderBase<Builder, ItemWrapper> {

        private final String id;

        protected Builder(@NotNull String id, @Nullable SerializerType serializer) {
            super(serializer);

            this.id = id;
        }

        @Override
        public @NotNull ItemWrapper build(boolean update) {
            ItemWrapper wrapper = new ItemWrapper(id, amount, serializer);
            update(wrapper, update);

            return wrapper;
        }
    }

    public abstract static class BuilderBase<B, T> {
        protected SerializerType serializer;
        protected int amount = 1;
        protected Component displayName;
        protected List<Component> lore;
        protected Integer customModelData;
        protected boolean enchanted;
        protected boolean autoFlushUpdate;
        protected PlaceholderEngine placeholderEngine;

        protected BuilderBase(@Nullable SerializerType serializer) {
            this.serializer = serializer == null ? GuiApi.get().defaultSerializer() : serializer;
        }

        public B amount(int amount) {
            this.amount = amount;
            return self();
        }

        public B displayName(@Nullable Component displayName) {
            this.displayName = displayName;
            return self();
        }

        public B displayName(@Nullable String displayName) {
            Component component = displayName == null ? null : serializer.deserialize(displayName);
            return displayName(component);
        }

        public B lore(@NotNull Collection<String> lore) {
            return lore(lore.toArray(new String[0]));
        }

        public B lore(@NotNull String... lore) {
            List<Component> list = new ArrayList<>();

            for (String line : lore) {
                list.add(serializer.deserialize(line));
            }
            return lore(list);
        }

        public B lore(@Nullable List<Component> lore) {
            this.lore = lore;
            return self();
        }

        public B customModelData(@Nullable Integer customModelData) {
            this.customModelData = customModelData;
            return self();
        }

        public B enchanted(boolean enchanted) {
            this.enchanted = enchanted;
            return self();
        }

        public B autoFlushUpdate(boolean autoFlushUpdate) {
            this.autoFlushUpdate = autoFlushUpdate;
            return self();
        }

        public B placeholderEngine(@Nullable PlaceholderEngine placeholderEngine) {
            this.placeholderEngine = placeholderEngine;
            return self();
        }

        protected void update(ItemWrapper wrapper, boolean update) {
            wrapper.meta
                    .displayName(displayName)
                    .lore(lore)
                    .customModelData(customModelData)
                    .enchanted(enchanted);

            wrapper.placeholderEngine = placeholderEngine;
            wrapper.autoFlushUpdate = autoFlushUpdate;

            if (update) wrapper.update();
        }

        @NotNull
        public T build() {
            return build(true);
        }

        @NotNull
        public abstract T build(boolean update);

        @SuppressWarnings("unchecked")
        protected B self() {
            return (B) this;
        }
    }
}
