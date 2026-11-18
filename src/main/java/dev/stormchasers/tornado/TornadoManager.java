
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
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
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

            v.tick++;

            double t = world.getGameTime() * 0.22;

            // Tornado particles
            for (int i = 0; i < 60; i++) {
                double y = (i % 30) * 0.85;
                double radius = 2.0 + y * 0.16;
                double angle = t + i * 0.55;

                double x = v.center.x + Math.cos(angle) * radius;
                double z = v.center.z + Math.sin(angle) * radius;

                world.sendParticles(
                    ParticleTypes.CLOUD,
                    x,
                    v.center.y + y,
                    z,
                    2,
                    0.25,
                    0.25,
                    0.25,
                    0.03
                );
            }

            // Entity suction and throwing
            AABB area = new AABB(
                v.center.x - 18,
                v.center.y - 5,
                v.center.z - 18,
                v.center.x + 18,
                v.center.y + 30,
                v.center.z + 18
            );

            for (Entity entity : world.getEntities(
                (Entity) null,
                area,
                e -> e.isAlive()
            )) {
                if (entity.isSpectator()) continue;

                Vec3 delta = v.center.subtract(entity.position());
                double distance = Math.max(
                    1.0,
                    delta.horizontalDistance()
                );

                double inwardX = delta.x / distance;
                double inwardZ = delta.z / distance;

                double spin = 0.22;
                double pull = Math.max(
                    0.04,
                    1.0 - distance / 20.0
                );

                Vec3 force = new Vec3(
                    inwardX * pull * 0.20 - inwardZ * spin,
                    0.18 + pull * 0.24,
                    inwardZ * pull * 0.20 + inwardX * spin
                );

                entity.setDeltaMovement(
                    entity.getDeltaMovement().add(force)
                );
            }

            // Destroy and lift blocks every 4 ticks
            if (v.tick % 4 == 0) {
                liftBlocks(world, v);
            }
        }
    }

    private static void liftBlocks(ServerLevel world, Vortex v) {
        int lifted = 0;
        int maxBlocksPerPass = 8;

        BlockPos center = BlockPos.containing(v.center);

        for (int y = -3; y <= 12; y++) {
            for (int x = -9; x <= 9; x++) {
                for (int z = -9; z <= 9; z++) {

                    if (lifted >= maxBlocksPerPass) return;

                    double distance = Math.sqrt(x * x + z * z);

                    if (distance > 9 || distance < 1.5) continue;

                    BlockPos pos = center.offset(x, y, z);
                    BlockState state = world.getBlockState(pos);

                    if (state.isAir()) continue;

                    // Protect block entities such as chests and machines
                    if (state.hasBlockEntity()) continue;

                    // Protect unbreakable blocks
                    if (state.getDestroySpeed(world, pos) < 0) continue;

                    // Turn the block into a flying block entity
                    FallingBlockEntity falling =
                        FallingBlockEntity.fall(world, pos, state);

                    double angle =
                        Math.atan2(z, x) + world.getGameTime() * 0.18;

                    double horizontalForce = 0.35;

                    falling.setDeltaMovement(
                        -Math.sin(angle) * horizontalForce,
                        0.65 + world.getRandom().nextDouble() * 0.35,
                        Math.cos(angle) * horizontalForce
                    );

                    lifted++;
                }
            }
        }
    }

    private static class Vortex {
        final ResourceKey<Level> dimension;
        final Vec3 center;
        final double strength;

        int time;
        int tick;

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
