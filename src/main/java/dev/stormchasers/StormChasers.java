
package dev.stormchasers;

import dev.stormchasers.tornado.TornadoManager;
import dev.stormchasers.tornado.TornadoRemoteItem;
import dev.stormchasers.equipment.StormProbeItem;
import dev.stormchasers.equipment.RadarItem;
import dev.stormchasers.vehicle.ChaserTruckItem;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;

public class StormChasers implements ModInitializer {

    public static final String MOD_ID = "stormchasers";

    private static Item register(String name, ItemFactory factory) {
        ResourceKey<Item> key = ResourceKey.create(
            Registries.ITEM,
            Identifier.fromNamespaceAndPath(MOD_ID, name)
        );

        Item.Properties properties = new Item.Properties()
            .setId(key)
            .stacksTo(1);

        Item item = factory.create(properties);

        return Registry.register(
            BuiltInRegistries.ITEM,
            key,
            item
        );
    }

    @FunctionalInterface
    private interface ItemFactory {
        Item create(Item.Properties properties);
    }

    public static final Item TORNADO_REMOTE = register(
        "tornado_remote",
        TornadoRemoteItem::new
    );

    public static final Item STORM_PROBE = register(
        "storm_probe",
        StormProbeItem::new
    );

    public static final Item STORM_RADAR = register(
        "storm_radar",
        RadarItem::new
    );

    public static final Item CHASER_TRUCK = register(
        "chaser_truck",
        ChaserTruckItem::new
    );

    @Override
    public void onInitialize() {
        TornadoManager.initialize();

        CreativeModeTabEvents.modifyOutputEvent(
            CreativeModeTabs.TOOLS_AND_UTILITIES
        ).register(output -> {
            output.accept(TORNADO_REMOTE);
            output.accept(STORM_PROBE);
            output.accept(STORM_RADAR);
            output.accept(CHASER_TRUCK);
        });
    }
}
