package dev.felnull.miningunderworld.item;

import dev.felnull.miningunderworld.util.MUUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public final class MUItemTags {
    public static final TagKey<Item> SLIMEBALLS = bind("slime_balls");
    public static final TagKey<Item> SWORDS = bind("swords");
    public static final TagKey<Item> PICKAXES = bind("pickaxes");
    public static final TagKey<Item> AXES = bind("axes");
    public static final TagKey<Item> SHOVELS = bind("shovels");

    private static TagKey<Item> bind(String id) {
        return TagKey.create(Registries.ITEM, MUUtils.modLoc(id));
    }
}
