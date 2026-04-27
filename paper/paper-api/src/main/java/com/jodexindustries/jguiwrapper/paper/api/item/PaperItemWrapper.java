package com.jodexindustries.jguiwrapper.paper.api.item;

import com.google.common.base.Preconditions;
import com.jodexindustries.jguiwrapper.api.item.ItemWrapper;
import com.jodexindustries.jguiwrapper.api.text.SerializerType;
import com.jodexindustries.jguiwrapper.paper.api.PaperGuiApi;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@SuppressWarnings("unused")
public class PaperItemWrapper extends ItemWrapper {

    private ItemStack itemStack;
    private Material material;

    public PaperItemWrapper(@NotNull final ItemStack itemStack, @Nullable final SerializerType serializer) {
        super(itemStack.getType().name(), itemStack.getAmount(), serializer);

        this.itemStack = itemStack;
        this.material = itemStack.getType();
    }

    public PaperItemWrapper(@NotNull final ItemStack itemStack) {
        this(itemStack, null);
    }

    public PaperItemWrapper(@NotNull final Material material, final int amount, @Nullable final SerializerType serializer) {
        this(new ItemStack(material, amount), serializer);
    }

    public PaperItemWrapper(@NotNull final Material material, final int amount) {
        this(new ItemStack(material, amount));
    }

    public PaperItemWrapper(@NotNull final Material material, @Nullable final SerializerType serializer) {
        this(material, 1, serializer);
    }

    public PaperItemWrapper(@NotNull final Material material) {
        this(material, 1);
    }

    public void update(@Nullable final HumanEntity entity) {
        update(((Player) entity));
    }

    public void update(@Nullable final Player player) {
        update(player != null ? PaperGuiApi.get().user(player) : null);
    }

    @Override
    protected void updateMeta(final @Nullable Component displayName, @Nullable final List<Component> lore) {
        ItemMeta meta = this.itemStack.getItemMeta();
        if (meta != null) {
            meta.displayName(displayName);
            meta.lore(lore);
            meta.setCustomModelData(this.meta().customModelData());
            if (this.meta().enchanted()) {
                meta.addEnchant(Enchantment.LURE, 1, true);
            } else {
                meta.removeEnchant(Enchantment.LURE);
            }
        }

        this.itemStack.setItemMeta(meta);
        this.itemStack.setType(material);
    }

    @NotNull
    public final ItemStack itemStack() {
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
        Preconditions.checkArgument(material != null, "Material cannot be null");
        this.material = material;
        flushUpdate();
    }

    public static Builder builder(@NotNull final Material material) {
        return builder(material, null);
    }

    @NotNull
    public static PaperItemWrapper wrap(@NotNull ItemWrapper itemWrapper) {
        if (itemWrapper instanceof PaperItemWrapper paperItemWrapper) {
            return paperItemWrapper;
        }

        Material material = Material.matchMaterial(itemWrapper.id());
        if (material == null) {
            throw new IllegalArgumentException("Cannot adapt ItemWrapper to PaperItemWrapper: unknown material id '" + itemWrapper.id() + "'");
        }

        PaperItemWrapper adapted = new PaperItemWrapper(material, itemWrapper.amount());
        adapted.meta(itemWrapper.meta());
        adapted.placeholderEngine(itemWrapper.placeholderEngine());
        adapted.canUpdate(itemWrapper.canUpdate());
        adapted.autoFlushUpdate(itemWrapper.autoFlushUpdate());
        adapted.update();
        return adapted;
    }

    public static Builder builder(@NotNull final Material material, @Nullable final SerializerType serializer) {
        Preconditions.checkArgument(material != null, "Material cannot be null");
        return new Builder(material, serializer);
    }

    @Override
    public PaperItemWrapper clone() {
        PaperItemWrapper clone = (PaperItemWrapper) super.clone();
        clone.itemStack = this.itemStack.clone();
        return clone;
    }

    public static class Builder extends BuilderBase<Builder, PaperItemWrapper> {
        private final Material material;

        private Builder(@NotNull final Material material, @Nullable final SerializerType serializer) {
            super(serializer);

            this.material = material;
        }

        @Override
        public @NotNull PaperItemWrapper build(boolean update) {
            PaperItemWrapper wrapper = new PaperItemWrapper(material, amount, serializer);
            update(wrapper, update);

            return wrapper;
        }
    }

}
