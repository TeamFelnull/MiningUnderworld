package dev.felnull.miningunderworld.item;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.EnderpearlItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

public class AccessoryItem extends Item {
private final MobEffect effect;

    public AccessoryItem(MobEffect effect,Properties properties) {
        super(properties);
        this.effect=effect;
    }


    @Override//アイテム選択
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {

        return super.use(level, player, interactionHand);
    }

    @Override
    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int slot, boolean inHand) {
       if(!level.isClientSide() && entity instanceof LivingEntity livingEntity){

           //生きているエンティティにエフェクトを与える
           livingEntity.addEffect(new MobEffectInstance(this.effect,20,3));

        //スニークしたら
        if(livingEntity.isCrouching())
            level.explode(entity,entity.getX(),entity.getY(),entity.getZ(),10f, Level.ExplosionInteraction.MOB);

       }
    }
}
