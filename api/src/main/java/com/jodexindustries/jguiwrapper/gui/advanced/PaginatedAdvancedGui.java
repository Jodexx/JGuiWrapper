package com.jodexindustries.jguiwrapper.gui.advanced;

import net.kyori.adventure.text.Component;
import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@ApiStatus.Experimental
@SuppressWarnings({"unused", "UnusedReturnValue"})
public class PaginatedAdvancedGui extends AdvancedGui {

    protected final List<Page> pages = new ArrayList<>();

    private int currentPage = 0;

    public PaginatedAdvancedGui(@NotNull String title) {
        super(title);
    }

    public PaginatedAdvancedGui(int size, @NotNull String title) {
        super(size, title);
    }

    public PaginatedAdvancedGui(@NotNull Component title) {
        super(title);
    }

    public PaginatedAdvancedGui(InventoryType type, @NotNull Component title) {
        super(type, title);
    }

    public PaginatedAdvancedGui(int size, @NotNull Component title) {
        super(size, title);
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
    public final void addPage(@NotNull Consumer<GuiItemController.Builder> @NotNull ... builderConsumers) {
        Page page = new Page();

        for (Consumer<GuiItemController.Builder> builderConsumer : builderConsumers) {
            GuiItemController.Builder builder = new GuiItemController.Builder(this);
            builderConsumer.accept(builder);

            page.controllers.add(builder.build());
        }

        addPage(page);
    }

    public final void addPage(@NotNull List<Consumer<GuiItemController.Builder>> builderConsumers) {
        addPage(new Page(this, builderConsumers));
    }

    private void addPage(@NotNull Page page) {
        pages.add(page);
    }

    public boolean nextPage() {
        if (currentPage < pages.size() - 1) {
            openPage(currentPage + 1);
            return true;
        } else {
            return false;
        }
    }

    public boolean previousPage() {
        if (currentPage > 0) {
            openPage(currentPage - 1);
            return true;
        } else {
            return false;
        }
    }

    public void openPage(int page) {
        if (page < 0 || page >= pages.size()) return;

        Page currentPage = pages.get(this.currentPage);

        for (int i = 0; i < currentPage.controllers().size(); i++) {
            unregister(Page.ITEM_KEY + i);
        }

        Page newPage = pages.get(page);

        for (int i = 0; i < newPage.controllers().size(); i++) {
            GuiItemController controller = newPage.controllers().get(i);

            registerItem(Page.ITEM_KEY + i, controller);
        }

        this.currentPage = page;
    }

    protected record Page(List<GuiItemController> controllers) {

        public static final String ITEM_KEY = "paged_item_";

        public Page() {
            this(new ArrayList<>());
        }

        public Page(AdvancedGui gui, List<Consumer<GuiItemController.Builder>> builderConsumers) {
            this(builderConsumers.stream().map(builderConsumer -> {
                GuiItemController.Builder builder = new GuiItemController.Builder(gui);
                builderConsumer.accept(builder);
                return builder.build();
            }).toList());
        }

    }
}
