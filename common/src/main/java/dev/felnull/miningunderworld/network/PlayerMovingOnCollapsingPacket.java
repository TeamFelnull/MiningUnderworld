package dev.felnull.miningunderworld.network;

import dev.architectury.networking.NetworkManager;
import dev.felnull.miningunderworld.block.CollapsingBlock;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

public class PlayerMovingOnCollapsingPacket extends BasePacket.arg1<Vec3> {//フィールドの数とその型
    public Vec3 movement;

    @Override//フィールドをバイト列に
    public FriendlyByteBuf encode(FriendlyByteBuf buf) {
        buf.writeDouble(movement.x).writeDouble(movement.y).writeDouble(movement.z);
        return buf;
    }

    @Override//バイト列からフィールド復元
    public BasePacket decode(FriendlyByteBuf buf) {
        movement = new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
        return this;
    }

    @Override//フィールドを介して送られた情報を処理
    public void handle(NetworkManager.PacketContext c) {
        if(c.getPlayer() instanceof ServerPlayer sp){//なんかServerPlayer以外が想定されてる。何故
            sp.setDeltaMovement(movement);//サーバー側には本来ない情報だけど無理やり入れる
            CollapsingBlock.startCollapse(sp, sp.blockPosition().below());//この状態なら他のEntityと同様に処理できる
        }
    }

    @Override//初期化。デフォルトコンストラクタが使いたいからメソッドでやってる
    public BasePacket init(Vec3 vec3) {
        movement = vec3;
        return this;
    }
}
