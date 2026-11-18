
package dev.stormchasers.equipment;

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
    public InteractionResult use(Level level, Player player, InteractionHand hand) {

        if (!level.isClientSide()) {
            int wind = 30 + level.random.nextInt(171);

            String risk = wind >= 160 ? "EF5 DANGER"
                : wind >= 130 ? "EF4 DANGER"
                : wind >= 100 ? "EF3 WARNING"
                : wind >= 70 ? "EF2 WARNING"
                : wind >= 45 ? "EF1 WATCH"
                : "LOW RISK";

            player.displayClientMessage(
                Component.literal(
                    "§bSTORM PROBE\n§fWind: §e" + wind +
                    " km/h §f| Risk: §c" + risk
                ),
                true
            );
        }

        return InteractionResult.SUCCESS;
    }
}
