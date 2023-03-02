package dev.felnull.miningunderworld.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class CollapsingBlock extends Block {

    public float baseCollapseProbability;

    public CollapsingBlock(float baseCollapseProbability, BlockBehaviour.Properties properties) {
        super(properties);
        this.baseCollapseProbability = baseCollapseProbability;
    }

    public static boolean shouldCollapse(Entity e, BlockState bs) {
        var baseCollapseProbability = bs.getBlock() instanceof CollapsingBlock collapsing ? collapsing.baseCollapseProbability
                : 1 / bs.getBlock().defaultDestroyTime();
        var collapseProbability = e.isNoGravity() ? 0
                : false /*e.getDeltaMovement() == Vec3.ZERO*//*止まってるときを取得したい*/ || (e instanceof Player pl && pl.isShiftKeyDown()) ? baseCollapseProbability - 0.25F
                : e.isSprinting() ? 2 * baseCollapseProbability
                : baseCollapseProbability * (1 + e.fallDistance);
        return e.level.random.nextFloat() < collapseProbability;
    }

    public static void collapsing(Entity e, BlockPos bp){
        var bs = e.level.getBlockState(bp);
        if(bs.isAir())
            FallingBlockEntity.fall(e.level, bp.above(), e.level.getBlockState(bp.above()));
        else if(shouldCollapse(e, bs))
            collapsing(e, bp.below());
    }
}
