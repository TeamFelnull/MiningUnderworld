package dev.felnull.miningunderworld.explatform.forge;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableList;
import dev.felnull.miningunderworld.item.accessory.EquipmentAccessoryItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class MUEquipmentAccessoryExpectPlatformImpl {
    private static final Supplier<TagKey<Item>> RING_TAG = Suppliers.memoize(() -> ItemTags.create(new ResourceLocation(getModId(), "ring")));
    private static final Supplier<TagKey<Item>> BELT_TAG = Suppliers.memoize(() -> ItemTags.create(new ResourceLocation(getModId(), "belt")));

    public static String getModId() {
        return "curios";
    }

    public static List<TagKey<Item>> getRingItemTag() {
        return ImmutableList.of(RING_TAG.get());
    }

    public static void initAccessoryItem(EquipmentAccessoryItem equipmentAccessoryItem) {
    }

    public static TagKey<Item> getBeltItemTag() {
        return BELT_TAG.get();
    }

    public static boolean isEquipped(LivingEntity livingEntity, Predicate<ItemStack> predicate) {
        return CuriosApi.getCuriosHelper().findFirstCurio(livingEntity, predicate).isPresent();
    }

    public static boolean equip(Player player, ItemStack stack) {
        return false;
    }
}
