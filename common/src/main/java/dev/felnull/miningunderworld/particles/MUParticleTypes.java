package dev.felnull.miningunderworld.particles;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import dev.felnull.miningunderworld.MiningUnderworld;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;

public class MUParticleTypes {
    private static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(MiningUnderworld.MODID, Registries.PARTICLE_TYPE);
    public static final RegistrySupplier<SimpleParticleType> DRIPPING_TAR = register("dripping_tar", false);

    private static RegistrySupplier<SimpleParticleType> register(String name, boolean overrideLimiter) {
        return PARTICLE_TYPES.register(name, () -> new MUSimpleParticleType(overrideLimiter));
    }

    public static void init() {
        PARTICLE_TYPES.register();
    }
}
