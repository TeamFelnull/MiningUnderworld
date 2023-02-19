package dev.felnull.miningunderworld.data;

import dev.felnull.otyacraftengine.data.CrossDataGeneratorAccess;

public class MiningUnderworldDataGenerator {
    public static void init(CrossDataGeneratorAccess access) {
        access.addProviderWrapper(MUItemModelProviderWrapper::new);
        access.addProviderWrapper(MUBlockStateAndModelProviderWrapper::new);
        access.addProviderWrapper(MURecipeProviderWrapper::new);
        access.addProviderWrapper(MURegistriesDatapackProviderWrapper::new);
    }
}
