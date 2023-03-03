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

    public static boolean canStartCollapse(Entity entity) {
        return entity.isOnGround()//地に足付いてるか
                && entity.level.getBlockState(entity.blockPosition().below()).getBlock() instanceof CollapsingBlock;//それは崩壊ブロックの上か
    }

    //崩落が真下に確率で伝播していき、伝播先がなくなれば実際に崩落。
    //でも崩落に関与したブロックが一斉に崩落すると、うまく全てブロックのまま着地はせず一部アイテム化したから、一番下だけ崩落。
    //下がなくなれば確率の壁が減るから崩落しやすくなって、まあまあ一気に崩落感がある
    public static void collapsing(Entity e, BlockPos bp){
        var bs = e.level.getBlockState(bp);
        if(bs.isAir())
            FallingBlockEntity.fall(e.level, bp.above(), e.level.getBlockState(bp.above()));
        else if(shouldCollapse(e, bs))
            collapsing(e, bp.below());
    }

    //崩壊確率を計算し、その確率に当たったかどうかを返す
    public static boolean shouldCollapse(Entity e, BlockState bs) {
        var baseCollapseProbability = bs.getBlock() instanceof CollapsingBlock collapsing ? collapsing.baseCollapseProbability
                : 1 / bs.getBlock().defaultDestroyTime();
        var collapseProbability = e.isNoGravity() ? 0
                : e.getDeltaMovement().horizontalDistance() == 0 || (e instanceof Player pl && pl.isShiftKeyDown()) ? baseCollapseProbability - 0.25F//最崩落しやすい奴以外なら崩落させないで済む値
                : e.isSprinting() ? 2 * baseCollapseProbability
                : baseCollapseProbability * (1 + e.fallDistance);
        return e.level.random.nextFloat() < collapseProbability;
    }
}
