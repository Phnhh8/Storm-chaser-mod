
package dev.stormchasers.equipment;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.entity.player.Player;

public class StormProbeItem extends Item {

    public StormProbeItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();

        if (player != null && !context.getLevel().isClientSide()) {
            int wind = 120;
            String risk = "EXTREME";

            player.sendSystemMessage(
                Component.literal(
                    "§bSTORM PROBE | Wind: " + wind + " km/h | Risk: " + risk
                )
            );
        }

        return InteractionResult.SUCCESS;
    }
}
