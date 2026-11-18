
package dev.stormchasers.tornado;


import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class TornadoRemoteItem extends Item {

    public TornadoRemoteItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(
        Level level,
        Player player,
        InteractionHand hand
    ) {
        if (!level.isClientSide()) {
            Vec3 position = player.position().add(
                player.getLookAngle().scale(24)
            );

            TornadoManager.create(level, position, 900, 8.5);

player.sendSystemMessage(
    Component.literal("§cTORNADO WARNING! Tornado spawned!")
);
            );
        }

        return InteractionResult.SUCCESS;
    }
}
