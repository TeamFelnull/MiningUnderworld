package dev.felnull.miningunderworld.client.particle;

import dev.felnull.miningunderworld.fluid.MUFluids;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.DripParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.Nullable;

public class TarParticle {
    public static class TarHangProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;

        public TarHangProvider(SpriteSet sprite) {
            this.sprite = sprite;
        }

        @Nullable
        @Override
        public Particle createParticle(SimpleParticleType particleOptions, ClientLevel clientLevel, double d, double e, double f, double g, double h, double i) {
            DripParticle dripParticle = new TarDripParticle(clientLevel, d, e, f, MUFluids.TAR.get());
            dripParticle.setColor(0f, 0f, 0f);
            dripParticle.pickSprite(this.sprite);

            return dripParticle;
        }
    }

    private static class TarDripParticle extends DripParticle {

        public TarDripParticle(ClientLevel clientLevel, double d, double e, double f, Fluid fluid) {
            super(clientLevel, d, e, f, fluid);
        }
    }
}
