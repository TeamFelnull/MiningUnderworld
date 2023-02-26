package dev.felnull.miningunderworld.item;

import com.mojang.blaze3d.shaders.Effect;
import net.minecraft.client.gui.font.glyphs.BakedGlyph;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Clearable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AccessoryItem extends Item {
    private final MobEffect alwaysAffect;
    private final MobEffect specialEffect;
    private final String AccessoryName;
    private final String specialEffectName;

    public AccessoryItem(MobEffect alwaysAffect, MobEffect specialEffect, Properties properties, String AccessoryName, String specialEffectName, Properties stacksTo, Properties rarity) {
        super(properties);
        this.alwaysAffect = alwaysAffect;
        this.specialEffect = specialEffect;
        this.AccessoryName = AccessoryName;
        this.specialEffectName = specialEffectName;
    }
    @Override//アイテム選択
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        switch (this.AccessoryName) {
            case "diamond_ring":
                player.addEffect(new MobEffectInstance(this.specialEffect,500,1));
                break;
            case "diamond_soul":
                player.addEffect(new MobEffectInstance(this.specialEffect,200,1));
                break;
        }
        return super.use(level, player, interactionHand);
    }

    @Override
    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int slot, boolean inHand) {
        if (!level.isClientSide() && entity instanceof LivingEntity livingEntity) {
            switch (this.AccessoryName) {
                case "diamond_ring":
                    livingEntity.addEffect(new MobEffectInstance(this.alwaysAffect, 20, 3));
                    break;
                case "diamond_soul":
                    livingEntity.addEffect(new MobEffectInstance(this.alwaysAffect, 500, 1));
                    break;
            }
        }
    }

    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        list.add(Component.literal("right click\"" + this.specialEffectName + "\"!"));
    }

}


