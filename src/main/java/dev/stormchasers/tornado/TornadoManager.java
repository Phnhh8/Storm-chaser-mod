
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

    private static final double SUCTION_RADIUS = 22.0;
    private static final double TORNADO_HEIGHT = 27.0;

    private TornadoManager() {}

    // =========================================
    // INITIALIZATION
    // =========================================

    public static void initialize() {
        if (initialized) return;

        initialized = true;

        ServerTickEvents.END_SERVER_TICK.register(server ->
            tick(server.getAllLevels())
        );
    }

    // =========================================
    // SPAWN TORNADO
    // =========================================

    public static void spawnTornado(
        Level level,
        BlockPos position
    ) {
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
        if (level.isClientSide()) return;

        double angle = level.getRandom().nextDouble()
            * Math.PI * 2.0;

        double speed = Math.max(
            0.035,
            Math.min(0.085, 0.045 + strength / 10000.0)
        );

        ACTIVE.add(new Vortex(
            level.dimension(),
            position,
            duration,
            strength,
            Math.cos(angle),
            Math.sin(angle),
            speed
        ));
    }

    // =========================================
    // MAIN TICK
    // =========================================

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

            // Move the actual tornado center
            moveTornado(world, v);

            // Effects follow the moving center
            spawnParticles(world, v);

            // Suck entities into the moving tornado
            affectEntities(world, v);

            // Lift a limited number of blocks
            if (v.tick % 8 == 0) {
                liftBlocks(world, v);
            }
        }
    }

    // =========================================
    // TORNADO MOVEMENT
    // =========================================

    private static void moveTornado(
        ServerLevel world,
        Vortex v
    ) {

        // Gently change direction every 100 ticks
        if (v.tick % 100 == 0) {

            double change =
                (world.getRandom().nextDouble() - 0.5) * 0.65;

            double currentAngle = Math.atan2(
                v.directionZ,
                v.directionX
            );

            double newAngle = currentAngle + change;

            v.directionX = Math.cos(newAngle);
            v.directionZ = Math.sin(newAngle);
        }

        // Small natural steering variation
        double angle = Math.atan2(
            v.directionZ,
            v.directionX
        );

        angle += (world.getRandom().nextDouble() - 0.5) * 0.006;

        v.directionX = Math.cos(angle);
        v.directionZ = Math.sin(angle);

        // Move the center through the world
        v.center = v.center.add(
            v.directionX * v.speed,
            0.0,
            v.directionZ * v.speed
        );
    }

    // =========================================
    // TORNADO PARTICLES
    // =========================================

    private static void spawnParticles(
        ServerLevel world,
        Vortex v
    ) {

        double rotation = v.tick * 0.24;

        // Main rotating funnel
        for (int i = 0; i < 32; i++) {

            double y = (i % 27) * 0.95;

            // Wide bottom, narrower upper funnel
            double radius = 1.5 + (27.0 - y) * 0.095;

            double angle = rotation + i * 0.72;

            double x = v.center.x
                + Math.cos(angle) * radius;

            double z = v.center.z
                + Math.sin(angle) * radius;

            world.sendParticles(
                ParticleTypes.CLOUD,
                x,
                v.center.y + y,
                z,
                1,
                0.18,
                0.22,
                0.18,
                0.025
            );
        }

        // Ground dust ring
        for (int i = 0; i < 12; i++) {

            double angle = rotation * 1.5 + i * 0.52;

            double radius = 4.0 + Math.sin(rotation + i) * 0.8;

            double x = v.center.x + Math.cos(angle) * radius;
            double z = v.center.z + Math.sin(angle) * radius;

            world.sendParticles(
                ParticleTypes.CLOUD,
                x,
                v.center.y + 0.3,
                z,
                1,
                0.35,
                0.12,
                0.35,
                0.04
            );
        }
    }

    // =========================================
    // ENTITY SUCTION
    // =========================================

    private static void affectEntities(
        ServerLevel world,
        Vortex v
    ) {

        AABB area = new AABB(
            v.center.x - SUCTION_RADIUS,
            v.center.y - 5,
            v.center.z - SUCTION_RADIUS,
            v.center.x + SUCTION_RADIUS,
            v.center.y + TORNADO_HEIGHT,
            v.center.z + SUCTION_RADIUS
        );

        for (Entity entity : world.getEntities(
            (Entity) null,
            area,
            e -> e.isAlive()
        )) {

            if (entity.isSpectator()) continue;

            Vec3 difference = new Vec3(
                v.center.x - entity.getX(),
                0.0,
                v.center.z - entity.getZ()
            );

            double distance = Math.max(
                0.5,
                difference.horizontalDistance()
            );

            if (distance > SUCTION_RADIUS) continue;

            double pull = 1.0 - distance / SUCTION_RADIUS;

            // Direction toward tornado center
            double inwardX = difference.x / distance;
            double inwardZ = difference.z / distance;

            // Circular wind around the center
            double spin = 0.16 + pull * 0.12;

            double force = 0.035 + pull * 0.19;

            double verticalForce = 0.025 + pull * 0.22;

            // Stronger upward force near the funnel
            if (distance < 5.0) {
                verticalForce += 0.12;
            }

            Vec3 velocity = entity.getDeltaMovement();

            Vec3 addedForce = new Vec3(
                inwardX * force - inwardZ * spin,
                verticalForce,
                inwardZ * force + inwardX * spin
            );

            Vec3 newVelocity = velocity.add(addedForce);

            // Prevent extreme acceleration
            if (newVelocity.length() > 1.6) {
                newVelocity = newVelocity.normalize().scale(1.6);
            }

            entity.setDeltaMovement(newVelocity);
        }
    }

    // =========================================
    // BLOCK LIFTING
    // =========================================

    private static void liftBlocks(
        ServerLevel world,
        Vortex v
    ) {

        int lifted = 0;

        final int MAX_BLOCKS = 2;

        BlockPos center = BlockPos.containing(v.center);

        // Random sampling prevents huge areas being destroyed
        for (int attempt = 0; attempt < 16; attempt++) {

            if (lifted >= MAX_BLOCKS) break;

            int x = world.getRandom().nextInt(17) - 8;
            int z = world.getRandom().nextInt(17) - 8;
            int y = world.getRandom().nextInt(5) - 1;

            double distance = Math.sqrt(x * x + z * z);

            if (distance > 8.0 || distance < 1.5) {
                continue;
            }

            BlockPos pos = center.offset(x, y, z);

            BlockState state = world.getBlockState(pos);

            if (state.isAir()) continue;

            // Never remove block entities
            if (state.hasBlockEntity()) continue;

            // Never remove unbreakable blocks
            if (state.getDestroySpeed(world, pos) < 0) {
                continue;
            }

            // Keep block lifting selective
            if (world.getRandom().nextFloat() > 0.28F) {
                continue;
            }

            FallingBlockEntity falling =
                FallingBlockEntity.fall(world, pos, state);

            if (falling == null) continue;

            double angle = Math.atan2(
                v.center.z - falling.getZ(),
                v.center.x - falling.getX()
            );

            // Spiral toward the moving funnel
            double horizontalForce = 0.22;

            double velocityX =
                Math.cos(angle) * horizontalForce
                - v.directionZ * 0.20;

            double velocityZ =
                Math.sin(angle) * horizontalForce
                + v.directionX * 0.20;

            double upwardForce =
                0.32 + world.getRandom().nextDouble() * 0.30;

            falling.setDeltaMovement(
                velocityX,
                upwardForce,
                velocityZ
            );

            lifted++;
        }
    }

    // =========================================
    // VORTEX DATA
    // =========================================

    private static class Vortex {

        final ResourceKey<Level> dimension;

        Vec3 center;

        final double strength;

        double directionX;
        double directionZ;

        final double speed;

        int time;
        int tick;

        Vortex(
            ResourceKey<Level> dimension,
            Vec3 center,
            int time,
            double strength,
            double directionX,
            double directionZ,
            double speed
        ) {
            this.dimension = dimension;
            this.center = center;
            this.time = time;
            this.strength = strength;
            this.directionX = directionX;
            this.directionZ = directionZ;
            this.speed = speed;
        }
    }
}
