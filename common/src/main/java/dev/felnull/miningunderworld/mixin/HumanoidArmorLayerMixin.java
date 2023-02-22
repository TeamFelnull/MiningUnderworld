package dev.felnull.miningunderworld.mixin;

import dev.felnull.miningunderworld.MiningUnderworld;
import dev.felnull.miningunderworld.item.MUArmorMaterials;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorItem;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(HumanoidArmorLayer.class)
public class HumanoidArmorLayerMixin {
    @Shadow
    @Final
    private static Map<String, ResourceLocation> ARMOR_LOCATION_CACHE;

    @Inject(method = "getArmorLocation", at = @At("HEAD"), cancellable = true)
    private void getArmorLocation(ArmorItem armorItem, boolean bl, String string, CallbackInfoReturnable<ResourceLocation> cir) {
        var material = armorItem.getMaterial();
        if (material instanceof MUArmorMaterials) {
            var string2 = MiningUnderworld.MODID + ":textures/models/armor/" + material.getName() + "_layer_" + (bl ? 2 : 1) + (string == null ? "" : "_" + string) + ".png";
            cir.setReturnValue(ARMOR_LOCATION_CACHE.computeIfAbsent(string2, ResourceLocation::new));
        }
    }
}