package dev.felnull.miningunderworld.mixin;

import dev.felnull.miningunderworld.Temp;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(EnchantmentMenu.class)
public abstract class EnchantmentMenuArmorSlotsGetter extends AbstractContainerMenu {

    private List<ItemStack> armorSlots;

    protected EnchantmentMenuArmorSlotsGetter(@Nullable MenuType<?> menuType, int i) {
        super(menuType, i);
    }

    //より一般的な方のinit
    @Inject(method = "<init>(ILnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/inventory/ContainerLevelAccess;)V", at = @At("TAIL"))
    private void storeArmorSlots(int i, Inventory inventory, ContainerLevelAccess containerLevelAccess, CallbackInfo ci){
        this.armorSlots = inventory.armor;
    }

    @Inject(method = "getEnchantmentList", at = @At("HEAD"))
    public void provideArmorSlots(ItemStack itemStack, int i, int j, CallbackInfoReturnable<List<EnchantmentInstance>> cir){
        Temp.armorSlotsReferringNow.set(armorSlots);//mixin間をつなぐには他クラスのstaticフィールドを使えばいい→ModifyEnchantmentValue
    }
}
