
package dev.stormchasers.tornado;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class TornadoManager {

    private static final List<Vortex> ACTIVE = new ArrayList<>();
    private static boolean initialized = false;

    private TornadoManager() {}

    public static void initialize() {
        if (initialized) return;
        initialized = true;

        ServerTickEvents.END_SERVER_TICK.register(server ->
            tick(server.getAllLevels())
        );
    }

    public static void spawnTornado(Level level, BlockPos position) {
        if (level.isClientSide()) return;

        create(
            level,
            Vec3.atBottomCenterOf(position),
            20 * 120,
            180.0
        );
    }

    public static void create(
        Level level,
        Vec3 position,
        int duration,
        double strength
    ) {
        ACTIVE.add(new Vortex(
            level.dimension(),
            position,
            duration,
            strength
        ));
    }

    private static void tick(Iterable<ServerLevel> worlds) {
        Iterator<Vortex> iterator = ACTIVE.iterator();

        while (iterator.hasNext()) {
            Vortex v = iterator.next();
            ServerLevel world = null;

            for (ServerLevel candidate : worlds) {
                if (candidate.dimension().equals(v.dimension)) {
                    world = candidate;
                    break;
                }
            }

            if (world == null || v.time-- <= 0) {
                iterator.remove();
                continue;
            }

            double t = world.getGameTime() * 0.2;

            // Tornado particle effects
            for (int i = 0; i < 40; i++) {
                double y = (i % 20) * 1.2;
                double radius = 2 + y * 0.13;
                double angle = t + i * 0.4;

                double x = v.center.x + Math.cos(angle) * radius;
                double z = v.center.z + Math.sin(angle) * radius;

                world.sendParticles(
                    ParticleTypes.CLOUD,
                    x, v.center.y + y, z,
                    3, 0.3, 0.3, 0.3, 0.02
                );
            }

            // Tornado suction area
            AABB area = new AABB(
                v.center.x - 12, v.center.y - 3, v.center.z - 12,
                v.center.x + 12, v.center.y + 25, v.center.z + 12
            );

            for (Entity entity : world.getEntities(
                (Entity) null,
                area,
                e -> e.isAlive()
            )) {
                Vec3 delta = v.center.subtract(entity.position());

                double distance = Math.max(
                    1.0,
                    delta.horizontalDistance()
                );

                Vec3 force = new Vec3(
                    delta.x / distance * v.strength / 100,
                    0.15,
                    delta.z / distance * v.strength / 100
                );

                entity.setDeltaMovement(
                    entity.getDeltaMovement().add(force)
                );
            }
        }
    }

    private static class Vortex {
        final ResourceKey<Level> dimension;
        final Vec3 center;
        final double strength;
        int time;

        Vortex(
            ResourceKey<Level> dimension,
            Vec3 center,
            int time,
            double strength
        ) {
            this.dimension = dimension;
            this.center = center;
            this.time = time;
            this.strength = strength;
        }
    }
}
