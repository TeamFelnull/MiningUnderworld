package dev.felnull.miningunderworld.data;

import dev.felnull.miningunderworld.entity.MUDamageSources;
import dev.felnull.otyacraftengine.data.CrossDataGeneratorAccess;
import dev.felnull.otyacraftengine.data.provider.DamageTypeTagsProviderWrapper;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageType;

import java.util.concurrent.CompletableFuture;

public class MUDamageTypeTagsProviderWrapper extends DamageTypeTagsProviderWrapper {


    public MUDamageTypeTagsProviderWrapper(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookup, CrossDataGeneratorAccess crossDataGeneratorAccess) {
        super(packOutput, lookup, crossDataGeneratorAccess);
    }

    @Override
    public void generateTag(TagProviderAccess<DamageType, TagAppenderWrapper<DamageType>> providerAccess) {
        providerAccess.tag(DamageTypeTags.IS_EXPLOSION).add(
                MUDamageSources.MINING_EXPLOSION,
                MUDamageSources.MINING_EXPLOSION_PLAYER);
    }
}
