package dev.felnull.miningunderworld.entity.damagesource;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

public class MUDamageSources {

    public static DamageSource miningExplosion(@Nullable Entity target, @Nullable Entity attacker) {
        if (target != null && attacker != null)
            return new MiningIndirectEntityDamageSource("mining_explosion.player", target, attacker)
                    .setScalesWithDifficulty()
                    .setExplosion();

        return target != null ? new MiningEntityDamageSource("mining_explosion", target)
                .setScalesWithDifficulty()
                .setExplosion()
                : new MiningDamageSource("mining_explosion")
                .setScalesWithDifficulty()
                .setExplosion();
    }
}
