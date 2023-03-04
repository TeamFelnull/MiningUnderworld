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
                && entity.level.getBlockState(entity.blockPosition().below()).getBlock() instanceof CollapsingBlock;//崩落ブロックの上か
    }

    //崩落計算開始時の処理
    public static void startCollapse(Entity e, BlockPos pos){
        collapsing(e, pos);//崩落の連鎖を開始
    }

    //崩落が真下に確率で伝播していき、伝播先がなくなれば実際に崩落。
    //でも崩落に関与したブロックが一斉に崩落すると、うまく全てブロックのまま着地はせず一部アイテム化したから、一番下だけ崩落。
    //下がなくなれば確率の壁が減るから崩落しやすくなって、まあまあ一気に崩落感がある
    public static void collapsing(Entity e, BlockPos pos) {
        var bs = e.level.getBlockState(pos);
        if (bs.isAir() /*&& !e.level.isOutsideBuildHeight(bp)*//*岩盤を突き抜けたら駄目だなと思ったけど爆破耐性も加味すればそうそう突き抜けることないからやめた*/)
            collapse(e, pos.above());
        else if (e.level.random.nextFloat() < collapseRate(e, bs))
            collapsing(e, pos.below());
    }

    //崩落確率
    public static float collapseRate(Entity e, BlockState bs) {
        var collapseRate = bs.getBlock() instanceof CollapsingBlock collapsing ? collapsing.startCollapseRate
                : 3F * 2 / Math.max(bs.getBlock().defaultDestroyTime() + bs.getBlock().getExplosionResistance(), 3F * 2);//ブロックごとの基本値、strength3以下は確率１にキャップ(ゼロ除算回避も兼ねて)
        collapseRate *= e.isNoGravity() ? 0
                : bs.getBlock() != MUBlocks.MOST_LIKELY_COLLAPSING_BLOCK.get() && (e.getDeltaMovement().horizontalDistance() == 0 || (e instanceof Player pl && pl.isShiftKeyDown())) ? 1 / 4F//MOST_LIKELY以外は止まったりシフトで崩落しにくくできる
                : e.isSprinting() ? 2//走ったら崩れやすい
                : 1;//それ以外、歩いたりMOST_LIKELYの上で止まったりスニーク
        collapseRate *= 1 + ((PrevFallDistanceEntity) e).getPrevFallDistance();//落下距離による係数
        return collapseRate;
    }

    //実際に崩落させる際の処理
    //TODO:ベッドみたいな複数ブロックで一つなやつも正常に落としたい
    private static void collapse(Entity e, BlockPos pos) {
        if(collapseRate(e, e.level.getBlockState(pos)) >= 1)//凄い高くから落下して一番下のブロックを確実に落とせる状況だったなら
            ((PrevFallDistanceEntity) e).postponeStoring();//次tickもその高さから落ちたとして計算して、一番下以外も落ちやすくする処理
        FallingBlockEntity.fall(e.level, pos, e.level.getBlockState(pos));
    }
}
