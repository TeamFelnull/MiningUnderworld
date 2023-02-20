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
        providerAccess.basicFlatItem(MUItems.IKISUGI_TEST.get());

        providerAccess.basicFlatItem(MUItems.EMERALD_SWORD.get());
        providerAccess.basicFlatItem(MUItems.EMERALD_PICKAXE.get());
        providerAccess.basicFlatItem(MUItems.EMERALD_AXE.get());
        providerAccess.basicFlatItem(MUItems.EMERALD_SHOVEL.get());
        providerAccess.basicFlatItem(MUItems.EMERALD_HOE.get());

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
