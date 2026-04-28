package com.jodexindustries.jguiwrapper.minestom.gui.types;

import com.jodexindustries.jguiwrapper.api.gui.types.advanced.AdvancedGuiItemController;
import com.jodexindustries.jguiwrapper.api.text.SerializerType;
import net.kyori.adventure.text.Component;
import net.minestom.server.inventory.InventoryType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

@ApiStatus.Experimental
public class MinestomPaginatedGui extends MinestomAdvancedGui {

    protected final List<Page> pages = new ArrayList<>();

    private int currentPage = 0;

    public MinestomPaginatedGui(@NotNull String title) {
        super(title);
    }

    public MinestomPaginatedGui(int size, @NotNull String title) {
        super(size, title);
    }

    public MinestomPaginatedGui(@NotNull Component title) {
        super(title);
    }

    public MinestomPaginatedGui(@NotNull InventoryType type, @NotNull Component title) {
        super(type, title);
    }

    public MinestomPaginatedGui(int size, @NotNull Component title) {
        super(size, title);
    }

    public MinestomPaginatedGui(@NotNull InventoryType type, @NotNull Component title, @Nullable SerializerType defaultSerializer) {
        super(type, title, defaultSerializer);
    }

    @Contract(pure = true)
    public final int pages() {
        return pages.size();
    }

    @Contract(pure = true)
    public final int currentPage() {
        return currentPage;
    }

    @SafeVarargs
    public final void addPage(@NotNull Consumer<AdvancedGuiItemController.@NotNull Builder<MinestomAdvancedGui>> @NotNull ... builderConsumers) {
        addPage(new Page(this, Arrays.stream(builderConsumers)));
    }

    public final void addPage(@NotNull List<Consumer<AdvancedGuiItemController.@NotNull Builder<MinestomAdvancedGui>>> builderConsumers) {
        addPage(new Page(this, builderConsumers));
    }

    private void addPage(@NotNull Page page) {
        pages.add(page);
    }

    public boolean nextPage() {
        if (currentPage < pages.size() - 1) {
            openPage(currentPage + 1);
            return true;
        }
        return false;
    }

    public boolean previousPage() {
        if (currentPage > 0) {
            openPage(currentPage - 1);
            return true;
        }
        return false;
    }

    public void openPage(int page) {
        if (page < 0 || page >= pages.size()) return;

        Page currentPage = pages.get(this.currentPage);
        currentPage.unregister();

        Page newPage = pages.get(page);
        newPage.register();

        this.currentPage = page;
    }

    protected record Page(@NotNull List<? extends AdvancedGuiItemController<MinestomAdvancedGui, ?>> controllers) {

        public static final String ITEM_KEY = "paged_item_";

        public Page(@NotNull MinestomAdvancedGui gui, @NotNull List<Consumer<AdvancedGuiItemController.@NotNull Builder<MinestomAdvancedGui>>> builderConsumers) {
            this(gui, builderConsumers.stream());
        }

        public Page(@NotNull MinestomAdvancedGui gui, @NotNull Stream<Consumer<AdvancedGuiItemController.@NotNull Builder<MinestomAdvancedGui>>> builderStream) {
            this(builderStream.map(builderConsumer -> {
                AdvancedGuiItemController.Builder<MinestomAdvancedGui> builder = new AdvancedGuiItemController.Builder<>(gui);
                builderConsumer.accept(builder);
                return (AdvancedGuiItemController<MinestomAdvancedGui, ?>) builder.build();
            }).toList());
        }

        public void register() {
            for (int i = 0; i < controllers.size(); i++) {
                AdvancedGuiItemController<MinestomAdvancedGui, ?> controller = controllers.get(i);
                controller.gui().registerItem(Page.ITEM_KEY + i, controller);
            }
        }

        public void unregister() {
            for (int i = 0; i < controllers.size(); i++) {
                AdvancedGuiItemController<MinestomAdvancedGui, ?> controller = controllers.get(i);
                controller.gui().unregister(ITEM_KEY + i);
            }
        }
    }
}

