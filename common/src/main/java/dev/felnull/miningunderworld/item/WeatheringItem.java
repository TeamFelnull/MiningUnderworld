package dev.felnull.miningunderworld.item;

import com.google.common.base.Preconditions;
import dev.felnull.otyacraftengine.util.OENbtUtils;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public interface WeatheringItem {
    List<Item> OXIDIZING_ITEMS = new ArrayList<>();

    default void registerWeatheringItems() {
        Preconditions.checkState(this instanceof Item, "Not item.");
        OXIDIZING_ITEMS.add((Item) this);
    }

    static WeatheringState getWeatheringState(ItemStack stack) {
        var tag = stack.getTag();
        if (tag != null)
            return OENbtUtils.readEnum(tag, "WeatheringState", WeatheringState.class, WeatheringState.NONE);
        return WeatheringState.NONE;
    }

    static void setWeatheringState(ItemStack stack, WeatheringState state) {
        var tag = stack.getOrCreateTag();
        OENbtUtils.writeEnum(tag, "WeatheringState", state);
    }

    default void weatheringInventoryTick(ItemStack stack, Level level, Entity entity) {
        if (level.isClientSide())
            return;

        //10分に1回の確率 12000f
        if (level.getRandom().nextFloat() > 1f / 120f)
            return;

        nextStep(stack);
    }

    default void nextStep(ItemStack stack) {
        var ox = getWeatheringState(stack);
        if (ox.ordinal() >= WeatheringState.values().length - 1)
            return;

        setWeatheringState(stack, WeatheringState.values()[ox.ordinal() + 1]);
    }

    enum WeatheringState implements StringRepresentable {
        NONE("none"),
        EXPOSED("exposed"),
        WEATHERED("weathered"),
        OXIDIZED("oxidized");

        private final String name;

        WeatheringState(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }
    }
}