package dev.felnull.miningunderworld.block;

import dev.felnull.miningunderworld.entity.PrevFallDistanceEntity;
import dev.felnull.miningunderworld.server.handler.ServerHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;

public interface CollapseStarter {

    float getStartCollapseRate();//崩落が始まる確率

    float getCollapseCoefficient();//このブロックから始まった崩落全体にかかる崩落率の係数

    //計算中に値が変わったため、ここに保持した値を使う
    ThreadLocal<Double> walkDistNow = new ThreadLocal<>();
    ThreadLocal<Float> prevFallDistNow = new ThreadLocal<>();

    //エンティティは崩落を開始するべきか
    static boolean shouldStartCollapse(Entity e) {
        return e.isOnGround()//地に足付いてるか
                && e.level.getBlockState(e.blockPosition().below()).getBlock() instanceof CollapseStarter starter//崩落開始奴の上か
                && e.level.random.nextFloat() < starter.getStartCollapseRate();//崩落開始確率に勝てたか
    }

    //サーバー側の現在の情報でスタート
    static void startCollapse(Entity e, BlockPos pos) {
        startCollapse(e, pos, e.getDeltaMovement().horizontalDistance(), ((PrevFallDistanceEntity) e).getPrevFallDistance());
    }

    //情報指定してスタート
    static void startCollapse(Entity e, BlockPos pos, double walkDistIn, float prevFallDistIn) {
        //処理中にも値は変わり得るので、処理開始時の値をこちらで保存(パケット通信で送ったら何故か必ず変えられた)
        walkDistNow.set(walkDistIn);
        prevFallDistNow.set(prevFallDistIn);
        if (e.level.getBlockState(pos).getBlock() instanceof CollapseStarter starter)//canStartCollapseで確認してるけどパケット送るまでに移動されたりすると意味を成す
            collapsing(e, pos, starter);//崩落の連鎖を開始
    }

    //崩落が真下に確率で伝播していき、伝播先がなくなれば実際に崩落。
    //でも崩落に関与したブロックが一斉に崩落すると、うまく全てブロックのまま着地はせず一部アイテム化したから、一番下だけ崩落。
    //下がなくなれば確率の壁が減るから崩落しやすくなって、まあまあ一気に崩落感がある
    static void collapsing(Entity e, BlockPos pos, CollapseStarter starter) {
        var bs = e.level.getBlockState(pos);
        if (bs.canBeReplaced()/*&& !e.level.isOutsideBuildHeight(bp)*//*岩盤を突き抜けたら駄目だなと思ったけど爆破耐性も加味すればそうそう突き抜けることないからやめた*/)
            collapse(e, pos.above(), starter);
        else if (e.level.random.nextFloat() < collapseRate(e, bs, starter))
            collapsing(e, pos.below(), starter);
    }

    //崩落確率
    static float collapseRate(Entity e, BlockState bs, CollapseStarter starter) {
        //ブロックごとの基本値、strength1.5以下で１以上
        float collapseRate = 1.5F * 2 / Math.max(bs.getBlock().defaultDestroyTime() + bs.getBlock().getExplosionResistance(), 0.1F);

        //崩落開始奴による係数
        collapseRate *= starter.getCollapseCoefficient();

        //エンティティの動きによる係数
        collapseRate *= e.isNoGravity() ? 0
                : walkDistNow.get() == 0 ||
                (e instanceof Player pl && pl.isShiftKeyDown()) ? 1 / 4F
                : e.isSprinting() ? 2
                : 1;
        collapseRate *= 1 + prevFallDistNow.get();

        return collapseRate;
    }

    //実際に崩落
    //TODO:ベッドみたいな複数ブロックで一つなやつも正常に落としたい
    private static void collapse(Entity e, BlockPos pos, CollapseStarter starter) {
        //高い確率で崩落したなら次も高い確率で崩落処理、高い崩落処理の原因となる落下距離は減衰させて
        if (e.level.random.nextFloat() < collapseRate(e, e.level.getBlockState(pos), starter))
            ServerHandler.addTickFunc(new ServerHandler.NextTickFunc() {//次tickに
                double prevWalkDist = walkDistNow.get();
                float prevPrevFallDist = prevFallDistNow.get();

                @Override
                public void func() {
                    if (!e.isRemoved())//次tickにエンティティがまだいたら(落下ダメで完全に死に終わってもまだ崩落が終わってないとき対策)
                        startCollapse(e, e.blockPosition().below(), prevWalkDist, Math.max(prevPrevFallDist - 2, 0));//落下距離は減衰させて前情報のままスタート
                }
            });
        //前情報使わなかったらまたクライアントからの情報提供が必要。
        //でもそれだとプレイヤーのprevFallDistを減衰させて次tickも処理、というのができない。減衰したことをクライアントに通知するまでに新たな落下距離０の崩落パケットが来る可能性がある。
        //ならサーバーでprevFallDist減衰→崩落をやってしまえばいい。
        //でもこれだと1tickで二回の崩落処理が行われることになる。まあ一度崩落が始まれば一気に崩落し易いって思えば多少はね？

        //崩落。FallingTileEntityにてTileの落下ブロックとしての挙動を修正
        FallingBlockEntity.fall(e.level, pos, e.level.getBlockState(pos));
        System.out.println(Thread.currentThread());

       /* var fallBlockEntity = FallingBlockEntity.fall(e.level, pos, e.level.getBlockState(pos));
        var be = e.level.getBlockEntity(pos);
        if (be != null)
            fallBlockEntity.blockData = be.saveWithoutMetadata();*/
    }
}
