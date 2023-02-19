package dev.felnull.miningunderworld.data;

import dev.felnull.miningunderworld.item.MUItems;
import dev.felnull.otyacraftengine.data.CrossDataGeneratorAccess;
import dev.felnull.otyacraftengine.data.provider.ItemModelProviderWrapper;
import net.minecraft.data.PackOutput;

public class MUItemModelProviderWrapper extends ItemModelProviderWrapper {
    public MUItemModelProviderWrapper(PackOutput packOutput, CrossDataGeneratorAccess crossDataGeneratorAccess) {
        super(packOutput, crossDataGeneratorAccess);
    }

    @Override
    public void generateItemModels(ItemModelProviderAccess providerAccess) {
        providerAccess.basicFlatItem(MUItems.IKISUGI_TEST.get());

        providerAccess.handheldFlatItem(MUItems.COPPER_SWORD.get());
        providerAccess.handheldFlatItem(MUItems.COPPER_PICKAXE.get());
        providerAccess.handheldFlatItem(MUItems.COPPER_AXE.get());
        providerAccess.handheldFlatItem(MUItems.COPPER_SHOVEL.get());
    }
}
