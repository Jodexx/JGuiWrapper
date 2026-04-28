package com.jodexindustries.jguiwrapper.minestom.item;

import com.jodexindustries.jguiwrapper.api.item.ItemWrapper;
import com.jodexindustries.jguiwrapper.api.text.SerializerType;
import net.kyori.adventure.text.Component;
import net.minestom.server.component.DataComponents;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.component.EnchantmentList;
import net.minestom.server.item.component.TooltipDisplay;
import net.minestom.server.item.enchant.Enchantment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public class MinestomItemWrapper extends ItemWrapper {

    private ItemStack itemStack;
    private Material material;

    public MinestomItemWrapper(@NotNull final ItemStack itemStack, @Nullable final SerializerType serializer) {
        super(itemStack.material().name(), itemStack.amount(), serializer);

        this.itemStack = itemStack;
        this.material = itemStack.material();
    }

    public MinestomItemWrapper(@NotNull final ItemStack itemStack) {
        this(itemStack, null);
    }

    public MinestomItemWrapper(@NotNull final Material material, final int amount, @Nullable final SerializerType serializer) {
        this(ItemStack.of(material, amount), serializer);
    }

    public MinestomItemWrapper(@NotNull final Material material, final int amount) {
        this(material, amount, null);
    }

    public MinestomItemWrapper(@NotNull final Material material, @Nullable final SerializerType serializer) {
        this(material, 1, serializer);
    }

    public MinestomItemWrapper(@NotNull final Material material) {
        this(material, null);
    }

    @NotNull
    public ItemStack itemStack() {
        return itemStack;
    }

    public final void itemStack(@NotNull final ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    @NotNull
    public final Material material() {
        return material;
    }

    public final void material(@NotNull final Material material) {
        this.material = material;
        flushUpdate();
    }

    @Override
    protected void updateMeta(final @Nullable Component displayName, @Nullable final List<Component> lore) {
        ItemStack.Builder builder = itemStack.builder();

        if (displayName != null) builder.customName(displayName);
        if (lore != null) builder.lore(lore);

        // after 1.21.4 is now deprecated
        Integer oldModelData = this.meta().customModelData();
        Float i = oldModelData != null ? oldModelData.floatValue() : null;
        if (i != null) builder.customModelData(List.of(i), List.of(), List.of(), List.of());

        if (this.meta().enchanted()) {
            // to improve?
            builder.set(DataComponents.TOOLTIP_DISPLAY, new TooltipDisplay(false, Set.of(DataComponents.ENCHANTMENTS)));
            builder.set(DataComponents.ENCHANTMENTS, new EnchantmentList(Enchantment.LURE, 1));
        } else {
            builder.remove(DataComponents.ENCHANTMENTS);
        }

        builder.material(material);
        this.itemStack = builder.build();
    }

    @NotNull
    public static MinestomItemWrapper wrap(@NotNull ItemWrapper itemWrapper) {
        if (itemWrapper instanceof MinestomItemWrapper minestomItemWrapper) {
            return minestomItemWrapper;
        }

        Material material = Material.fromKey(itemWrapper.id());
        if (material == null) {
            throw new IllegalArgumentException("Cannot adapt ItemWrapper to MinestomItemWrapper: unknown material id '" + itemWrapper.id() + "'");
        }

        MinestomItemWrapper adapted = new MinestomItemWrapper(material, itemWrapper.amount());
        adapted.meta(itemWrapper.meta());
        adapted.placeholderEngine(itemWrapper.placeholderEngine());
        adapted.canUpdate(itemWrapper.canUpdate());
        adapted.autoFlushUpdate(itemWrapper.autoFlushUpdate());
        if (itemWrapper.isUpdated()) adapted.update();
        return adapted;
    }

    public static Builder builder(@NotNull final Material material) {
        return builder(material, null);
    }

    public static Builder builder(@NotNull final Material material, @Nullable final SerializerType serializer) {
        return new Builder(material, serializer);
    }

    @Override
    public MinestomItemWrapper clone() {
//      ItemStack in Minestom is immutable, so shallow clone is sufficient
        return (MinestomItemWrapper) super.clone();
    }

    public static class Builder extends BuilderBase<Builder, MinestomItemWrapper> {
        private final Material material;

        private Builder(@NotNull final Material material, @Nullable final SerializerType serializer) {
            super(serializer);

            this.material = material;
        }

        @Override
        public @NotNull MinestomItemWrapper build(boolean update) {
            MinestomItemWrapper wrapper = new MinestomItemWrapper(material, amount, serializer);
            update(wrapper, update);

            return wrapper;
        }
    }
}
