package dev.felnull.miningunderworld.world;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.BlockEvent;
import dev.architectury.event.events.common.TickEvent;
import dev.architectury.utils.value.IntValue;
import dev.felnull.miningunderworld.block.CollapseStarter;
import dev.felnull.miningunderworld.entity.StrictFallingBlockEntity;
import dev.felnull.miningunderworld.world.dimension.MUBiomes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ServerHandler {
    private static final List<TickFunc> SCHEDULED_TICK_FUNCS = new ArrayList<>();//TickFunc内でTickFunc増やす際のキャッシュ
    private static final List<TickFunc> TICK_FUNCS = new ArrayList<>();
    private static final List<TickFunc> DEPOSED_TICK_FUNCS = new ArrayList<>();//TickFunc内でTickFunc消す際のキャッシュ

    public static void init() {
        TickEvent.SERVER_LEVEL_POST.register(ServerHandler::onServerLevelPostTick);
        BlockEvent.BREAK.register(ServerHandler::collapse);
    }

    private static EventResult collapse(Level level, BlockPos pos, BlockState blockState, ServerPlayer serverPlayer, @Nullable IntValue intValue) {
        if (level.getBiome(pos).is(MUBiomes.COLLAPSING_CAVE))
            collapseNeighbors(level, pos);
        return EventResult.pass();
    }

    public static void collapseNeighbors(Level level, BlockPos pos) {
        Direction.stream()
                .map(pos::relative)
                .forEach(nPos -> {
                    if (!level.getBlockState(nPos).canBeReplaced()//自分が落ちれるもので
                            && level.getBlockState(nPos.relative(Direction.DOWN)).canBeReplaced()//落ちれて
                            && level.random.nextFloat() < CollapseStarter.baseCollapseRate(level.getBlockState(nPos))) {//確率に勝てたら
                        StrictFallingBlockEntity.strictFall(level, nPos);
                        collapseNeighbors(level, nPos);
                    }
                });
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
