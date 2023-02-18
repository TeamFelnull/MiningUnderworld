package dev.felnull.miningunderworld.item;

import dev.architectury.registry.CreativeTabRegistry;
import dev.felnull.miningunderworld.MiningUnderworld;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class MUCreativeModeTab {
    public static final CreativeTabRegistry.TabSupplier MOD_TAB = CreativeTabRegistry.create(new ResourceLocation(MiningUnderworld.MODID, MiningUnderworld.MODID), () -> new ItemStack(Items.APPLE));
}
