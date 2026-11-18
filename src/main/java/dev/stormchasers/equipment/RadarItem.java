
package dev.stormchasers.equipment;


import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

public class RadarItem extends Item {

    public RadarItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(
        Level level,
        Player player,
        InteractionHand hand
    ) {
        if (!level.isClientSide()) {
player.sendSystemMessage(
    Component.literal(
        "§2STORM RADAR: §fScan completed!"
    )
);
                ),
                new UUID(0L, 0L)
            );
        }

        return InteractionResult.SUCCESS;
    }
}
