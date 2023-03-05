package dev.felnull.miningunderworld.block;

import dev.felnull.miningunderworld.entity.PrimedMiningTnt;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.Nullable;

/**
 * 採掘用のTNT
 * 液体を破壊可能であり、石系は壊れやすくなり、アイテムが爆風で消滅しない
 */
public class MiningTntBlock extends TntBlock {
    public MiningTntBlock(Properties properties) {
        super(properties);
    }

    public static void miningExplode(Level level, BlockPos blockPos, @Nullable LivingEntity livingEntity) {
        if (!level.isClientSide) {
            PrimedMiningTnt primedTnt = new PrimedMiningTnt(level, (double) blockPos.getX() + 0.5, blockPos.getY(), (double) blockPos.getZ() + 0.5, livingEntity);
            level.addFreshEntity(primedTnt);
            level.playSound(null, primedTnt.getX(), primedTnt.getY(), primedTnt.getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
            level.gameEvent(livingEntity, GameEvent.PRIME_FUSE, blockPos);
        }
    }

    @Override
    public void wasExploded(Level level, BlockPos blockPos, Explosion explosion) {
        if (!level.isClientSide) {
            PrimedMiningTnt primedTnt = new PrimedMiningTnt(level, (double) blockPos.getX() + 0.5, (double) blockPos.getY(), (double) blockPos.getZ() + 0.5, explosion.getIndirectSourceEntity());
            int i = primedTnt.getFuse();
            primedTnt.setFuse((short) (level.random.nextInt(i / 4) + i / 8));
            level.addFreshEntity(primedTnt);
        }
    }
}
