package dev.felnull.miningunderworld.mixin.client;

import dev.felnull.miningunderworld.data.dynamic.MUPackResource;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.resources.MultiPackResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.ArrayList;
import java.util.List;

@Mixin(MultiPackResourceManager.class)
public class AddDynamicPackResource {//起動中に中身作るリソースパック

    @ModifyVariable(method = "<init>", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private static List<PackResources> inject(List<PackResources> list) {
        var packs = new ArrayList<>(list);
        packs.add(new MUPackResource());//最後尾に挿入
        return packs;
    }
}
