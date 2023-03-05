package dev.felnull.miningunderworld.network;

import dev.architectury.networking.NetworkManager;
import dev.felnull.miningunderworld.block.CollapseStarter;
import dev.felnull.miningunderworld.entity.PrevFallDistanceEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

public class PlayerStartCollapsePacket extends BasePacket.arg2<Double, Float> {//フィールドの数とその型
    public double walkDist;
    public float prevFallDist;

    @Override//フィールドをバイト列に
    public FriendlyByteBuf encode(FriendlyByteBuf buf) {
        buf.writeDouble(walkDist);
        buf.writeFloat(prevFallDist);
        return buf;
    }

    @Override//バイト列からフィールド復元
    public BasePacket decode(FriendlyByteBuf buf) {
        walkDist = buf.readDouble();
        prevFallDist = buf.readFloat();
        return this;
    }

    @Override//フィールドを介して送られた情報を処理
    public void handle(NetworkManager.PacketContext c) {
        if (c.getPlayer() instanceof ServerPlayer sp) {//なんかServerPlayer以外が想定されてる。何故
            CollapseStarter.startCollapse(sp, sp.blockPosition().below(), walkDist, prevFallDist);
            System.out.println("Server received:" + prevFallDist);
            System.out.println("Server now:" + ((PrevFallDistanceEntity) sp).getPrevFallDistance());
        }
    }

    @Override//初期化。デフォルトコンストラクタが使いたいからメソッドでやってる
    public BasePacket init(Double d, Float f) {
        walkDist = d;
        prevFallDist = f;
        return this;
    }
}
