package dev.felnull.miningunderworld.entity.damagesource;

import net.minecraft.world.damagesource.DamageSource;

public class MiningDamageSource extends DamageSource implements IMiningDamageSource {
    protected MiningDamageSource(String string) {
        super(string);
    }
}
