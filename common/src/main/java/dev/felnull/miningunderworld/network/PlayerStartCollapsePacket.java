package dev.felnull.miningunderworld.network;

import dev.felnull.miningunderworld.block.CollapseStarter;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

public class PlayerStartCollapsePacket extends BasePacket.arg1<Double> {//フィールドの数とその型
    public double walkDist;

    @Override//フィールドをバイト列に
    public FriendlyByteBuf encode(FriendlyByteBuf buf) {
        buf.writeDouble(walkDist);
        return buf;
    }

    @Override//バイト列からフィールド復元
    public BasePacket decode(FriendlyByteBuf buf) {
        walkDist = buf.readDouble();
        return this;
    }

    @Override
    public void handleOnServer(ServerPlayer sp) {
        CollapseStarter.startCollapse(sp, sp.blockPosition().below(), walkDist);
    }

    @Override//初期化。デフォルトコンストラクタが使いたいからメソッドでやってる
    public BasePacket init(Double d) {
        walkDist = d;
        return this;
    }
}
