import com.jodexindustries.jguiwrapper.api.gui.factory.GuiOptions;
import com.jodexindustries.jguiwrapper.api.gui.factory.GuiType;
import com.jodexindustries.jguiwrapper.api.item.ItemWrapper;
import com.jodexindustries.jguiwrapper.minestom.MinestomGuiApi;
import com.jodexindustries.jguiwrapper.minestom.gui.types.advanced.MinestomPaginatedGui;
import com.jodexindustries.jguiwrapper.minestom.item.MinestomItemWrapper;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.LightingChunk;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;

public class MinestomTest {
    public static void main() {
        var server = MinecraftServer.init();

        MinestomGuiApi.init(MinecraftServer.process());

        InstanceContainer instance = MinecraftServer.getInstanceManager().createInstanceContainer();
        instance.setChunkSupplier(LightingChunk::new);
        instance.setGenerator(unit -> unit.modifier().fillHeight(-20, -10, Block.STONE));

        MinecraftServer.getGlobalEventHandler().addListener(AsyncPlayerConfigurationEvent.class, event -> {
            event.setSpawningInstance(instance);
        });

        MinestomPaginatedGui gui = MinestomGuiApi.get().guiFactory().create(GuiType.PAGINATED, GuiOptions.builder().title(Component.text("123")).size(54).build());

        gui.registerItem("prev_page", b -> b.slots(45)
                .defaultItem(MinestomItemWrapper.builder(Material.ARROW).build())
                .defaultClickHandler((e, c) -> {
                    e.setCancelled(true);
                    gui.previousPage();
                }));

        gui.registerItem("next_page", b -> b.slots(53)
                .defaultItem(MinestomItemWrapper.builder(Material.ARROW).build())
                .defaultClickHandler((e, c) -> {
                    e.setCancelled(true);
                    gui.nextPage();
                }));

        for (int i = 0; i < 10; i++) {
            final int slot = i;
            gui.addPage(builder -> {
                builder.slots(slot).defaultItem(ItemWrapper.builder("stone").build()).build();
            });
        }

        gui.openPage(0);

        MinecraftServer.getGlobalEventHandler().addListener(PlayerSpawnEvent.class, event -> {
            if (event.isFirstSpawn()) {
                gui.open(event.getPlayer());
            }
        });

        var commandManager = MinecraftServer.getCommandManager();
        var command = new Command("jguiwrapper");
        command.setDefaultExecutor((sender, _) -> {
            if (sender instanceof Player player) {
                gui.open(player);
            }
        });

        commandManager.register(command);

        server.start("127.0.0.1", 25565);
    }
}
