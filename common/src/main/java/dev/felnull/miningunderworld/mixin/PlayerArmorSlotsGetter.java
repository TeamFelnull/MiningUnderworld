package dev.felnull.miningunderworld.mixin;

import dev.felnull.miningunderworld.Temp;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(EnchantmentMenu.class)
public abstract class PlayerArmorSlotsGetter {

    private List<ItemStack> armorSlots;

    @Inject(method = "<init>", at = @At("TAIL"))//"HEAD"だとエラー吐いた、this()の前に入れるなって
    private void storeArmorSlots(int i, Inventory inventory, CallbackInfo ci){
        this.armorSlots = inventory.armor;
    }

    @Inject(method = "slotsChanged", at = @At("HEAD"))
    public void provideArmorSlots(Container container, CallbackInfo ci){
        if(armorSlots != null)//一回のスロット変更でもslotsChangedは謎に何回も呼ばれる。なぜかarmorSlotsがnullのときも来るからnullをはじく
            Temp.armorSlotsReferringNow = armorSlots;
    }
}
