package dev.felnull.miningunderworld.data;

import dev.felnull.miningunderworld.item.MUItemTags;
import dev.felnull.miningunderworld.item.MUItems;
import dev.felnull.miningunderworld.item.accessory.EquipmentAccessoryItem;
import dev.felnull.otyacraftengine.data.CrossDataGeneratorAccess;
import dev.felnull.otyacraftengine.data.provider.BlockTagProviderWrapper;
import dev.felnull.otyacraftengine.data.provider.ItemTagProviderWrapper;
import dev.felnull.otyacraftengine.tag.PlatformItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.StreamSupport;

public class MUItemTagProviderWrapper extends ItemTagProviderWrapper {
    public MUItemTagProviderWrapper(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookup, CrossDataGeneratorAccess crossDataGeneratorAccess, @NotNull BlockTagProviderWrapper blockTagProviderWrapper) {
        super(packOutput, lookup, crossDataGeneratorAccess, blockTagProviderWrapper);
    }

    @Override
    public void generateTag(ItemTagProviderAccess providerAccess) {
        providerAccess.tag(ItemTags.SWORDS).addTag(MUItemTags.SWORDS);
        PlatformItemTags.swords().ifPresent(tag -> providerAccess.tag(tag).addTag(MUItemTags.SWORDS));

        providerAccess.tag(ItemTags.PICKAXES).addTag(MUItemTags.PICKAXES);
        PlatformItemTags.pickaxes().ifPresent(tag -> providerAccess.tag(tag).addTag(MUItemTags.PICKAXES));

        providerAccess.tag(ItemTags.AXES).addTag(MUItemTags.AXES);
        PlatformItemTags.axes().ifPresent(tag -> providerAccess.tag(tag).addTag(MUItemTags.AXES));

        providerAccess.tag(ItemTags.SHOVELS).addTag(MUItemTags.SHOVELS);
        PlatformItemTags.shovels().ifPresent(tag -> providerAccess.tag(tag).addTag(MUItemTags.SHOVELS));

        providerAccess.tag(MUItemTags.SLIMEBALLS)
                .add(MUItems.TAR_CHUNK.get());

        providerAccess.tag(MUItemTags.SWORDS)
                .add(MUItems.COPPER_SWORD.get(),
                        MUItems.EMERALD_SWORD.get(),
                        MUItems.AMETHYST_SWORD.get(),
                        MUItems.REDSTONE_SWORD.get(),
                        MUItems.LAPIS_LAZULI_SWORD.get());

        providerAccess.tag(MUItemTags.PICKAXES)
                .add(MUItems.COPPER_PICKAXE.get(),
                        MUItems.EMERALD_PICKAXE.get(),
                        MUItems.AMETHYST_PICKAXE.get(),
                        MUItems.REDSTONE_PICKAXE.get(),
                        MUItems.LAPIS_LAZULI_PICKAXE.get());

        providerAccess.tag(MUItemTags.AXES)
                .add(MUItems.COPPER_AXE.get(),
                        MUItems.EMERALD_AXE.get(),
                        MUItems.AMETHYST_AXE.get(),
                        MUItems.REDSTONE_AXE.get(),
                        MUItems.LAPIS_LAZULI_AXE.get());

        providerAccess.tag(MUItemTags.SHOVELS)
                .add(MUItems.COPPER_SHOVEL.get(),
                        MUItems.EMERALD_SHOVEL.get(),
                        MUItems.AMETHYST_SHOVEL.get(),
                        MUItems.REDSTONE_SHOVEL.get(),
                        MUItems.LAPIS_LAZULI_SHOVEL.get());

        providerAccess.tag(PlatformItemTags.clayBalls().getKey())
                .add(MUItems.BLUE_CLAY_BALL.get());

        StreamSupport.stream(MUItems.ITEMS.spliterator(), false).map(Supplier::get).forEach(it -> {
            List<TagKey<Item>> tags;
            if (it instanceof EquipmentAccessoryItem equipmentAccessoryItem && (tags = equipmentAccessoryItem.getAccessoryType().getTagKeys()) != null)
                tags.forEach(tag -> providerAccess.tag(tag).add(it));
        });

        PlatformItemTags.stone().registering(providerAccess);
        PlatformItemTags.slimeBalls().forEach(tag -> {
            providerAccess.tag(tag.getKey()).addTag(MUItemTags.SLIMEBALLS);
            tag.registering(providerAccess);
        });
        PlatformItemTags.clayBalls().registering(providerAccess);
    }
}
