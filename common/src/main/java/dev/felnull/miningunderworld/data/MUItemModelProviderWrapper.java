package dev.felnull.miningunderworld.data;

import com.google.common.collect.ImmutableList;
import dev.felnull.miningunderworld.client.MUItemProperties;
import dev.felnull.miningunderworld.item.MUItems;
import dev.felnull.miningunderworld.item.WeatheringItem;
import dev.felnull.otyacraftengine.data.CrossDataGeneratorAccess;
import dev.felnull.otyacraftengine.data.model.MutableFileModel;
import dev.felnull.otyacraftengine.data.model.OverridePredicate;
import dev.felnull.otyacraftengine.data.provider.ItemModelProviderWrapper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class MUItemModelProviderWrapper extends ItemModelProviderWrapper {

    public MUItemModelProviderWrapper(PackOutput packOutput, CrossDataGeneratorAccess crossDataGeneratorAccess) {
        super(packOutput, crossDataGeneratorAccess);
    }

    @Override
    public void generateItemModels(ItemModelProviderAccess providerAccess) {
        // providerAccess.basicFlatItem(); <-通常の平たいアイテムモデルを生成
        // providerAccess.handheldFlatItem(); <-剣などのツールのような持ち方をするアイテムモデルを生成

        providerAccess.basicFlatItem(MUItems.IKISUGI_TEST.get());

        providerAccess.handheldFlatItem(MUItems.AMETHYST_SWORD.get());
        providerAccess.handheldFlatItem(MUItems.AMETHYST_PICKAXE.get());
        providerAccess.handheldFlatItem(MUItems.AMETHYST_AXE.get());
        providerAccess.handheldFlatItem(MUItems.AMETHYST_SHOVEL.get());
        providerAccess.handheldFlatItem(MUItems.AMETHYST_HOE.get());
        providerAccess.basicFlatItem(MUItems.AMETHYST_INGOT.get());
        providerAccess.basicFlatItem(MUItems.AMETHYST_HELMET.get());
        providerAccess.basicFlatItem(MUItems.AMETHYST_CHESTPLATE.get());
        providerAccess.basicFlatItem(MUItems.AMETHYST_LEGGINGS.get());
        providerAccess.basicFlatItem(MUItems.AMETHYST_BOOTS.get());

        providerAccess.handheldFlatItem(MUItems.EMERALD_SWORD.get());
        providerAccess.handheldFlatItem(MUItems.EMERALD_PICKAXE.get());
        providerAccess.handheldFlatItem(MUItems.EMERALD_AXE.get());
        providerAccess.handheldFlatItem(MUItems.EMERALD_SHOVEL.get());
        providerAccess.handheldFlatItem(MUItems.EMERALD_HOE.get());
        providerAccess.basicFlatItem(MUItems.EMERALD_HELMET.get());
        providerAccess.basicFlatItem(MUItems.EMERALD_CHESTPLATE.get());
        providerAccess.basicFlatItem(MUItems.EMERALD_LEGGINGS.get());
        providerAccess.basicFlatItem(MUItems.EMERALD_BOOTS.get());

        handheldWeatheringItem(providerAccess, MUItems.COPPER_SWORD.get());
        handheldWeatheringItem(providerAccess, MUItems.COPPER_PICKAXE.get());
        handheldWeatheringItem(providerAccess, MUItems.COPPER_AXE.get());
        handheldWeatheringItem(providerAccess, MUItems.COPPER_SHOVEL.get());
    }

    private MutableFileModel handheldWeatheringItem(ItemModelProviderAccess providerAccess, Item item) {
        var model = providerAccess.handheldFlatItem(item);
        var subModels = subHandheldWeatheringItems(providerAccess, BuiltInRegistries.ITEM.getKey(item).getPath());

        subModels.forEach((state, subModel) -> model.addOverride(subModel, ImmutableList.of(new OverridePredicate(MUItemProperties.OXIDIZING, (float) state.ordinal() / 10f))));

        return model;
    }

    private Map<WeatheringItem.WeatheringState, MutableFileModel> subHandheldWeatheringItems(ItemModelProviderAccess providerAccess, String name) {
        return Arrays.stream(WeatheringItem.WeatheringState.values())
                .filter(it -> it != WeatheringItem.WeatheringState.NONE)
                .collect(Collectors.toMap(oxidizingState -> oxidizingState, oxidizingState -> providerAccess.handheldFlatItem(modLoc(oxidizingState.getSerializedName() + "_" + name)), (mutableFileModel, mutableFileModel2) -> mutableFileModel, TreeMap::new));
    }
}
