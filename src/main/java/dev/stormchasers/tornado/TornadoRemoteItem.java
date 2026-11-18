
package dev.stormchasers.tornado;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.entity.player.Player;

public class TornadoRemoteItem extends Item {

    public TornadoRemoteItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();

        if (player != null && !context.getLevel().isClientSide()) {
            TornadoManager.spawnTornado(
                context.getLevel(),
                context.getClickedPos().above()
            );

            player.sendSystemMessage(
                Component.literal("§cTORNADO WARNING! Tornado spawned!")
            );
        }

        return InteractionResult.SUCCESS;
    }
}
