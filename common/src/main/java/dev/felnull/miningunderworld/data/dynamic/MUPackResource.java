package dev.felnull.miningunderworld.data.dynamic;

import dev.felnull.miningunderworld.MiningUnderworld;
import dev.felnull.miningunderworld.block.CrystalBlock;
import dev.felnull.miningunderworld.util.MUUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import net.minecraft.server.packs.resources.IoSupplier;
import org.jetbrains.annotations.Nullable;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

public class MUPackResource implements PackResources {

    /**
     * このリソースパックが対応可能な情報の大まかな種類を返す。
     * 具体的には、PackType（assetsかdataか）と、Namespace（ResourceLocationの左側）がフィルターに使える。
     */
    @Override
    public Set<String> getNamespaces(PackType packType) {
        if (packType == PackType.CLIENT_RESOURCES) {//assetsなら
            var names = new HashSet<String>();
            names.add(MiningUnderworld.MODID);//このMODのMODIDのみ受け付ける
            return names;
        }
        return Collections.emptySet();//それ以外は何も受け付けない
    }

    /**
     * このリソースパックが持つ全てのリソースを認識させる。
     *
     * @param packType       getNamespacesのフィルターを掻い潜ってきたPackType。PackTypeごとに認識させるリソースがある場合は使う。
     * @param namespace      同じくフィルター済みのNameSpace。Namespaceごとに認識させるリソースがある場合は使う。
     * @param path           Path。ResourceLocationの右側。ここではリソースの種類。種類ごとにリソース認識処理を書く。
     * @param resourceOutput これのacceptを呼べば認識させられる。
     */
    @Override
    public void listResources(PackType packType, String namespace, String path, ResourceOutput resourceOutput) {
        if (path.equals("textures/block"))//ブロックテクスチャについて
            IntStream.rangeClosed(0, CrystalBlock.MAX_ID)//全CrystalBlock分
                    .mapToObj(i -> MUUtils.modLoc("textures/block/crystal_" + i + ".png"))//テクスチャのResourceLocation作って
                    .forEach(loc -> resourceOutput.accept(loc, getResource(packType, loc)));//リソースとともに認識させる
    }

    /**
     * ResourceLocationからResource（のInputStream（のサプラヤ））を返す。
     * minecraft:textures/item/apple.png みたいに、一番具体的なResourceLocation。
     * Resourceの実体はjsonでもoggでもpngでもいいから、minecraft:appleだとどのappleだよってなるからね。
     *
     * @param packType フィルター済みPackType。PackTypeごとにリソースの取得方法が違かったら使う。
     * @param loc      受付ResourceLocation。pathではフィルターされないため、listResourcesで認識させたもの以外も来る可能性あり。
     * @return ResourceのInputStreamのサプライヤ。Resourceはファイル情報で、openすればInputStreamとしてデータが見える。なぜかそれのサプライヤを欲しがっている。
     */
    @Nullable
    @Override
    public IoSupplier<InputStream> getResource(PackType packType, ResourceLocation loc) {
        if (loc.getPath().matches("textures/block/crystal_\\d*.png")) {//上で登録したものについて
            var order = Integer.parseInt(loc.getPath().replaceAll("textures/block/crystal_(\\d*).png", "$1"));//取得する鉱石の番号。0から始まる
            var max = OreHolder.idToOre.size() - 1;//この起動構成での鉱石数-1。鉱石番号

            if (order > max)//CrystalBlockの全blockstatesに対してテクスチャを与えるから、最大鉱石番号以上のも来る。
                return imageToInputStreamSupplier(TRANSPARENT);//そんな奴は適当に透明なテクスチャで。ここnullだとforgeDataGenが起動しない

            //鉱石番号内なら鉱石ごとに加工していく
            try {//ImageIOがthrowするからtry
                var image = ImageIO.read(new ByteArrayInputStream(TextureHolder.crystalTexture));//加工するテクスチャ
                var oreLoc = OreHolder.idToOre.get(order);//鉱石のResourceLocation(文脈依存形)
                var oreARGB = 0xBFBBBBBB/*不透明度約0.75の白めの色*/ + (int) (0x444444 * ((float) order) / max);//鉱石に特有の色を生成、TODO 鉱石に合った色を取得したい
                IntStream.range(0, image.getWidth()).forEach(x -> IntStream.range(0, image.getHeight()).forEach(y ->//テクスチャ全体に
                        image.setRGB(x, y, MUUtils.blend(oreARGB, image.getRGB(x, y)))));//鉱石特有色を下からブレンド
                return imageToInputStreamSupplier(image);//加工済みのものを返す
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return null;//それ以外はnull!このリソパには含まれてなかったことを示す。
    }

    public static BufferedImage TRANSPARENT = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);//16x16透明テクスチャ

    /**
     * 画像をマイクラの欲しがる形に変形。
     * BufferedImageから直接byte[]にできないから一旦outputStreamをかますとかいう調べたら出てきた手法。
     *
     * @param image 画像
     * @return リソースのインプットストリームのサプライヤ
     */
    public static IoSupplier<InputStream> imageToInputStreamSupplier(BufferedImage image) {
        try {//ImageIO君の為
            var bo = new ByteArrayOutputStream();
            ImageIO.write(image, "png", bo);
            return () -> new ByteArrayInputStream(bo.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Nullable
    @Override
    public IoSupplier<InputStream> getRootResource(String... strings) {//ネームスペース直下のリソースを取得するときに使うけど使わんわ
        return null;
    }

    @Nullable
    @Override
    public <T> T getMetadataSection(MetadataSectionSerializer<T> metadataSectionSerializer) throws IOException {//pack.mcmetaらしいけど知らね
        /*return AbstractPackResources.getMetadataFromStream(metadataSectionSerializer, new ByteArrayInputStream("""
                {
                  "pack": {
                    "pack_format": 10,
                    "description": ""
                  }
                }
                """.getBytes(StandardCharsets.UTF_8)));*/
        return null;
    }

    @Override
    public String packId() {//名前、ログでしか見れないけど
        return "MiningUnderworld Auto Generated Textures";
    }

    @Override
    public boolean isBuiltin() {//ビルトインです
        return true;
    }

    @Override
    public void close() {//終了時の処理、ないです
    }
}
