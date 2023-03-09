package dev.felnull.miningunderworld.mixin.client;

import dev.felnull.miningunderworld.data.builtin.MUPackResource;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.repository.PackRepository;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(PackRepository.class)
public class AddBuiltinPackResource {

    @Inject(method = "openAllSelected", at = @At("RETURN"), cancellable = true)
    public void inject(CallbackInfoReturnable<List<PackResources>> cir){
        var packs = new ArrayList<>(cir.getReturnValue());
        packs.add(new MUPackResource());
        cir.setReturnValue(packs);
    }
}
