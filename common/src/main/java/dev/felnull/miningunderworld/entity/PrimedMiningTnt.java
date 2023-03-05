package dev.felnull.miningunderworld.entity;

import dev.felnull.miningunderworld.entity.damagesource.MUDamageSources;
import dev.felnull.miningunderworld.mixin.PrimedTntAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Material;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class PrimedMiningTnt extends PrimedTnt {
    public PrimedMiningTnt(EntityType<? extends PrimedMiningTnt> entityType, Level level) {
        super(entityType, level);
    }

    public PrimedMiningTnt(Level level, double d, double e, double f, @Nullable LivingEntity livingEntity) {
        this(MUEntityTypes.MINING_TNT.get(), level);
        this.setPos(d, e, f);
        double g = level.random.nextDouble() * 6.2831854820251465;
        this.setDeltaMovement(-Math.sin(g) * 0.02, 0.20000000298023224, -Math.cos(g) * 0.02);
        this.setFuse(80);
        this.xo = d;
        this.yo = e;
        this.zo = f;
        ((PrimedTntAccessor) this).setOwner(livingEntity);
    }

    @Override
    public void tick() {
        if (!this.isNoGravity()) {
            this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.04, 0.0));
        }

        this.move(MoverType.SELF, this.getDeltaMovement());
        this.setDeltaMovement(this.getDeltaMovement().scale(0.98));
        if (this.onGround) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(0.7, -0.5, 0.7));
        }

        int i = this.getFuse() - 1;
        this.setFuse(i);
        if (i <= 0) {
            this.discard();
            if (!this.level.isClientSide) {
                this.miningExplode();
            }
        } else {
            this.updateInWaterStateAndDoFluidPushing();
            if (this.level.isClientSide) {
                this.level.addParticle(ParticleTypes.SMOKE, this.getX(), this.getY() + 0.5, this.getZ(), 0.0, 0.0, 0.0);
            }
        }
    }

    private void miningExplode() {
        this.level.explode(this, MUDamageSources.miningExplosion(this, this.getOwner()),
                new ExplosionDamageCalculator() {
                    @Override
                    public Optional<Float> getBlockExplosionResistance(Explosion explosion, BlockGetter blockGetter, BlockPos blockPos, BlockState blockState, FluidState fluidState) {
                        if (!fluidState.isEmpty())
                            return Optional.of(0f);

                        return super.getBlockExplosionResistance(explosion, blockGetter, blockPos, blockState, fluidState)
                                .map(it -> canMining(blockState) ? Math.max(it * 0.1f, 0.5f) : it);
                    }
                }, this.getX(), this.getY(0.0625), this.getZ(), 4.0F, false, Level.ExplosionInteraction.TNT);
    }


    private boolean canMining(BlockState blockState) {
        //ピッケルが最適ツールではない場合は除外
        if (!blockState.is(BlockTags.MINEABLE_WITH_PICKAXE)) return false;

        //採掘にダイヤモンド以上が必要な場合は除外
        if (blockState.is(BlockTags.NEEDS_DIAMOND_TOOL)) return false;

        //破壊不可のものを除外
        if (blockState.getBlock().defaultDestroyTime() < 0) return false;

        return blockState.getMaterial() == Material.STONE || blockState.is(BlockTags.BASE_STONE_OVERWORLD) || blockState.is(BlockTags.BASE_STONE_NETHER);
    }
}
