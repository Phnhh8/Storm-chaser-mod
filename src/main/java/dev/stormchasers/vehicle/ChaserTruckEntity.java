
package dev.stormchasers.vehicle;

import dev.stormchasers.StormChasers;

import net.minecraft.server.level.ServerPlayer;
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

public class ChaserTruckEntity extends VehicleEntity {

    private static final double MAX_FORWARD_SPEED = 0.62;
    private static final double MAX_BOOST_SPEED = 0.78;
    private static final double MAX_REVERSE_SPEED = 0.25;

    private static final double ACCELERATION = 0.012;
    private static final double BRAKE_POWER = 0.035;
    private static final double ROLLING_FRICTION = 0.985;

    private static final double GROUND_GRIP = 0.78;
    private static final double AIR_GRIP = 0.97;

    private float steeringSmooth = 0.0F;

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

        boolean grounded = this.onGround();

        float throttle = 0.0F;
        float steering = 0.0F;
        boolean boosting = false;

        // Read driver controls
        if (this.getFirstPassenger() instanceof ServerPlayer driver) {
            Input input = driver.getLastClientInput();

            throttle =
                (input.forward() ? 1.0F : 0.0F)
                - (input.backward() ? 1.0F : 0.0F);

            steering =
                (input.left() ? 1.0F : 0.0F)
                - (input.right() ? 1.0F : 0.0F);

            boosting = input.sprint();
        }

        double horizontalSpeed = Math.sqrt(vx * vx + vz * vz);

        // Smooth steering input
        steeringSmooth += (steering - steeringSmooth) * 0.22F;

        // Calculate speed relative to the truck direction
        double yaw = Math.toRadians(this.getYRot());

        double forwardX = -Math.sin(yaw);
        double forwardZ = Math.cos(yaw);

        double sideX = Math.cos(yaw);
        double sideZ = Math.sin(yaw);

        double forwardSpeed = vx * forwardX + vz * forwardZ;
        double sideSpeed = vx * sideX + vz * sideZ;

        // Steering: slower at low speed, faster while moving
        if (Math.abs(steeringSmooth) > 0.01F) {
            double speedFactor = Math.min(horizontalSpeed / 0.30, 1.0);

            double turnRate = 0.25 + speedFactor * 1.15;

            if (forwardSpeed < -0.02) {
                turnRate *= -1.0;
            }

            if (!grounded) {
                turnRate *= 0.35;
            }

            this.setYRot(
                this.getYRot() + steeringSmooth * (float) turnRate
            );
        }

        // Recalculate direction after steering
        yaw = Math.toRadians(this.getYRot());

        forwardX = -Math.sin(yaw);
        forwardZ = Math.cos(yaw);

        sideX = Math.cos(yaw);
        sideZ = Math.sin(yaw);

        // Recalculate forward and sideways velocity
        forwardSpeed = vx * forwardX + vz * forwardZ;
        sideSpeed = vx * sideX + vz * sideZ;

        // Acceleration and braking
        if (throttle > 0.0F) {
            if (forwardSpeed < -0.02) {
                // Brake before changing from reverse to forward
                forwardSpeed += BRAKE_POWER;
            } else {
                double acceleration = boosting
                    ? ACCELERATION * 1.35
                    : ACCELERATION;

                forwardSpeed += acceleration;
            }
        } else if (throttle < 0.0F) {
            if (forwardSpeed > 0.02) {
                // Brake before reversing
                forwardSpeed -= BRAKE_POWER;
            } else {
                forwardSpeed -= ACCELERATION * 0.65;
            }
        } else {
            // Gradually slow down when no key is pressed
            forwardSpeed *= ROLLING_FRICTION;

            if (Math.abs(forwardSpeed) < 0.003) {
                forwardSpeed = 0.0;
            }
        }

        // Speed limits
        double maxSpeed;

        if (forwardSpeed < 0.0) {
            maxSpeed = MAX_REVERSE_SPEED;
        } else {
            maxSpeed = boosting
                ? MAX_BOOST_SPEED
                : MAX_FORWARD_SPEED;
        }

        forwardSpeed = Math.max(
            -MAX_REVERSE_SPEED,
            Math.min(forwardSpeed, maxSpeed)
        );

        // Tire grip: remove sideways sliding
        double grip = grounded ? GROUND_GRIP : AIR_GRIP;

        sideSpeed *= grip;

        if (Math.abs(sideSpeed) < 0.002) {
            sideSpeed = 0.0;
        }

        // Rebuild horizontal velocity
        vx = forwardX * forwardSpeed + sideX * sideSpeed;
        vz = forwardZ * forwardSpeed + sideZ * sideSpeed;

        // Gravity
        if (!grounded) {
            vy = Math.max(-0.6, vy - 0.08);
        } else if (vy < 0.0) {
            vy = 0.0;
        }

        // Move and handle collisions
        this.move(
            MoverType.SELF,
            new Vec3(vx, vy, vz)
        );

        if (this.horizontalCollision) {
            vx = 0.0;
            vz = 0.0;
        }

        if (this.verticalCollision) {
            vy = 0.0;
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
            this.getX() + Math.cos(angle) * 1.8,
            this.getY() + 0.2,
            this.getZ() + Math.sin(angle) * 1.8
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
