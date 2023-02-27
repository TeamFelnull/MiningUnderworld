package dev.felnull.miningunderworld.data;

import dev.felnull.miningunderworld.item.MUItems;
import dev.felnull.otyacraftengine.data.CrossDataGeneratorAccess;
import dev.felnull.otyacraftengine.data.provider.BlockTagProviderWrapper;
import dev.felnull.otyacraftengine.data.provider.ItemTagProviderWrapper;
import dev.felnull.otyacraftengine.tag.PlatformItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class MUItemTagProviderWrapper extends ItemTagProviderWrapper {
    public MUItemTagProviderWrapper(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookup, CrossDataGeneratorAccess crossDataGeneratorAccess, @NotNull BlockTagProviderWrapper blockTagProviderWrapper) {
        super(packOutput, lookup, crossDataGeneratorAccess, blockTagProviderWrapper);
    }

    @Override
    public void generateTag(ItemTagProviderAccess providerAccess) {
        providerAccess.tag(PlatformItemTags.swords())
                .add(MUItems.COPPER_SWORD.get(), MUItems.EMERALD_SWORD.get(), MUItems.AMETHYST_SWORD.get(),MUItems.REDSTONE_SWORD.get(),MUItems.LAPIS_LAZULI_SWORD.get());

        providerAccess.tag(PlatformItemTags.pickaxes())
                .add(MUItems.COPPER_PICKAXE.get(), MUItems.EMERALD_PICKAXE.get(), MUItems.AMETHYST_PICKAXE.get(),MUItems.REDSTONE_PICKAXE.get(),MUItems.LAPIS_LAZULI_PICKAXE.get());

        providerAccess.tag(PlatformItemTags.axes())
                .add(MUItems.COPPER_AXE.get(), MUItems.EMERALD_AXE.get(), MUItems.AMETHYST_AXE.get(),MUItems.REDSTONE_AXE.get(),MUItems.LAPIS_LAZULI_AXE.get());

        providerAccess.tag(PlatformItemTags.shovels())
                .add(MUItems.COPPER_SHOVEL.get(), MUItems.EMERALD_SHOVEL.get(), MUItems.AMETHYST_SHOVEL.get(),MUItems.REDSTONE_SHOVEL.get(),MUItems.LAPIS_LAZULI_SHOVEL.get());
    }
}
