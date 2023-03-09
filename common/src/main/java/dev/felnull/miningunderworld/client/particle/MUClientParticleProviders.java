package dev.felnull.miningunderworld.client.particle;

import dev.architectury.registry.client.particle.ParticleProviderRegistry;
import dev.felnull.miningunderworld.particles.MUParticleTypes;

public class MUClientParticleProviders {
    public static void init() {
        ParticleProviderRegistry.register(MUParticleTypes.DRIPPING_TAR, TarParticle.TarHangProvider::new);
    }
}
