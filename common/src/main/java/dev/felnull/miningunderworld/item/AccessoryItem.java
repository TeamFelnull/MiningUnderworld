package dev.felnull.miningunderworld.item;

import dev.felnull.miningunderworld.MiningUnderworld;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AccessoryItem extends Item {
    private final MobEffect alwaysAffect;
    private final MobEffect specialEffect;
    private final Type type;

    public AccessoryItem(MobEffect alwaysAffect, MobEffect specialEffect, Type type, Properties properties) {
        super(properties);
        this.alwaysAffect = alwaysAffect;
        this.specialEffect = specialEffect;
        this.type = type;
    }

    @Override
    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int slot, boolean inHand) {
        if (!level.isClientSide() && entity instanceof LivingEntity livingEntity)
            livingEntity.addEffect(type.getAlwaysEffectInstance(alwaysAffect));
    }

    @Override//アイテム選択
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        if (!level.isClientSide())
            player.addEffect(type.getSpecialEffectInstance(specialEffect));
        return super.use(level, player, interactionHand);
    }

    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        list.add(Component.translatable( "item." + MiningUnderworld.MODID + ".accessory.desc", specialEffect.getDisplayName()));
    }

    public enum Type {
        RING,
        SOUL;

        public MobEffectInstance getAlwaysEffectInstance(MobEffect effect){
            return switch (this) {
                case RING -> new MobEffectInstance(effect, 20, 3);
                case SOUL -> new MobEffectInstance(effect, 500, 1);
            };
        }

        public MobEffectInstance getSpecialEffectInstance(MobEffect effect){
            return switch (this) {
                case RING -> new MobEffectInstance(effect, 500, 1);
                case SOUL -> new MobEffectInstance(effect, 200, 1);
            };
        }
    }
}


