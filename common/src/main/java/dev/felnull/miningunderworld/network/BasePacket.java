package dev.felnull.miningunderworld.network;

import dev.architectury.networking.NetworkManager;
import dev.felnull.otyacraftengine.networking.PacketMessage;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public abstract class BasePacket implements PacketMessage {
    @Override
    public FriendlyByteBuf toFBB(FriendlyByteBuf buf) {
        encode(buf);
        return buf;
    }

    public abstract FriendlyByteBuf encode(FriendlyByteBuf buf);

    public abstract BasePacket decode(FriendlyByteBuf buf);

    public void handle(NetworkManager.PacketContext c) {
        if (c.getPlayer() instanceof ServerPlayer sp)
            c.queue(() -> handleOnServer(sp));
        else
            c.queue(() -> handleOnClient(c.getPlayer()));

    }

    public void handleOnServer(ServerPlayer sp) {
    }

    public void handleOnClient(Player p) {
    }

    public ResourceLocation getLoc() {
        return MUPackets.PACKET_LOCATIONS.get(this.getClass());
    }

    public void sendToServer() {
        NetworkManager.sendToServer(getLoc(), this.toFBB());
    }

    public void sendToClient(ServerPlayer sp) {
        NetworkManager.sendToPlayer(sp, getLoc(), this.toFBB());
    }

    public static abstract class arg1<A> extends BasePacket {

        public abstract BasePacket init(A a);
    }

    public static abstract class arg2<A, B> extends BasePacket {

        public abstract BasePacket init(A a, B b);
    }

    public static abstract class arg3<A, B, C> extends BasePacket {

        public abstract BasePacket init(A a, B b, C c);
    }

    public static abstract class arg4<A, B, C, D> extends BasePacket {

        public abstract BasePacket init(A a, B b, C c, D d);
    }

    public static abstract class arg5<A, B, C, D, E> extends BasePacket {

        public abstract BasePacket init(A a, B b, C c, D d, E e);
    }

    public static abstract class arg6<A, B, C, D, E, F> extends BasePacket {

        public abstract BasePacket init(A a, B b, C c, D d, E e, F f);
    }
}
