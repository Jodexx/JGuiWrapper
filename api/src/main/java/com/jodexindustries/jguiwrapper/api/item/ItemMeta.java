package com.jodexindustries.jguiwrapper.api.item;

import com.jodexindustries.jguiwrapper.api.GuiApi;
import com.jodexindustries.jguiwrapper.api.text.SerializerType;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public class ItemMeta implements Cloneable {

    private ItemWrapper item;
    private SerializerType serializer;

    private Component displayName;
    private List<Component> lore;
    private Integer customModelData;
    private boolean enchanted;

    public ItemMeta(@Nullable ItemWrapper item, @Nullable SerializerType serializer) {
        this.item = item;
        this.serializer = serializer == null ? GuiApi.get().defaultSerializer() : serializer;
    }

    public ItemMeta(@Nullable ItemWrapper item) {
        this(item, null);
    }

    public ItemMeta(@Nullable SerializerType serializer) {
        this(null, serializer);
    }

    public ItemMeta() {
        this(null, null);
    }

    @Nullable
    public ItemWrapper item() {
        return item;
    }

    public ItemMeta item(@Nullable ItemWrapper item) {
        this.item = item;
        return this;
    }

    @NotNull
    public SerializerType serializer() {
        return serializer;
    }

    public ItemMeta serializer(@NotNull SerializerType serializer) {
        this.serializer = serializer;
        return this;
    }

    public Component displayName() {
        return displayName;
    }

    public ItemMeta displayName(Component displayName) {
        this.displayName = displayName;
        flushUpdate();
        return this;
    }

    public ItemMeta displayName(String displayName) {
        Component component = displayName == null ? null : serializer.deserialize(displayName);
        return displayName(component);
    }

    public List<Component> lore() {
        return lore;
    }

    public ItemMeta lore(List<Component> lore) {
        this.lore = lore;
        flushUpdate();
        return this;
    }

    public ItemMeta lore(@NotNull Collection<String> lore) {
        return lore(lore.toArray(new String[0]));
    }

    public ItemMeta lore(@NotNull String... lore) {
        List<Component> list = new ArrayList<>();

        for (String line : lore) {
            list.add(serializer.deserialize(line));
        }
        return lore(list);
    }

    @Nullable
    public Integer customModelData() {
        return customModelData;
    }

    public ItemMeta customModelData(@Nullable Integer customModelData) {
        this.customModelData = customModelData;
        flushUpdate();
        return this;
    }

    public boolean enchanted() {
        return enchanted;
    }

    public ItemMeta enchanted(boolean enchanted) {
        this.enchanted = enchanted;
        flushUpdate();
        return this;
    }

    private void flushUpdate() {
        if (item == null) return;

        item.flushUpdate();
    }

    @Override
    public ItemMeta clone() {
        try {
            ItemMeta clone = (ItemMeta) super.clone();
            clone.lore = lore == null ? null : new ArrayList<>(lore);
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
