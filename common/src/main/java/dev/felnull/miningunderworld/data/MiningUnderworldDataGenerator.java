package dev.felnull.miningunderworld.data;

import dev.felnull.otyacraftengine.data.CrossDataGeneratorAccess;
import dev.felnull.otyacraftengine.data.provider.DataProviderWrapper;

public class MiningUnderworldDataGenerator {
    public static void init(CrossDataGeneratorAccess access) {
        access.addProviderWrapper(MUItemModelProviderWrapper::new);
        access.addProviderWrapper(MUBlockStateAndModelProviderWrapper::new);
        access.addProviderWrapper(MURecipeProviderWrapper::new);
        access.addProviderWrapper(MURegistriesDatapackProviderWrapper::new);

        var blockTagProviderWrapper = access.addProviderWrapper(MUBlockTagProviderWrapper::new);
        access.addProviderWrapper((DataProviderWrapper.LookupGeneratorAccessedFactory<DataProviderWrapper<?>>) (packOutput, lookup, generatorAccess) -> new MUItemTagProviderWrapper(packOutput, lookup, generatorAccess, blockTagProviderWrapper));
        access.addProviderWrapper(MUFluidTagProviderWrapper::new);
    }
}
