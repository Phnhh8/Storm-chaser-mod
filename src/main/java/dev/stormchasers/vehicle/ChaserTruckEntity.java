
package dev.stormchasers.vehicle;

import dev.stormchasers.StormChasers;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.VehicleEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import net.minecraft.server.level.ServerPlayer;

public class ChaserTruckEntity extends VehicleEntity {

    public ChaserTruckEntity(
        EntityType<? extends ChaserTruckEntity> type,
        Level level
    ) {
        super(type, level);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide()) {
            return;
        }

        Vec3 velocity = this.getDeltaMovement();

        double vx = velocity.x;
        double vy = velocity.y;
        double vz = velocity.z;

        if (this.getFirstPassenger() instanceof ServerPlayer driver) {

            Input input = driver.getLastClientInput();

            float throttle =
                (input.forward() ? 1.0F : 0.0F)
                - (input.backward() ? 1.0F : 0.0F);

            float steering =
                (input.left() ? 1.0F : 0.0F)
                - (input.right() ? 1.0F : 0.0F);

            double speed = Math.sqrt(vx * vx + vz * vz);

            // Steering
            if (Math.abs(steering) > 0.01F && speed > 0.02) {
                float reverse = throttle < 0 ? -1.0F : 1.0F;

                float turnSpeed =
                    1.5F + (float) Math.min(speed * 3.0, 1.2);

                this.setYRot(
                    this.getYRot() + steering * turnSpeed * reverse
                );
            }

            // Acceleration
            double acceleration =
                input.sprint() ? 0.035 : 0.025;

            double angle = Math.toRadians(this.getYRot());

            vx += -Math.sin(angle) * throttle * acceleration;
            vz += Math.cos(angle) * throttle * acceleration;

            // Maximum speed
            double maxSpeed;

            if (throttle < 0) {
                maxSpeed = 0.30;
            } else {
                maxSpeed = input.sprint() ? 0.72 : 0.52;
            }

            double horizontalSpeed = Math.sqrt(vx * vx + vz * vz);

            if (horizontalSpeed > maxSpeed) {
                double scale = maxSpeed / horizontalSpeed;
                vx *= scale;
                vz *= scale;
            }
        }

        // Friction
        double friction = this.onGround() ? 0.88 : 0.99;

        vx *= friction;
        vz *= friction;

        // Gravity
        if (!this.onGround()) {
            vy = Math.max(-0.6, vy - 0.08);
        } else if (vy < 0) {
            vy = 0;
        }

        // Collision with terrain
        this.move(
            MoverType.SELF,
            new Vec3(vx, vy, vz)
        );

        if (this.horizontalCollision) {
            vx = 0;
            vz = 0;
        }

        if (this.verticalCollision) {
            vy = 0;
        }

        this.setDeltaMovement(vx, vy, vz);

        this.needsSync = true;
    }

    @Override
    public InteractionResult interact(
        Player player,
        InteractionHand hand,
        Vec3 location
    ) {
        InteractionResult result =
            super.interact(player, hand, location);

        if (result != InteractionResult.PASS) {
            return result;
        }

        if (player.isSecondaryUseActive()) {
            return InteractionResult.PASS;
        }

        if (!this.level().isClientSide()
            && !player.startRiding(this)) {
            return InteractionResult.PASS;
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public Vec3 getDismountLocationForPassenger(
        LivingEntity passenger
    ) {
        double angle = Math.toRadians(this.getYRot());

        return new Vec3(
            this.getX() + Math.cos(angle) * 1.5,
            this.getY() + 0.2,
            this.getZ() + Math.sin(angle) * 1.5
        );
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    protected Item getDropItem() {
        return StormChasers.CHASER_TRUCK;
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
    }
}
