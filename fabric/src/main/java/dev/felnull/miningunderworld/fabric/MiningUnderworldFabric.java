package dev.felnull.miningunderworld.fabric;

import dev.felnull.miningunderworld.MiningUnderworld;
import dev.felnull.miningunderworld.data.dynamic.OreHolder;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ModResourcePack;
import net.fabricmc.fabric.impl.resource.loader.ModResourcePackUtil;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.ServerPacksSource;
import net.minecraft.server.packs.resources.MultiPackResourceManager;

import java.util.ArrayList;
import java.util.List;

public class MiningUnderworldFabric implements ModInitializer {
    @Override
    public void onInitialize() {

        List<ModResourcePack> modPacks = new ArrayList<>();
        ModResourcePackUtil.appendModResourcePacks(modPacks, PackType.SERVER_DATA, null);//modのpack追加
        List<PackResources> packs = new ArrayList<>(modPacks);//ジェネリクスのキャスト
        packs.add(new ServerPacksSource().getVanillaPack());//バニラのpack追加
        OreHolder.load(new MultiPackResourceManager(PackType.SERVER_DATA, packs));//dataから鉱石取得

        MiningUnderworld.init();
    }
    /*
    Trinkets関係
    https://www.curseforge.com/minecraft/mc-mods/trinkets
    https://github.com/emilyploszaj/trinkets/wiki/Quick-Start-Guide
     */
}
