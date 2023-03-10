package dev.felnull.miningunderworld.data;

import com.google.common.collect.ImmutableList;
import dev.felnull.miningunderworld.client.MUItemProperties;
import dev.felnull.miningunderworld.item.MUItems;
import dev.felnull.miningunderworld.item.WeatheringItem;
import dev.felnull.otyacraftengine.data.CrossDataGeneratorAccess;
import dev.felnull.otyacraftengine.data.model.ItemModelProviderAccess;
import dev.felnull.otyacraftengine.data.model.MutableFileModel;
import dev.felnull.otyacraftengine.data.model.OverridePredicate;
import dev.felnull.otyacraftengine.data.provider.ItemModelProviderWrapper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.TieredItem;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MUItemModelProviderWrapper extends ItemModelProviderWrapper {

    public MUItemModelProviderWrapper(PackOutput packOutput, CrossDataGeneratorAccess crossDataGeneratorAccess) {
        super(packOutput, crossDataGeneratorAccess);
    }

    @Override
    public void generateItemModels(ItemModelProviderAccess providerAccess) {
        StreamSupport.stream(MUItems.ITEMS.spliterator(), false).map(Supplier::get).forEach(item -> {
            if (item instanceof WeatheringItem)//錆び
                if (item instanceof TieredItem)//ツール
                    weatheringItem(item, providerAccess::handheldFlatItem);
                else//それ以外、アーマー等
                    weatheringItem(item, providerAccess::basicFlatItem);
            else if (item instanceof TieredItem)//ツール
                providerAccess.handheldFlatItem(item);
            else if (item instanceof SpawnEggItem)//スポーンエッグ
                providerAccess.parentedItem(item, new ResourceLocation("item/template_spawn_egg"));
            else //通常
                providerAccess.basicFlatItem(item);
        });
    }

    private MutableFileModel weatheringItem(Item item, Function<ResourceLocation, MutableFileModel> modelType) {
        var itemLoc = BuiltInRegistries.ITEM.getKey(item);
        var model = modelType.apply(itemLoc);
        var subModels = subWeatheringItems(itemLoc.getPath(), modelType);

        subModels.forEach((state, subModel) ->//序数の小さい順、条件の広い順に登録する。徐々に狭い条件で上書きしていく。広い条件で上書きしたらそれまでの条件が意味なくなる。
                model.override(subModel, ImmutableList.of(new OverridePredicate(MUItemProperties.OXIDIZING, (float) state.ordinal() / 10f))));

        return model;
    }

    private Map<WeatheringItem.WeatheringState, MutableFileModel> subWeatheringItems(String name, Function<ResourceLocation, MutableFileModel> modelType) {
        return Arrays.stream(WeatheringItem.WeatheringState.values())
                .filter(it -> it != WeatheringItem.WeatheringState.NONE)
                .collect(Collectors.toMap(
                        oxidizingState -> oxidizingState,
                        oxidizingState -> modelType.apply(modLoc(oxidizingState.getSerializedName() + "_" + name)),
                        (mutableFileModel, mutableFileModel2) -> mutableFileModel,//重複した値が来たら最初の値のまま、下の引数指定するためにこの引数入れないといけない
                        TreeMap::new));//中身をソートするマップ、WeatheringStateの序数順。
    }
}
