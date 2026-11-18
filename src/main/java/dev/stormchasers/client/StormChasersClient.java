
package dev.stormchasers.client;

import dev.stormchasers.client.vehicle.ChaserTruckRenderer;
import dev.stormchasers.vehicle.ChaserTruckEntities;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.renderer.entity.EntityRenderers;

public class StormChasersClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        EntityRenderers.register(
            ChaserTruckEntities.CHASER_TRUCK,
            ChaserTruckRenderer::new
        );
    }
}
