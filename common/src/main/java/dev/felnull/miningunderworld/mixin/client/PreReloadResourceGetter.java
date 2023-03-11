package dev.felnull.miningunderworld.mixin.client;

import dev.felnull.miningunderworld.data.dynamic.OreHolder;
import dev.felnull.miningunderworld.data.dynamic.TextureHolder;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.MultiPackResourceManager;
import net.minecraft.server.packs.resources.ReloadInstance;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.util.Unit;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;


@Mixin(ReloadableResourceManager.class)
public class PreReloadResourceGetter {//既存のリソースを使って独自テクスチャ作りたいから、リロード前にリソース取得

    @Inject(method = "createReload", at = @At("HEAD"))
    public void add(Executor executor, Executor executor2, CompletableFuture<Unit> completableFuture, List<PackResources> list, CallbackInfoReturnable<ReloadInstance> cir) {
        new OreHolder.Loader(new MultiPackResourceManager(PackType.SERVER_DATA, list));//dataから鉱石取得
        TextureHolder.load(new MultiPackResourceManager(PackType.CLIENT_RESOURCES, list));//assetsからテクスチャ取得
    }
}
