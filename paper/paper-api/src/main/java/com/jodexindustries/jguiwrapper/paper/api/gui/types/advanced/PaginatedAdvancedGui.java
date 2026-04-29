package com.jodexindustries.jguiwrapper.paper.api.gui.types.advanced;

import com.jodexindustries.jguiwrapper.api.text.SerializerType;
import com.jodexindustries.jguiwrapper.api.gui.types.advanced.AdvancedGuiItemController;
import net.kyori.adventure.text.Component;
import org.bukkit.event.inventory.InventoryType;
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
public class PaginatedAdvancedGui extends PaperAdvancedGui {

    protected final List<Page> pages = new ArrayList<>();

    private int currentPage = 0;

    public PaginatedAdvancedGui(int size, @NotNull Component title, @Nullable SerializerType defaultSerializer) {
        super(size, title, defaultSerializer);
    }

    public PaginatedAdvancedGui(@NotNull InventoryType type, @NotNull Component title, @Nullable SerializerType defaultSerializer) {
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
    public final void addPage(@NotNull Consumer<AdvancedGuiItemController.@NotNull Builder<PaperAdvancedGui>> @NotNull ... builderConsumers) {
        addPage(new Page(this, Arrays.stream(builderConsumers)));
    }

    public final void addPage(@NotNull List<Consumer<AdvancedGuiItemController.@NotNull Builder<PaperAdvancedGui>>> builderConsumers) {
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

    protected record Page(@NotNull List<? extends AdvancedGuiItemController<PaperAdvancedGui, ?>> controllers) {

        public static final String ITEM_KEY = "paged_item_";

        public Page(@NotNull PaperAdvancedGui gui, @NotNull List<Consumer<AdvancedGuiItemController.@NotNull Builder<PaperAdvancedGui>>> builderConsumers) {
            this(gui, builderConsumers.stream());
        }

        public Page(@NotNull PaperAdvancedGui gui, @NotNull Stream<Consumer<AdvancedGuiItemController.@NotNull Builder<PaperAdvancedGui>>> builderStream) {
            this(builderStream.map(builderConsumer -> {
                AdvancedGuiItemController.Builder<PaperAdvancedGui> builder = new AdvancedGuiItemController.Builder<>(gui);
                builderConsumer.accept(builder);
                return (AdvancedGuiItemController<PaperAdvancedGui, ?>) builder.build();
            }).toList());
        }

        public void register() {
            for (int i = 0; i < controllers.size(); i++) {
                AdvancedGuiItemController<PaperAdvancedGui, ?> controller = controllers.get(i);
                controller.gui().registerItem(Page.ITEM_KEY + i, controller);
            }
        }

        public void unregister() {
            for (int i = 0; i < controllers.size(); i++) {
                AdvancedGuiItemController<PaperAdvancedGui, ?> controller = controllers.get(i);
                controller.gui().unregister(ITEM_KEY + i);
            }
        }

    }
}
