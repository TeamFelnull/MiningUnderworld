package dev.felnull.miningunderworld.network;

import com.google.common.base.CaseFormat;
import com.mojang.datafixers.util.Function3;
import com.mojang.datafixers.util.Function4;
import com.mojang.datafixers.util.Function5;
import com.mojang.datafixers.util.Function6;
import dev.architectury.networking.NetworkManager;
import dev.felnull.miningunderworld.util.MUUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.*;

public class MUPackets {

    public static final Map<Class<? extends BasePacket>, ResourceLocation> PACKET_LOCATIONS = new HashMap<>();
    public static final Map<ResourceLocation, Supplier<? extends BasePacket>> MU_C2S = new HashMap<>();
    public static final Map<ResourceLocation, Supplier<? extends BasePacket>> MU_S2C = new HashMap<>();

    public static Consumer<Vec3> PLAYER_MOVING_ON_COLLAPSING =
            sendToServer1(//パケットを初期化する引数をacceptするだけでサーバーに送れる形
                    registerC2S(//C2Sで登録
                            PlayerMovingOnCollapsingPacket::new));//パケット本体。handleが実際の処理

    public static <T extends BasePacket> Supplier<T> registerC2S(Supplier<T> packetSup) {
        return register(packetSup, MU_C2S::put);
    }

    public static <T extends BasePacket> Supplier<T> registerS2C(Supplier<T> packetSup) {
        return register(packetSup, MU_S2C::put);
    }

    public static <T extends BasePacket> Supplier<T> registerTwoWay(Supplier<T> packetSup) {
        return register(packetSup, MU_C2S::put, MU_S2C::put);
    }

    public static <T extends BasePacket> Supplier<T> register(Supplier<T> packetSup, BiConsumer<ResourceLocation, Supplier<T>>... caches) {
        var c = packetSup.get().getClass();
        var loc = MUUtils.modLoc(CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, c.getSimpleName().replaceAll("Packet", "")));
        Arrays.stream(caches).forEach(cache -> cache.accept(loc, packetSup));
        PACKET_LOCATIONS.put(c, loc);
        return packetSup;
    }

    public static void initServer() {
        MU_C2S.forEach((loc, packetSup) -> NetworkManager.registerReceiver(NetworkManager.c2s(), loc, (fbb, context) -> packetSup.get().decode(fbb).handle(context)));
    }

    public static void initClient() {
        MU_S2C.forEach((loc, packetSup) -> NetworkManager.registerReceiver(NetworkManager.s2c(), loc, (fbb, context) -> packetSup.get().decode(fbb).handle(context)));
    }

    private static <A, T extends BasePacket.arg1<A>> Function<A, T> func1(Supplier<T> packetSup) {
        return a -> (T) packetSup.get().init(a);
    }

    private static <A, B, T extends BasePacket.arg2<A, B>> BiFunction<A, B, T> func2(Supplier<T> packetSup) {
        return (a, b) -> (T) packetSup.get().init(a, b);
    }

    private static <A, B, C, T extends BasePacket.arg3<A, B, C>> Function3<A, B, C, T> func3(Supplier<T> packetSup) {
        return (a, b, c) -> (T) packetSup.get().init(a, b, c);
    }

    private static <A, B, C, D, T extends BasePacket.arg4<A, B, C, D>> Function4<A, B, C, D, T> func4(Supplier<T> packetSup) {
        return (a, b, c, d) -> (T) packetSup.get().init(a, b, c, d);
    }

    private static <A, B, C, D, E, T extends BasePacket.arg5<A, B, C, D, E>> Function5<A, B, C, D, E, T> func5(Supplier<T> packetSup) {
        return (a, b, c, d, e) -> (T) packetSup.get().init(a, b, c, d, e);
    }

    private static <A, B, C, D, E, F, T extends BasePacket.arg6<A, B, C, D, E, F>> Function6<A, B, C, D, E, F, T> func6(Supplier<T> packetSup) {
        return (a, b, c, d, e, f) -> (T) packetSup.get().init(a, b, c, d, e, f);
    }

    private static <A, T extends BasePacket.arg1<A>> Consumer<A> sendToServer1(Supplier<T> packetSup) {
        return a -> func1(packetSup).apply(a).sendToServer();
    }

    private static <A, B, T extends BasePacket.arg2<A, B>> BiConsumer<A, B> sendToServer2(Supplier<T> packetSup) {
        return (a, b) -> func2(packetSup).apply(a, b).sendToServer();
    }

    private static <A, B, C, T extends BasePacket.arg3<A, B, C>> Consumer3<A, B, C> sendToServer3(Supplier<T> packetSup) {
        return (a, b, c) -> func3(packetSup).apply(a, b, c).sendToServer();
    }

    private static <A, B, C, D, T extends BasePacket.arg4<A, B, C, D>> Consumer4<A, B, C, D> sendToServer4(Supplier<T> packetSup) {
        return (a, b, c, d) -> func4(packetSup).apply(a, b, c, d).sendToServer();
    }

    private static <A, B, C, D, E, T extends BasePacket.arg5<A, B, C, D, E>> Consumer5<A, B, C, D, E> sendToServer5(Supplier<T> packetSup) {
        return (a, b, c, d, e) -> func5(packetSup).apply(a, b, c, d, e).sendToServer();
    }

    private static <A, B, C, D, E, F, T extends BasePacket.arg6<A, B, C, D, E, F>> Consumer6<A, B, C, D, E, F> sendToServer6(Supplier<T> packetSup) {
        return (a, b, c, d, e, f) -> func6(packetSup).apply(a, b, c, d, e, f).sendToServer();
    }

    public interface Consumer3<A, B, C> {
        void accept(A a, B b, C c);
    }

    public interface Consumer4<A, B, C, D> {
        void accept(A a, B b, C c, D d);
    }

    public interface Consumer5<A, B, C, D, E> {
        void accept(A a, B b, C c, D d, E e);
    }

    public interface Consumer6<A, B, C, D, E, F> {
        void accept(A a, B b, C c, D d, E e, F f);
    }
}
