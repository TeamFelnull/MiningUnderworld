package dev.felnull.miningunderworld.data.builtin;

import dev.felnull.miningunderworld.MiningUnderworld;
import dev.felnull.miningunderworld.util.MUUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import net.minecraft.server.packs.resources.IoSupplier;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

public class MUPackResource implements PackResources {
    @Nullable
    @Override
    public IoSupplier<InputStream> getRootResource(String... strings) {
        return null;
    }

    @Nullable
    @Override
    //TODO なぜか起動時にテクスチャを取得しようとされない(KubeJSは起動時から読み込まれている)
    public IoSupplier<InputStream> getResource(PackType packType, ResourceLocation loc) {//受付先
        System.out.println("受け取る"+loc);
        if (packType != PackType.CLIENT_RESOURCES || !loc.getPath().matches("textures/block/crystal_\\d*.png"))
            return null;
        System.out.println("処理する"+loc);
        var order = Integer.parseInt(loc.getPath().replaceAll("textures/block/crystal_(\\d*).png", "$1"));//鉱石番号
        var max = OreGetter.getInstance().ores.size() - 1;//最大鉱石番号
        if (order > max)
            return null;
        try {
            var bytes = TextureGetter.crystalTexture;
            var image = ImageIO.read(new ByteArrayInputStream(bytes));
            var oreLoc = OreGetter.getInstance().ores.get(MUUtils.modLoc("crystal_" + order));
            var oreARGB =0xBFBBBBBB/*不透明度約0.75の白めの色*/ + (int) (0x444444 * ((float) order) / max);//TODO oreLoc使って鉱石毎に合ったRGB計算
            IntStream.range(0, image.getWidth()).forEach(x -> IntStream.range(0, image.getHeight()).forEach(y ->
                image.setRGB(x, y, MUUtils.blend(oreARGB, image.getRGB(x, y)))));

            var bo = new ByteArrayOutputStream();
            ImageIO.write(image, "png", bo);
            return () -> new ByteArrayInputStream(bo.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void listResources(PackType packType, String string, String string2, ResourceOutput resourceOutput) {//なんなの
        IntStream.range(0, OreGetter.getInstance().ores.size())
                .mapToObj(i -> MUUtils.modLoc("textures/block/crystal_" + i + ".png"))
                .forEach(loc -> resourceOutput.accept(loc, getResource(packType, loc)));
    }

    @Override
    public Set<String> getNamespaces(PackType packType) {//受付可能なネームスペース
        if(packType != PackType.CLIENT_RESOURCES)
            return Collections.emptySet();

        var names = new HashSet<String>();
        names.add(MiningUnderworld.MODID);
        return names;
    }

    @Nullable
    @Override
    public <T> T getMetadataSection(MetadataSectionSerializer<T> metadataSectionSerializer) throws IOException {//なにこれ
        return null;
    }

    @Override
    public String packId() {//名前
        return "MiningUnderworld Auto Generated Textures";
    }

    @Override
    public boolean isBuiltin() {//ビルトインです
        return true;
    }

    @Override
    public void close() {//終了時の処理

    }
}
