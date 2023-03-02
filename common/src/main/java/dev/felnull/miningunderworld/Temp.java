package dev.felnull.miningunderworld;

import net.minecraft.world.item.ItemStack;

import java.util.List;

//mixin用クラス
//mixin内にpublic staticを置けないから、mixinのためにpublic static使うには外部クラスが必要
//mixinフォルダ内においたら@Mixinないのに勝手にmixin扱いされてエラー吐いた
public class Temp {
    public static List<ItemStack> armorSlotsReferringNow;
}
