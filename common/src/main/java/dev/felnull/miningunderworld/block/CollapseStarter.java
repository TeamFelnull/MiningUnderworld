package dev.felnull.miningunderworld.block;

import dev.felnull.miningunderworld.entity.StrictFallingBlockEntity;
import dev.felnull.miningunderworld.server.handler.ServerHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public interface CollapseStarter {

    float getStartCollapseRate();//崩落が始まる確率

    float getCollapseCoefficient();//このブロックから始まった崩落全体にかかる崩落率の係数

    //エンティティは崩落を開始するべきか
    static boolean shouldStartCollapse(Entity e) {
        return e.isOnGround()//地に足付いてるか
                && e.level.getBlockState(e.blockPosition().below()).getBlock() instanceof CollapseStarter starter//崩落開始奴の上か
                && e.level.random.nextFloat() < starter.getStartCollapseRate();//崩落開始確率に勝てたか
    }

    //サーバー側の現在の情報でスタート
    static void startCollapse(Entity e, BlockPos pos) {
        startCollapse(e, pos, e.getDeltaMovement().horizontalDistance());
    }

    static void startCollapse(Entity e, BlockPos pos, double walkDist) {
        if (e.level.getBlockState(pos).getBlock() instanceof CollapseStarter starter)//canStartCollapseで確認してるけどパケット送るまでに移動されたりすると意味を成す
            collapsing(e, pos, starter, walkDist);//崩落の連鎖を開始
    }

    //崩落が真下に確率で伝播していき、伝播先がなくなれば実際に崩落。
    //でも崩落に関与したブロックが一斉に崩落すると、うまく全てブロックのまま着地はせず一部アイテム化したから、一番下だけ崩落。
    //下がなくなれば確率の壁が減るから崩落しやすくなって、まあまあ一気に崩落感がある
    static void collapsing(Entity e, BlockPos pos, CollapseStarter starter, double walkDist) {
        var bs = e.level.getBlockState(pos);
        if (bs.canBeReplaced()/*&& !e.level.isOutsideBuildHeight(bp)*//*岩盤を突き抜けたら駄目だなと思ったけど爆破耐性も加味すればそうそう突き抜けることないからやめた*/)
            collapse(e, pos.above(), starter, walkDist);
        else if (e.level.random.nextFloat() < collapseRate(e, bs, starter, walkDist))
            collapsing(e, pos.below(), starter, walkDist);
        else
            fallDistances.put(e.getUUID(), 0F);//崩落に失敗したら落下距離リセット、しないと次の崩落で落下してないのに凄い崩落し易くなる
    }

    //崩落確率
    static float collapseRate(Entity e, BlockState bs, CollapseStarter starter, double walkDist) {
        float collapseRate = baseCollapseRate(bs);

        //崩落開始奴による係数
        collapseRate *= starter.getCollapseCoefficient();

        //エンティティの動きによる係数
        collapseRate *= e.isNoGravity() ? 0
                : walkDist == 0 || (e instanceof Player pl && pl.isShiftKeyDown()) ? 1 / 4F
                : e.isSprinting() ? 2
                : 1;
        collapseRate *= 1 + 4 * getFallDist(e);

        return collapseRate;
    }

    //ブロックごとの基本値、strength1.5以下で１以上
    static float baseCollapseRate(BlockState bs) {
        return 1.5F * 2 / Math.max(bs.getBlock().defaultDestroyTime() + bs.getBlock().getExplosionResistance(), 0.1F);
    }

    //実際に崩落
    //TODO:ベッドみたいな複数ブロックで一つなやつも正常に落としたい
    private static void collapse(Entity e, BlockPos pos, CollapseStarter starter, double walkDist) {
        //高い確率で崩落したなら次も高い確率で崩落処理
        if (e.level.random.nextFloat() < collapseRate(e, e.level.getBlockState(pos), starter, walkDist))
            ServerHandler.addTickFunc(new ServerHandler.NextTickFunc() {//次tickに崩落処理
                final float fallDist = getFallDist(e);

                @Override
                public void func() {
                    if (!e.isRemoved() && shouldStartCollapse(e)) {
                        fallDistances.put(e.getUUID(), Math.max(fallDist - 1, 0));//高い確率の原因となる落下距離は減衰
                        startCollapse(e, e.blockPosition().below(), walkDist);//移動情報そのままスタート（でなきゃプレイヤーの移動が無条件で０として扱われる）
                    }
                }
            });

        StrictFallingBlockEntity.strictFall(e.level, pos);//今回の崩落
    }

    Map<UUID, Float> fallDistances = new HashMap<>();

    static float getFallDist(Entity e) {
        if (fallDistances.containsKey(e.getUUID()))
            return fallDistances.get(e.getUUID());
        else {
            fallDistances.put(e.getUUID(), 0F);
            return 0F;
        }
    }

    static void fallOn(Entity e) {
        if (!e.level.isClientSide)
            fallDistances.put(e.getUUID(), e.fallDistance);
    }
}
