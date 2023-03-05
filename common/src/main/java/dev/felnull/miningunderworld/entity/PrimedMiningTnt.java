package dev.felnull.miningunderworld.entity;

import dev.felnull.miningunderworld.entity.damagesource.MUDamageSources;
import dev.felnull.miningunderworld.mixin.PrimedTntAccessor;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

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
                null, this.getX(), this.getY(0.0625), this.getZ(), 4.0F, false, Level.ExplosionInteraction.TNT);
    }
}
