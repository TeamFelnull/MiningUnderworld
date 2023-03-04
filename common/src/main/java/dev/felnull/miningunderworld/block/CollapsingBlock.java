package dev.felnull.miningunderworld.block;

import dev.felnull.miningunderworld.entity.PrevFallDistanceEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class CollapsingBlock extends Block {

    public float startCollapseRate;//一番最初に崩落しかける確率、CollapsingBlockのみが崩落を開始する仕様故

    public CollapsingBlock(float startCollapseRate, BlockBehaviour.Properties properties) {
        super(properties);
        this.startCollapseRate = startCollapseRate;
    }

    public static boolean canStartCollapse(Entity entity) {//エンティティは崩落させ得る状況にいるか
        return entity.isOnGround()//地に足付いてるか
                && entity.level.getBlockState(entity.blockPosition().below()).getBlock() instanceof CollapsingBlock;//崩壊ブロックの上か
    }

    //崩落が真下に確率で伝播していき、伝播先がなくなれば実際に崩落。
    //でも崩落に関与したブロックが一斉に崩落すると、うまく全てブロックのまま着地はせず一部アイテム化したから、一番下だけ崩落。
    //下がなくなれば確率の壁が減るから崩落しやすくなって、まあまあ一気に崩落感がある
    public static void collapsing(Entity e, BlockPos bp) {
        var bs = e.level.getBlockState(bp);
        if (bs.isAir() /*&& !e.level.isOutsideBuildHeight(bp)*//*岩盤を突き抜けたら駄目だなと思ったけど爆破耐性も加味すればそうそう突き抜けることないからやめた*/)
            FallingBlockEntity.fall(e.level, bp.above(), e.level.getBlockState(bp.above()));//TODO:ベッドみたいな複数ブロックで一つなやつも正常に落としたい
        else if (e.level.random.nextFloat() < collapseRate(e, bs))
            collapsing(e, bp.below());
    }

    public static float collapseRate(Entity e, BlockState bs) {
        var baseCollapseRate = bs.getBlock() instanceof CollapsingBlock collapsing ? collapsing.startCollapseRate
                : 3 / Math.max(bs.getBlock().defaultDestroyTime() + bs.getBlock().getExplosionResistance(), 1 / 4096F);//ブロックごとの基本値、strength1.5以下は確率１、0はエラー吐くからmax
        var collapseRate = e.isNoGravity() ? 0
                : e.getDeltaMovement().horizontalDistance() == 0 || (e instanceof Player pl && pl.isShiftKeyDown()) ? baseCollapseRate - 0.25F//最崩落しやすい奴以外なら崩落させないで済む値
                : e.isSprinting() ? 2 * baseCollapseRate
                : baseCollapseRate;
        return collapseRate + ((PrevFallDistanceEntity) e).getPrevFallDistance() / 16;//高くから落ちたら崩落しやすい
    }
}
