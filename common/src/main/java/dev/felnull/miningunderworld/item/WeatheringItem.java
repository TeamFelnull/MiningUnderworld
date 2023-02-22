package dev.felnull.miningunderworld.item;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import dev.felnull.miningunderworld.MiningUnderworld;
import dev.felnull.miningunderworld.mixin.ItemAccessor;
import dev.felnull.otyacraftengine.util.OENbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//銅ツールのように時間経過で錆びるアイテム
public interface WeatheringItem {
    List<Item> OXIDIZING_ITEMS = new ArrayList<>();
    UUID[] VANILLA_ARMOR_MODIFIER_UUID_PER_SLOT = new UUID[]{UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"), UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"), UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"), UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150")};

    default void registerWeatheringItems() {
        Preconditions.checkState(this instanceof Item, "Not item.");
        OXIDIZING_ITEMS.add((Item) this);
    }

    @NotNull
    static WeatheringState getWeatheringState(ItemStack stack) {
        var tag = stack.getTag();
        if (tag != null)
            return OENbtUtils.readEnum(tag, "WeatheringState", WeatheringState.class, WeatheringState.NONE);
        return WeatheringState.NONE;
    }

    static void setWeatheringState(ItemStack stack, @NotNull WeatheringState state) {
        var tag = stack.getOrCreateTag();
        OENbtUtils.writeEnum(tag, "WeatheringState", state);
    }

    static boolean isWaxed(ItemStack stack) {
        var tag = stack.getTag();
        return tag != null && tag.getBoolean("Waxed");
    }

    static void setWaxed(ItemStack stack, boolean waxed) {
        stack.getOrCreateTag().putBoolean("Waxed", waxed);
    }

    default void weatheringInventoryTick(ItemStack stack, Level level, Entity entity) {
        if (level.isClientSide())
            return;

        if (level.getRandom().nextFloat() > 1f / (12000f * 3f))
            return;

        nextStep(stack);
    }

    default Component getWeatheringName(ItemStack stack, Component defaultComponent) {
        Component wcomp;

        var state = getWeatheringState(stack);
        if (state == WeatheringState.NONE) {
            wcomp = defaultComponent;
        } else {
            wcomp = Component.translatable("item.wrap." + MiningUnderworld.MODID + "." + state.getSerializedName(), defaultComponent);
        }

        if (isWaxed(stack))
            wcomp = Component.translatable("item.wrap." + MiningUnderworld.MODID + ".waxed", wcomp);

        return wcomp;
    }

    default void nextStep(ItemStack stack) {
        if (isWaxed(stack))
            return;

        var ox = getWeatheringState(stack);
        if (ox.ordinal() >= WeatheringState.values().length - 1)
            return;

        setWeatheringState(stack, WeatheringState.values()[ox.ordinal() + 1]);
    }

    default Multimap<Attribute, AttributeModifier> weatheringToolAttribute(WeatheringState state, EquipmentSlot slot, Multimap<Attribute, AttributeModifier> defaultAttributeModifiers) {
        if (state == WeatheringState.NONE)
            return defaultAttributeModifiers;

        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();

        for (Attribute attribute : defaultAttributeModifiers.keySet()) {
            var modifiers = defaultAttributeModifiers.get(attribute);

            if (attribute == Attributes.ATTACK_DAMAGE && !(this instanceof AxeItem) && slot == EquipmentSlot.MAINHAND) {

                for (AttributeModifier modifier : modifiers) {
                    if (ItemAccessor.getBaseAttackDamageUUID() == modifier.getId()) {
                        builder.put(attribute, new AttributeModifier(ItemAccessor.getBaseAttackDamageUUID(), "Weapon modifier", modifier.getAmount() + (0.3 * state.ordinal()), AttributeModifier.Operation.ADDITION));
                    } else {
                        builder.put(attribute, modifier);
                    }
                }

            } else if (attribute == Attributes.ARMOR_TOUGHNESS && this instanceof ArmorItem && slot.isArmor()) {
                var uuid = VANILLA_ARMOR_MODIFIER_UUID_PER_SLOT[slot.getIndex()];

                for (AttributeModifier modifier : modifiers) {
                    if (uuid.equals(modifier.getId())) {
                        builder.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(uuid, "Armor toughness", 0.5f * state.ordinal(), AttributeModifier.Operation.ADDITION));
                    } else {
                        builder.put(attribute, modifier);
                    }
                }

            } else {
                builder.putAll(attribute, modifiers);
            }
        }

        return builder.build();
    }

    default float weatheringDestroySpeed(ItemStack stack, BlockState blockState, float defaultSpeed) {
        var state = getWeatheringState(stack);
        if (state == WeatheringState.NONE)
            return defaultSpeed;

        return defaultSpeed > 1f ? defaultSpeed + ((float) state.ordinal() * 0.25f) : 1f;
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
