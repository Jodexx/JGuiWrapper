//import me.kcra.takenaka.generator.accessor.AccessorType
//import me.kcra.takenaka.generator.accessor.CodeLanguage
//import me.kcra.takenaka.generator.accessor.plugin.PlatformTristate
//import me.kcra.takenaka.generator.accessor.plugin.accessorRuntime

dependencies {
//    mappingBundle("me.kcra.takenaka:mappings:1.8.8+1.20.6")
    compileOnly(fileTree("libs"))
//    implementation(accessorRuntime())
}

//tasks.jar {
//    from({
//        configurations.runtimeClasspath.get()
//            .filter { it.name.contains("accessor-runtime") }
//            .map { zipTree(it) }
//    })
//}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(8))
}

//accessors {
//    version("1.16.5")
//
//    basePackage("com.jodexindustries.jguiwrapper.accessors")
//    namespaces("spigot", "mojang")
//    accessorType(AccessorType.REFLECTION)
//    platform(PlatformTristate.SERVER)
//    codeLanguage(CodeLanguage.JAVA)
//    mappingWebsite("https://mappings.dev/")
//
//    mapClass("net.minecraft.world.inventory.MenuType") {
//        field("net.minecraft.world.inventory.MenuType", "GENERIC_9x1")
//        field("net.minecraft.world.inventory.MenuType", "GENERIC_9x2")
//        field("net.minecraft.world.inventory.MenuType", "GENERIC_9x3")
//        field("net.minecraft.world.inventory.MenuType", "GENERIC_9x4")
//        field("net.minecraft.world.inventory.MenuType", "GENERIC_9x5")
//        field("net.minecraft.world.inventory.MenuType", "GENERIC_9x6")
//        field("net.minecraft.world.inventory.MenuType", "GENERIC_3x3")
//        field("net.minecraft.world.inventory.MenuType", "ANVIL")
//        field("net.minecraft.world.inventory.MenuType", "BEACON")
//        field("net.minecraft.world.inventory.MenuType", "BLAST_FURNACE")
//        field("net.minecraft.world.inventory.MenuType", "BREWING_STAND")
//        field("net.minecraft.world.inventory.MenuType", "CRAFTING")
//        field("net.minecraft.world.inventory.MenuType", "ENCHANTMENT")
//        field("net.minecraft.world.inventory.MenuType", "FURNACE")
//        field("net.minecraft.world.inventory.MenuType", "GRINDSTONE")
//        field("net.minecraft.world.inventory.MenuType", "HOPPER")
//        field("net.minecraft.world.inventory.MenuType", "LECTERN")
//        field("net.minecraft.world.inventory.MenuType", "LOOM")
//        field("net.minecraft.world.inventory.MenuType", "MERCHANT")
//        field("net.minecraft.world.inventory.MenuType", "SHULKER_BOX")
//        field("net.minecraft.world.inventory.MenuType", "SMITHING")
//        field("net.minecraft.world.inventory.MenuType", "SMOKER")
//        field("net.minecraft.world.inventory.MenuType", "CARTOGRAPHY_TABLE")
//        field("net.minecraft.world.inventory.MenuType", "STONECUTTER")
//    }
//
//
//    mapClass("net.minecraft.world.inventory.AbstractContainerMenu") {
//        method("net.minecraft.world.inventory.MenuType", "getType")
//        field("int", "containerId")
//    }
//
//    mapClass("net.minecraft.world.entity.player.Player") {
//        field("net.minecraft.world.inventory.AbstractContainerMenu", "containerMenu")
//    }
//
//    mapClass("net.minecraft.server.level.ServerPlayer") {
//        field("net.minecraft.server.network.ServerGamePacketListenerImpl", "connection")
//    }
//
//    mapClass("net.minecraft.server.network.ServerGamePacketListenerImpl") {
//        method("void", "send", "net.minecraft.network.protocol.Packet")
//    }
//
//    mapClass("net.minecraft.network.protocol.game.ClientboundOpenScreenPacket") {
//        constructor("int", "net.minecraft.world.inventory.MenuType", "net.minecraft.network.chat.Component")
//    }
//}