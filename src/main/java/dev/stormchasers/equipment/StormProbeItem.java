
package dev.stormchasers.equipment;

import java.util.concurrent.ThreadLocalRandom;

import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

public class StormProbeItem extends Item {

    public StormProbeItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(
        Level level,
        Player player,
        InteractionHand hand
    ) {
        if (!level.isClientSide()) {
            int wind = ThreadLocalRandom.current().nextInt(30, 201);

            String risk =
                wind >= 180 ? "EF5 EXTREME" :
                wind >= 150 ? "EF4 SEVERE" :
                wind >= 120 ? "EF3 HIGH" :
                wind >= 90 ? "EF2 MODERATE" :
                wind >= 60 ? "EF1 LOW" : "NORMAL";

            player.sendSystemMessage(
                Component.literal(
                    "§bSTORM PROBE | Wind: " +
                    wind + " km/h | Risk: " + risk
                ),
                Util.NIL_UUID
            );
        }

        return InteractionResult.SUCCESS;
    }
}
