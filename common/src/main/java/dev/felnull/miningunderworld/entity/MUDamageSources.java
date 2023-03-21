package dev.felnull.miningunderworld.entity;

import dev.felnull.miningunderworld.util.MUUtils;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public interface MUDamageSources {

    Map<ResourceKey<DamageType>, DamageType> DATAPACK_CACHE = new HashMap<>();

    ResourceKey<DamageType> MINING_EXPLOSION = register(new DamageType("mining_explosion", DamageScaling.ALWAYS/*常時難易度の影響を受ける*/, 0.1F));
    ResourceKey<DamageType> MINING_EXPLOSION_PLAYER = register(new DamageType("mining_explosion.player", DamageScaling.ALWAYS, 0.1F));

    static ResourceKey<DamageType> register(DamageType type) {
        var key = ResourceKey.create(Registries.DAMAGE_TYPE, MUUtils.modLoc(type.msgId()));
        DATAPACK_CACHE.put(key, type);
        return key;
    }

    static RegistrySetBuilder addToBuilder(RegistrySetBuilder builder) {
        return builder.add(Registries.DAMAGE_TYPE, c -> DATAPACK_CACHE.forEach((c::register)));
    }

    static DamageSource miningExplosion(Entity cause, @Nullable Entity indirectCause) {
        var type = cause.level.registryAccess().lookup(Registries.DAMAGE_TYPE).get()
                .get(indirectCause != null ? MINING_EXPLOSION_PLAYER : MINING_EXPLOSION).get();
        return new DamageSource(type, cause, indirectCause);
    }
}
