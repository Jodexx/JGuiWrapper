package com.jodexindustries.jguiwrapper.api.item;

import com.google.common.base.Preconditions;
import com.jodexindustries.jguiwrapper.api.text.SerializerType;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@SuppressWarnings({"unused", "UnusedReturnValue", "BooleanMethodIsAlwaysInverted"})
public class ItemWrapper {

    private final SerializerType serializer;

    private ItemStack itemStack;

    private Material material;
    private Component displayName;
    private List<Component> lore;
    private Integer customModelData;

    private boolean canUpdate = true;

    private boolean autoFlushUpdate;

    private boolean updated = true;

    public ItemWrapper(@NotNull final ItemStack itemStack, @Nullable SerializerType serializer) {
        this.itemStack = itemStack;
        this.material = itemStack.getType();
        this.serializer = serializer == null ? SerializerType.LEGACY : serializer;
    }

    public ItemWrapper(@NotNull final ItemStack itemStack) {
        this(itemStack, null);
    }

    public ItemWrapper(@NotNull final Material material, final int amount, @Nullable SerializerType serializer) {
        this(new ItemStack(material, amount), serializer);
    }

    public ItemWrapper(@NotNull final Material material, final int amount) {
        this(new ItemStack(material, amount));
    }

    public ItemWrapper(@NotNull final Material material, @Nullable SerializerType serializer) {
        this(material, 1, serializer);
    }

    public ItemWrapper(@NotNull final Material material) {
        this(material, 1);
    }

    public void update() {
        if (!canUpdate) return;

        ItemMeta meta = this.itemStack.getItemMeta();
        updateMeta(meta);
        this.itemStack.setItemMeta(meta);
        this.itemStack.setType(material);
        updated = true;
    }

    protected void updateMeta(ItemMeta meta) {
        if (meta == null) return;

        meta.displayName(displayName);
        meta.lore(lore);
        meta.setCustomModelData(customModelData);
    }

    protected void flushUpdate() {
        if (autoFlushUpdate) {
            update();
        } else {
            updated = false;
        }
    }

    @NotNull
    public final ItemStack itemStack() {
        return itemStack;
    }

    public final void itemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    @NotNull
    public final Material material() {
        return material;
    }

    public final void material(@NotNull Material material) {
        Preconditions.checkArgument(material != null, "Material cannot be null");
        this.material = material;
        flushUpdate();
    }

    public final void displayName(@Nullable Component displayName) {
        this.displayName = displayName;
        flushUpdate();
    }

    public final void displayName(@Nullable String displayName) {
        Component component = displayName == null ? null : serializer.deserialize(displayName);
        displayName(component);
    }

    public final Component displayName() {
        return this.displayName;
    }

    public final void lore(@NotNull Collection<String> lore) {
        lore(lore.toArray(new String[0]));
    }

    public final void lore(@NotNull String... lore) {
        List<Component> list = new ArrayList<>();

        for (String line : lore) {
            list.add(serializer.deserialize(line));
        }
        lore(list);
    }

    public final void lore(@Nullable List<Component> lore) {
        this.lore = lore;
        flushUpdate();
    }

    public final List<Component> lore() {
        return this.lore;
    }

    public final void customModelData(@Nullable Integer data) {
        this.customModelData = data;
        flushUpdate();
    }

    public Integer customModelData() {
        return customModelData;
    }

    public void autoFlushUpdate(boolean autoFlushUpdate) {
        this.autoFlushUpdate = autoFlushUpdate;
    }

    public boolean autoFlushUpdate() {
        return autoFlushUpdate;
    }

    public void canUpdate(boolean canUpdate) {
        this.canUpdate = canUpdate;
    }

    public boolean canUpdate() {
        return canUpdate;
    }

    public static Builder builder(@NotNull Material material) {
        return builder(material, null);
    }

    public static Builder builder(@NotNull Material material, @Nullable SerializerType serializer) {
        Preconditions.checkArgument(material != null, "Material cannot be null");
        return new Builder(material, serializer);
    }

    public boolean isUpdated() {
        return updated;
    }

    public static class Builder {
        private final Material material;
        private SerializerType serializer = SerializerType.LEGACY;
        private int amount = 1;
        private Component displayName;
        private List<Component> lore;
        private Integer customModelData;
        private boolean autoFlushUpdate;

        private Builder(@NotNull Material material, @Nullable SerializerType serializer) {
            this.material = material;

            if (serializer != null) this.serializer = serializer;
        }

        public Builder amount(int amount) {
            this.amount = amount;
            return this;
        }

        public Builder displayName(@Nullable Component displayName) {
            this.displayName = displayName;
            return this;
        }

        public Builder displayName(@Nullable String displayName) {
            Component component = displayName == null ? null : serializer.deserialize(displayName);
            return displayName(component);
        }

        public Builder lore(@NotNull Collection<String> lore) {
            return lore(lore.toArray(new String[0]));
        }

        public Builder lore(@NotNull String... lore) {
            List<Component> list = new ArrayList<>();

            for (String line : lore) {
                list.add(serializer.deserialize(line));
            }
            return lore(list);
        }

        public Builder lore(@Nullable List<Component> lore) {
            this.lore = lore;
            return this;
        }

        public Builder customModelData(@Nullable Integer customModelData) {
            this.customModelData = customModelData;
            return this;
        }

        public Builder autoFlushUpdate(boolean autoFlushUpdate) {
            this.autoFlushUpdate = autoFlushUpdate;
            return this;
        }

        public ItemWrapper build() {
            ItemWrapper wrapper = new ItemWrapper(material, amount, serializer);

            wrapper.displayName = displayName;
            wrapper.lore = lore;
            wrapper.customModelData = customModelData;
            wrapper.autoFlushUpdate = autoFlushUpdate;

            wrapper.update();

            return wrapper;
        }
    }

}
