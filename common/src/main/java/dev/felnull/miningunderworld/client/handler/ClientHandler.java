package dev.felnull.miningunderworld.client.handler;

import dev.architectury.event.events.client.ClientTickEvent;
import dev.felnull.miningunderworld.world.DynamicSignalLevel;
import net.minecraft.client.multiplayer.ClientLevel;

public class ClientHandler {
    public static void init() {
        ClientTickEvent.CLIENT_LEVEL_POST.register(ClientHandler::onClientLevelPostTick);
    }

    private static void onClientLevelPostTick(ClientLevel level) {
        ((DynamicSignalLevel) level).getDynamicSignal().postTick();
    }
}
