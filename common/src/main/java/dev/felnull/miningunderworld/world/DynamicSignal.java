package dev.felnull.miningunderworld.world;

import com.google.common.collect.Sets;
import com.google.common.collect.Streams;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public class DynamicSignal {
    private final Level level;
    private final LongOpenHashSet signals = new LongOpenHashSet();
    private final LongOpenHashSet preSignals = new LongOpenHashSet();
    private final LongOpenHashSet nextSignals = new LongOpenHashSet();

    public DynamicSignal(Level level) {
        this.level = level;
    }

    public boolean isSignal(BlockPos blockPos) {
        return signals.contains(blockPos.asLong());
    }

    public void putSignalBox(AABB aabb) {
        int sx = Mth.floor(aabb.minX);
        int sy = Mth.floor(aabb.minY);
        int sz = Mth.floor(aabb.minZ);

        int ex = Mth.ceil(aabb.maxX);
        int ey = Mth.ceil(aabb.maxY);
        int ez = Mth.ceil(aabb.maxZ);

        for (int x = sx; x < ex; x++) {
            for (int y = sy; y < ey; y++) {
                for (int z = sz; z < ez; z++) {
                    var pos = new BlockPos(x, y, z);
                    nextSignals.add(pos.asLong());
                }
            }
        }
    }

    public void postTick() {
        preSignals.addAll(signals);
        signals.clear();
        signals.addAll(nextSignals);
        signals.trim();

        nextSignals.clear();
        nextSignals.trim();

        if (!level.isClientSide()) {
            Streams.concat(Sets.difference(signals, preSignals).stream(), Sets.difference(preSignals, signals).stream())
                    .map(BlockPos::of)
                    .forEach(pos -> level.updateNeighborsAt(pos, level.getBlockState(pos).getBlock()));
        }

        preSignals.clear();
        preSignals.trim();
    }
}
