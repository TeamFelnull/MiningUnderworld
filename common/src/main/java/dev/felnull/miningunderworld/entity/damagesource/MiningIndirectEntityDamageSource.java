package dev.felnull.miningunderworld.entity.damagesource;

import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

public class MiningIndirectEntityDamageSource extends IndirectEntityDamageSource implements IMiningDamageSource {
    public MiningIndirectEntityDamageSource(String string, Entity entity, @Nullable Entity entity2) {
        super(string, entity, entity2);
    }
}
