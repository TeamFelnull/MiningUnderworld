package dev.felnull.miningunderworld.server.handler;

import dev.architectury.event.events.common.TickEvent;
import dev.felnull.miningunderworld.world.DynamicSignalLevel;
import net.minecraft.server.level.ServerLevel;

public class ServerHandler {
    public static void init() {
        TickEvent.SERVER_LEVEL_POST.register(ServerHandler::onServerLevelPostTick);
    }

    private static void onServerLevelPostTick(ServerLevel level) {
        ((DynamicSignalLevel) level).getDynamicSignal().postTick();
    }
}
