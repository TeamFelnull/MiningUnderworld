package dev.felnull.miningunderworld.server.handler;

import dev.architectury.event.events.common.TickEvent;
import dev.felnull.miningunderworld.world.DynamicSignalLevel;
import net.minecraft.server.level.ServerLevel;

import java.util.ArrayList;
import java.util.List;

public class ServerHandler {
    private static final List<TickFunc> SCHEDULED_TICK_FUNCS = new ArrayList<>();//TickFunc内でTickFunc増やす際のキャッシュ
    private static final List<TickFunc> TICK_FUNCS = new ArrayList<>();
    private static final List<TickFunc> DEPOSED_TICK_FUNCS = new ArrayList<>();//TickFunc内でTickFunc消す際のキャッシュ

    public static void init() {
        TickEvent.SERVER_LEVEL_POST.register(ServerHandler::onServerLevelPostTick);
    }

    private static void onServerLevelPostTick(ServerLevel level) {
        ((DynamicSignalLevel) level).getDynamicSignal().postTick();

        TICK_FUNCS.addAll(SCHEDULED_TICK_FUNCS);
        SCHEDULED_TICK_FUNCS.clear();
        TICK_FUNCS.forEach(TickFunc::tick);
        TICK_FUNCS.removeAll(DEPOSED_TICK_FUNCS);
        DEPOSED_TICK_FUNCS.clear();
    }

    public static void addTickFunc(TickFunc func) {
        SCHEDULED_TICK_FUNCS.add(func);
    }

    public static abstract class TickFunc {

        public void tick() {
            if (!isValid())
                depose();
            else
                func();
        }

        public void depose() {
            DEPOSED_TICK_FUNCS.add(this);
        }

        public abstract boolean isValid();

        public abstract void func();
    }

    public static abstract class NextTickFunc extends TickFunc {
        private boolean hasProcessed;

        @Override
        public void tick() {
            super.tick();
            hasProcessed = true;
        }

        @Override
        public boolean isValid() {
            return !hasProcessed;
        }
    }
}
