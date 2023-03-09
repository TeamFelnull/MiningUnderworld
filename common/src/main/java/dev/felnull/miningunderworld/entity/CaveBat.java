package dev.felnull.miningunderworld.entity;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.level.Level;

public class CaveBat extends Bat {
    private int excretaTick;

    public CaveBat(EntityType<? extends Bat> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 12.0);
    }

    @Override
    public void tick() {
        if (this.isAlive()) {
            excretaTick++;

            if (excretaTick >= 20) {
                excretaTick = 0;

                if (!level.isClientSide) {
                    Excreta excreta = new Excreta(level, this);
                    this.level.addFreshEntity(excreta);

                    if (!isSilent()) {
                        this.level.playSound(null, this.getX(), this.getY(), this.getZ(),
                                SoundEvents.CHICKEN_EGG, this.getSoundSource(), 1.0F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
                    }

                }
            }
        }

        super.tick();
    }
}
