package com.jodexindustries.jguiwrapper.paper.api.item;

import com.google.common.base.Preconditions;
import com.jodexindustries.jguiwrapper.paper.api.PaperGuiApi;
import com.jodexindustries.jguiwrapper.api.text.PlaceholderEngine;
import com.jodexindustries.jguiwrapper.api.text.SerializerType;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@SuppressWarnings({"unused", "UnusedReturnValue", "BooleanMethodIsAlwaysInverted"})
public class ItemWrapper implements Cloneable {

    private final SerializerType serializer;

    private ItemStack itemStack;

    private Material material;
    private Component displayName;
    private List<Component> lore;
    private Integer customModelData;
    private boolean enchanted;

    private boolean canUpdate = true;

    private boolean autoFlushUpdate;
    private PlaceholderEngine<OfflinePlayer> placeholderEngine;

    private boolean updated = true;

    public ItemWrapper(@NotNull final ItemStack itemStack, @Nullable SerializerType serializer) {
        this.itemStack = itemStack;
        this.material = itemStack.getType();
        this.serializer = serializer == null ? PaperGuiApi.get().defaultSerializer() : serializer;
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
        update((OfflinePlayer) null);
    }

    public void update(@Nullable HumanEntity entity) {
        update(((OfflinePlayer) entity));
    }

    public void update(@Nullable OfflinePlayer player) {
        if (!canUpdate) return;

        ItemMeta meta = this.itemStack.getItemMeta();
        updateMeta(meta, player);
        this.itemStack.setItemMeta(meta);
        this.itemStack.setType(material);
        updated = true;
    }

    protected void updateMeta(ItemMeta meta, @Nullable OfflinePlayer player) {
        if (meta == null) return;

        Component tempDisplayName = this.displayName;
        List<Component> tempLore = this.lore;

        if (placeholderEngine != null) {
            if (tempDisplayName != null) tempDisplayName = placeholderEngine.process(tempDisplayName, player);
            if (tempLore != null) tempLore = placeholderEngine.process(tempLore, player);
        }

        meta.displayName(tempDisplayName);
        meta.lore(tempLore);
        meta.setCustomModelData(customModelData);

        if (enchanted) {
            meta.addEnchant(Enchantment.LURE, 1, true);
        } else {
            meta.removeEnchant(Enchantment.LURE);
        }
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

    public final Integer customModelData() {
        return this.customModelData;
    }

    public final void enchanted(boolean enchanted) {
        this.enchanted = enchanted;
        flushUpdate();
    }

    public final boolean enchanted() {
        return this.enchanted;
    }

    public final void autoFlushUpdate(boolean autoFlushUpdate) {
        this.autoFlushUpdate = autoFlushUpdate;
    }

    public final boolean autoFlushUpdate() {
        return this.autoFlushUpdate;
    }

    public final void canUpdate(boolean canUpdate) {
        this.canUpdate = canUpdate;
    }

    public final boolean canUpdate() {
        return this.canUpdate;
    }

    public void placeholderEngine(PlaceholderEngine<OfflinePlayer> placeholderEngine) {
        this.placeholderEngine = placeholderEngine;
    }

    public PlaceholderEngine<OfflinePlayer> placeholderEngine() {
        return this.placeholderEngine;
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

    @Override
    public ItemWrapper clone() {
        try {
            ItemWrapper clone = (ItemWrapper) super.clone();
            clone.itemStack = this.itemStack.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public static class Builder {
        private final Material material;
        private SerializerType serializer = PaperGuiApi.get().defaultSerializer();
        private int amount = 1;
        private Component displayName;
        private List<Component> lore;
        private Integer customModelData;
        private boolean enchanted;
        private boolean autoFlushUpdate;
        private PlaceholderEngine<OfflinePlayer> placeholderEngine;

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

        public Builder enchanted(boolean enchanted) {
            this.enchanted = enchanted;
            return this;
        }

        public Builder autoFlushUpdate(boolean autoFlushUpdate) {
            this.autoFlushUpdate = autoFlushUpdate;
            return this;
        }

        public Builder placeholderEngine(PlaceholderEngine<OfflinePlayer> placeholderEngine) {
            this.placeholderEngine = placeholderEngine;
            return this;
        }

        public ItemWrapper build() {
            ItemWrapper wrapper = new ItemWrapper(material, amount, serializer);

            wrapper.displayName = displayName;
            wrapper.lore = lore;
            wrapper.customModelData = customModelData;
            wrapper.autoFlushUpdate = autoFlushUpdate;
            wrapper.enchanted = enchanted;
            wrapper.placeholderEngine = placeholderEngine;

            wrapper.update();

            return wrapper;
        }
    }

}
