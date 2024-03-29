package dev.felnull.miningunderworld.data.dynamic;

import dev.architectury.registry.registries.RegistrySupplier;
import dev.felnull.miningunderworld.MiningUnderworld;
import dev.felnull.miningunderworld.block.CrystalBlock;
import dev.felnull.miningunderworld.block.MUBlocks;
import dev.felnull.miningunderworld.util.MUUtils;
import dev.felnull.miningunderworld.world.dimension.MUBiomes;
import dev.felnull.miningunderworld.world.dimension.generation.MUPlacedFeatures;
import net.minecraft.core.registries.BuiltInRegistries;
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
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MUPackResource implements PackResources {

    String VANILLA = "minecraft";

    //こいつらはただの文字列としてもregexとしても使うから、.だけだと駄目なように見える。
    //でも拡張子の前なんて.しか来ないからOK
    String JSON = ".json";
    String PNG = ".png";

    String BLOCK_TAG = "tags/blocks";
    String PICKAXE_MINEABLE = "mineable/pickaxe";
    String LOOT_TABLE = "loot_tables";
    String BLOCK_LOOT_TABLE = "loot_tables/blocks";
    String BLOCK_TEXTURE = "textures/block";
    String MODEL = "models";
    String BLOCK_MODEL = "models/block";
    String BLOCK = "block";
    String ITEM_MODEL = "models/item";
    String BLOCKSTATES = "blockstates";
    String BIOME = "worldgen/biome";

    String CRYSTAL = "ore_crystal_.*";


    /**
     * このリソースパックが対応可能な情報の大まかな種類を返す。
     * 具体的には、PackType（assetsかdataか）と、Namespace（ResourceLocationの左側）がフィルターに使える。
     */
    @Override
    public Set<String> getNamespaces(PackType packType) {
        return new HashSet<>(packType == PackType.SERVER_DATA ? List.of(MiningUnderworld.MODID, VANILLA) : List.of(MiningUnderworld.MODID));
    }

    /**
     * このリソースパックが持つ全てのリソースを認識させる。
     *
     * @param packType       getNamespacesのフィルターを掻い潜ってきたPackType。PackTypeごとに認識させるリソースがある際に使う。
     * @param namespace      同じくフィルター済みのNameSpace。Namespaceごとに認識させるリソースがある場合に使う。
     * @param path           Path。ResourceLocationの右側。ここではリソースの種類。種類ごとにリソース認識処理を書く。
     * @param resourceOutput これのacceptを呼べば認識させられる。
     */
    @Override
    public void listResources(PackType packType, String namespace, String path, ResourceOutput resourceOutput) {
        if (packType == PackType.SERVER_DATA) {
            if (namespace.equals(VANILLA)) {
                if (path.equals(BLOCK_TAG)) {
                    var loc = new ResourceLocation(BLOCK_TAG + "/" + PICKAXE_MINEABLE + JSON);
                    resourceOutput.accept(loc, getResource(packType, loc));
                }
            } else if (path.equals(LOOT_TABLE))
                recognizeCrystals(resourceOutput, packType, BLOCK_LOOT_TABLE + "/", JSON);
            else if (DataHolder.collectedBiomes && path.equals(BIOME))//biome取得する準備が整ってない段階では、resourceをnullで上書きしないよう止める
                recognizeCollection(MUBiomes.biomeLocs(), resourceOutput, packType, BIOME + "/", JSON);
        } else if (path.equals(BLOCK_TEXTURE))
            recognizeCrystals(resourceOutput, packType, BLOCK_TEXTURE + "/", PNG);
        else if (path.equals(MODEL)) {
            recognizeCrystals(resourceOutput, packType, BLOCK_MODEL + "/", JSON);
            recognizeCrystals(resourceOutput, packType, ITEM_MODEL + "/", JSON);
        } else if (path.equals(BLOCKSTATES))
            recognizeCrystals(resourceOutput, packType, BLOCKSTATES + "/", JSON);
    }

    public void recognizeCrystals(ResourceOutput resourceOutput, PackType packType, String prefix, String suffix) {
        recognizeCollection(MUBlocks.CRYSTALS.stream().map(RegistrySupplier::getId).toList(), resourceOutput, packType, prefix, suffix);
    }

    public void recognizeCollection(Collection<ResourceLocation> c, ResourceOutput resourceOutput, PackType packType, String prefix, String suffix) {
        c.stream()
                .map(loc -> MUUtils.addPrefixAndSuffix(loc, prefix, suffix))
                .forEach(loc -> resourceOutput.accept(loc, getResource(packType, loc)));
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
        if (packType == PackType.SERVER_DATA) {
            if (loc.getNamespace().equals(VANILLA)) {
                if (loc.getPath().equals(BLOCK_TAG + "/" + PICKAXE_MINEABLE + JSON))
                    return toIOSup("""
                            {
                             "replace":false,
                             "values":[
                             1919
                             ]
                            }
                            """.replace("1919", String.join(",", MUBlocks.CRYSTALS.stream().map(it -> "\"" + it.getId() + "\"").toArray(String[]::new))));
            } else if (loc.getPath().matches(BLOCK_LOOT_TABLE + "/" + CRYSTAL + JSON))
                return toIOSup("""
                        {
                          "type": "minecraft:block",
                          "pools": [
                            {
                              "bonus_rolls": 0.0,
                              "conditions": [
                                {
                                  "condition": "minecraft:survives_explosion"
                                }
                              ],
                              "entries": [
                                {
                                  "type": "minecraft:item",
                                  "name": "114514"
                                }
                              ],
                              "rolls": 1.0
                            }
                          ]
                        }
                        """.replace("114514", MUUtils.subPrefixAndSuffix(loc, BLOCK_LOOT_TABLE + "/", JSON).toString()));
            else if (loc.getPath().matches(BIOME + "/" + "(?!.*_base).*" + JSON))
                return toIOSup(DataHolder.muBiomes.get(loc)
                        .replace("\"" + MUPlacedFeatures.DUMMY.location() + "\"", DataHolder.joinedOres));//ダミーを差し替え
        } else if (loc.getPath().matches(BLOCK_TEXTURE + "/" + CRYSTAL + PNG)) {
            var crystal = (CrystalBlock) BuiltInRegistries.BLOCK.get(MUUtils.subPrefixAndSuffix(loc, BLOCK_TEXTURE + "/", PNG));
            return toIOSup(TextureHolder.oreToTexture.get(crystal.ORE_LOC));
        } else if (loc.getPath().matches(BLOCK_MODEL + "/" + CRYSTAL + JSON))
            return toIOSup("""
                    {
                      "parent": "minecraft:block/cube_all",
                      "textures": {
                        "all": "1234"
                      }
                    }
                    """.replace("1234", MUUtils.subPrefixAndSuffix(loc, MODEL + "/", JSON).toString()));
        else if (loc.getPath().matches(ITEM_MODEL + "/" + CRYSTAL + JSON))
            return toIOSup("""
                    {
                      "parent": "194"
                    }
                    """.replace("194", MUUtils.subPrefixAndSuffix(loc, ITEM_MODEL + "/", JSON).withPrefix(BLOCK + "/").toString()));
        else if (loc.getPath().matches(BLOCKSTATES + "/" + CRYSTAL + JSON))
            return toIOSup("""
                    {
                      "variants": {
                        "": {
                          "model": "1234"
                        }
                      }
                    }
                    """.replace("1234", MUUtils.subPrefixAndSuffix(loc, BLOCKSTATES + "/", JSON).withPrefix(BLOCK + "/").toString()));

        return null;
        //このnullの意味
        //getResourceが直接呼ばれた場合：このリソパには含まれてなかった（別のリソパにあればそっちから取得される）
        //listResourcesで認識させるために呼ばれた場合：そのリソースは存在しない（別のリソパにも存在しないとされる、存在状況が上書きされる）
    }

    /**
     * 画像をマイクラの欲しがる形に変形。
     * BufferedImageから直接byte[]にできないから一旦outputStreamをかますとかいう調べたら出てきた手法。
     *
     * @param image 画像
     * @return リソースのインプットストリームのサプライヤ
     */
    public static IoSupplier<InputStream> toIOSup(BufferedImage image) {
        try {//ImageIO君の為
            var bo = new ByteArrayOutputStream();
            ImageIO.write(image, "png", bo);
            return () -> new ByteArrayInputStream(bo.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static IoSupplier<InputStream> toIOSup(String str) {
        return () -> new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8));
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
        return "MiningUnderworld Auto Generated Resources";
    }

    @Override
    public boolean isBuiltin() {//ビルトインです
        return true;
    }

    @Override
    public void close() {//終了時の処理、ないです
    }
}
