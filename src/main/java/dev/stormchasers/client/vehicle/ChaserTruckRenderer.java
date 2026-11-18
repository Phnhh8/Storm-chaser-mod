
package dev.stormchasers.client.vehicle;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import dev.stormchasers.vehicle.ChaserTruckEntity;

import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;

public class ChaserTruckRenderer
    extends EntityRenderer<ChaserTruckEntity, ChaserTruckRenderer.State> {

    public static class State extends EntityRenderState {
        public float yaw;
        public float wheelSpin;
        public float radarSpin;
    }

    public ChaserTruckRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 1.15F;
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(
        ChaserTruckEntity entity,
        State state,
        float partialTicks
    ) {
        super.extractRenderState(entity, state, partialTicks);

        state.yaw = entity.getYRot(partialTicks);

        state.wheelSpin =
            state.ageInTicks
            * (float) entity.getDeltaMovement().horizontalDistance()
            * 18.0F;

        state.radarSpin = state.ageInTicks * 7.0F;
    }

    @Override
    public void submit(
        State state,
        PoseStack poseStack,
        SubmitNodeCollector collector,
        CameraRenderState camera
    ) {
        poseStack.pushPose();

        // Orient the truck in the direction it is facing
        poseStack.rotateDegrees(Axis.YP, -state.yaw);

        ChaserTruckModel.render(
            collector,
            poseStack,
            state.lightCoords,
            state.wheelSpin,
            state.radarSpin
        );

        poseStack.popPose();

        super.submit(state, poseStack, collector, camera);
    }
}
