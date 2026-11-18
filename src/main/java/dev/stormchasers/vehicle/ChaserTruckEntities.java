
package dev.stormchasers.vehicle;

import dev.stormchasers.StormChasers;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.phys.Vec3;

public final class ChaserTruckEntities {

    public static final ResourceKey<EntityType<?>> CHASER_TRUCK_KEY =
        ResourceKey.create(
            Registries.ENTITY_TYPE,
            Identifier.fromNamespaceAndPath(
                StormChasers.MOD_ID,
                "chaser_truck"
            )
        );

    public static final EntityType<ChaserTruckEntity> CHASER_TRUCK =
        Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            CHASER_TRUCK_KEY,
            EntityType.Builder.of(
                ChaserTruckEntity::new,
                MobCategory.MISC
            )
            .sized(2.5F, 2.6F)
            .passengerAttachments(new Vec3(0.0, 1.05, -0.1))
            .clientTrackingRange(10)
            .updateInterval(1)
            .noLootTable()
            .build(CHASER_TRUCK_KEY)
        );

    private ChaserTruckEntities() {}

    public static void initialize() {}
}
