import com.jodexindustries.jguiwrapper.api.gui.event.GuiClickEvent;
import com.jodexindustries.jguiwrapper.api.gui.event.GuiCloseEvent;
import com.jodexindustries.jguiwrapper.api.gui.event.GuiDragEvent;
import com.jodexindustries.jguiwrapper.api.item.ItemWrapper;
import com.jodexindustries.jguiwrapper.minestom.gui.types.MinestomGuiBase;
import org.jetbrains.annotations.NotNull;

public class MinestomGuiTest extends MinestomGuiBase<MinestomGuiTest> {

    public MinestomGuiTest(@NotNull String title) {
        super(title);

        ItemWrapper stone = ItemWrapper.builder("stone")
                .displayName("&bSome name")
                .lore("&cHehe", "&aNah")
                .enchanted(true)
                .build();

        holder().setItem(1, stone);
    }


    @Override
    public void onClick(@NotNull GuiClickEvent event) {
        super.onClick(event);

        event.user().sendMessage("Clicked: " + event.rawSlot() + " Action: " + event.action() + " Type: " + event.clickType());
    }

    @Override
    public void onDrag(@NotNull GuiDragEvent event) {
        super.onDrag(event);

        event.user().sendMessage("Dragged: " + event.rawSlots());
    }

    @Override
    public void onClose(@NotNull GuiCloseEvent event) {
        super.onClose(event);

        event.user().sendMessage("Closed: " + event.gui());
    }
}
