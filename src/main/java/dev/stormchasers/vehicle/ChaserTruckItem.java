
package dev.stormchasers.vehicle;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class ChaserTruckItem extends Item {

    public ChaserTruckItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {

        Level level = context.getLevel();

        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        BlockPos pos = context.getClickedPos()
            .relative(context.getClickedFace())
            .above();

        Entity cart = net.minecraft.world.entity.EntityType.MINECART
            .create(level, EntitySpawnReason.SPAWN_ITEM_USE);

        if (cart == null) {
            return InteractionResult.FAIL;
        }

        cart.setPos(
            pos.getX() + 0.5,
            pos.getY(),
            pos.getZ() + 0.5
        );

        cart.setCustomName(
            Component.literal("§bSTORM CHASER TRUCK")
        );

        cart.setCustomNameVisible(true);

        level.addFreshEntity(cart);

        Player player = context.getPlayer();

        if (player != null && !player.getAbilities().instabuild) {
            context.getItemInHand().shrink(1);
        }

        return InteractionResult.SUCCESS;
    }
}
