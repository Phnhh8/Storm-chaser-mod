
package dev.stormchasers.equipment;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.entity.player.Player;

public class RadarItem extends Item {

    public RadarItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();

        if (player != null && !context.getLevel().isClientSide()) {
            player.sendSystemMessage(
                Component.literal("§2STORM RADAR: §fScan completed!")
            );
        }

        return InteractionResult.SUCCESS;
    }
}
