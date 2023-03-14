package dev.felnull.miningunderworld;

import dev.felnull.miningunderworld.item.MUArmorMaterials;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;

import java.util.List;

//mixin用クラス
//mixinフォルダ内においたら@Mixinないのに勝手にmixin扱いされてエラー吐いた
public class MixinTemp {

    //mixin内にpublic staticを置けないから、mixinのためにpublic static使うには外部クラスが必要
    public static ThreadLocal<List<ItemStack>> armorSlotsNow = new ThreadLocal<>();//一応複数スレッドで同時にエンチャ選択されたとき用にスレッドごとのstatic変数を提供

    //forgeとfabricでRedirect先が違ったから共通部分をここに移動
    public static int modifyEnchantmentValue(int enchantmentValue){
        if(MixinTemp.armorSlotsNow.get() == null)//null→アーマーが取得されなかった→エンチャ台以外からselectEnchantmentが呼ばれた
            return enchantmentValue;
        var isFullArmor = MixinTemp.armorSlotsNow.get().stream().allMatch(item ->
                item.getItem() instanceof ArmorItem armor && armor.getMaterial() == MUArmorMaterials.LAPIS_LAZULI);
        MixinTemp.armorSlotsNow.set(null);//エンチャ台で呼んだら毎回nullに戻して、アーマーが取得されたかどうか<=>nullじゃないかどうか、とする
        return isFullArmor ? enchantmentValue + 20 : enchantmentValue;
    }
}
