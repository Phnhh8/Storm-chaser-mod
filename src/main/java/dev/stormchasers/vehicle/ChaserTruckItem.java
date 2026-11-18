
package dev.stormchasers.vehicle;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class ChaserTruckItem extends Item {

    public ChaserTruckItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {

        Level level = context.getLevel();

        if (!level.isClientSide()) {

            BlockPos pos = context.getClickedPos()
                .relative(context.getClickedFace());

            ChaserTruckEntity truck =
                ChaserTruckEntities.CHASER_TRUCK.create(
                    level,
                    EntitySpawnReason.SPAWN_ITEM_USE
                );

            if (truck != null) {

                truck.setPos(
                    pos.getX() + 0.5,
                    pos.getY(),
                    pos.getZ() + 0.5
                );

                Player player = context.getPlayer();

                if (player != null) {
                    truck.setYRot(player.getYRot());
                }

                if (level.addFreshEntity(truck)
                    && player != null
                    && !player.getAbilities().instabuild) {

                    context.getItemInHand().shrink(1);
                }
            }
        }

        return InteractionResult.SUCCESS;
    }
}
