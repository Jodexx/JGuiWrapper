package com.jodexindustries.jguiwrapper.gui.advanced;

import com.jodexindustries.jguiwrapper.api.text.SerializerType;
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
@SuppressWarnings({"unused", "UnusedReturnValue"})
public class PaginatedAdvancedGui extends AdvancedGui {

    protected final List<Page> pages = new ArrayList<>();

    private int currentPage = 0;

    /**
     * Constructs a GUI with the default size (54) and a string title.
     *
     * @param title The GUI title as a string
     */
    public PaginatedAdvancedGui(@NotNull String title) {
        super(title);
    }

    /**
     * Constructs a GUI with a specific size and string title.
     *
     * @param size  The inventory size
     * @param title The GUI title as a string
     */
    public PaginatedAdvancedGui(int size, @NotNull String title) {
        super(size, title);
    }

    /**
     * Constructs a GUI with the default CHEST type and a component title.
     *
     * @param title The GUI title as a Component
     */
    public PaginatedAdvancedGui(@NotNull Component title) {
        super(title);
    }

    /**
     * Constructs a GUI with a specific inventory type and component title.
     *
     * @param type  The inventory type
     * @param title The GUI title as a Component
     */
    public PaginatedAdvancedGui(@NotNull InventoryType type, @NotNull Component title) {
        super(type, title);
    }

    /**
     * Constructs a GUI with a specific size, optional type, and component title.
     *
     * @param size  The inventory size
     * @param title The GUI title as a Component
     */
    public PaginatedAdvancedGui(int size, @NotNull Component title) {
        super(size, title);
    }

    /**
     * Constructs a GUI with a specific inventory type and component title with serializer.
     *
     * @param type              The inventory type.
     * @param title             The GUI title as a {@link Component}.
     * @param defaultSerializer The default serializer used for converting between plain strings and {@link Component}
     *                          instances. If {@code null}, the
     *                          {@link #defaultSerializer} will be used.
     */
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
    public final void addPage(@NotNull Consumer<GuiItemController.@NotNull Builder> @NotNull ... builderConsumers) {
        addPage(new Page(this, Arrays.stream(builderConsumers)));
    }

    public final void addPage(@NotNull List<Consumer<GuiItemController.@NotNull Builder>> builderConsumers) {
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

    protected record Page(@NotNull List<GuiItemController> controllers) {

        public static final String ITEM_KEY = "paged_item_";

        public Page(@NotNull AdvancedGui gui, @NotNull List<Consumer<GuiItemController.@NotNull Builder>> builderConsumers) {
            this(gui, builderConsumers.stream());
        }

        public Page(@NotNull AdvancedGui gui, @NotNull Stream<Consumer<GuiItemController.@NotNull Builder>> builderStream) {
            this(builderStream.map(builderConsumer -> {
                GuiItemController.Builder builder = new GuiItemController.Builder(gui);
                builderConsumer.accept(builder);
                return builder.build();
            }).toList());
        }

    }
}
