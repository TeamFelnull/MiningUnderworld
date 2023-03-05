package dev.felnull.miningunderworld.entity.damagesource;

import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.Entity;

public class MiningEntityDamageSource extends EntityDamageSource implements IMiningDamageSource {
    public MiningEntityDamageSource(String string, Entity entity) {
        super(string, entity);
    }
}
