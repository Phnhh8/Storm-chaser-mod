
package dev.stormchasers;

import dev.stormchasers.tornado.*;
import dev.stormchasers.equipment.*;
import dev.stormchasers.vehicle.*;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.*;

public class StormChasers implements ModInitializer {

    public static final String MOD_ID = "stormchasers";

    public static Item TORNADO_REMOTE;
    public static Item STORM_PROBE;
    public static Item STORM_RADAR;
    public static Item CHASER_TRUCK;

    private static Item register(String name, Item item) {
        ResourceKey<Item> key = ResourceKey.create(
            Registries.ITEM,
            Identifier.fromNamespaceAndPath(MOD_ID, name)
        );
        return Registry.register(
            BuiltInRegistries.ITEM,
            key,
            item
        );
    }

    @Override
    public void onInitialize() {

        TORNADO_REMOTE = register(
            "tornado_remote",
            new TornadoRemoteItem(new Item.Properties().stacksTo(1))
        );

        STORM_PROBE = register(
            "storm_probe",
            new StormProbeItem(new Item.Properties().stacksTo(1))
        );

        STORM_RADAR = register(
            "storm_radar",
            new RadarItem(new Item.Properties().stacksTo(1))
        );

        CHASER_TRUCK = register(
            "chaser_truck",
            new ChaserTruckItem(new Item.Properties().stacksTo(1))
        );

        TornadoManager.initialize();

        ItemGroupEvents.modifyEntriesEvent(
            CreativeModeTabs.TOOLS_AND_UTILITIES
        ).register(entries -> {
            entries.accept(TORNADO_REMOTE);
            entries.accept(STORM_PROBE);
            entries.accept(STORM_RADAR);
            entries.accept(CHASER_TRUCK);
        });
    }
}
