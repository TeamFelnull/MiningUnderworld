package dev.felnull.miningunderworld.world;

import com.google.common.collect.Sets;
import com.google.common.collect.Streams;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.HashSet;
import java.util.Set;

public class DynamicSignal {
    private final Level level;
    private final LongOpenHashSet signals = new LongOpenHashSet();
    private final LongOpenHashSet preSignals = new LongOpenHashSet();
    private final Set<AABB> signalBoxes = new HashSet<>();

    public DynamicSignal(Level level) {
        this.level = level;
    }

    public boolean isSignal(BlockPos blockPos) {
        return signals.contains(blockPos.asLong());
    }

    public void putSignalBox(AABB aabb) {
        signalBoxes.add(aabb);
    }

    public void postTick() {
        preSignals.addAll(signals);
        signals.clear();

        for (AABB aabb : signalBoxes) {
            int sx = Mth.floor(aabb.minX);
            int sy = Mth.floor(aabb.minY);
            int sz = Mth.floor(aabb.minZ);

            int ex = Mth.ceil(aabb.maxX);
            int ey = Mth.ceil(aabb.maxY);
            int ez = Mth.ceil(aabb.maxZ);

            for (int i = sx; i < ex; i++) {
                for (int j = sy; j < ey; j++) {
                    for (int k = sz; k < ez; k++) {
                        var pos = new BlockPos(i, j, k);
                        signals.add(pos.asLong());
                    }
                }
            }
        }

        signalBoxes.clear();
        signals.trim();

        Streams.concat(Sets.difference(signals, preSignals).stream(), Sets.difference(preSignals, signals).stream())
                .map(BlockPos::of)
                .forEach(pos -> level.updateNeighborsAt(pos, level.getBlockState(pos).getBlock()));

        preSignals.clear();
        preSignals.trim();
    }
}
