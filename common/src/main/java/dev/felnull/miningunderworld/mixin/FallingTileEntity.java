package dev.felnull.miningunderworld.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PlayerHeadItem;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(FallingBlockEntity.class)
public abstract class FallingTileEntity extends Entity {

    @Shadow
    public abstract BlockState getBlockState();

    public FallingTileEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Nullable
    public CompoundTag tileData;
    @Nullable
    public BlockEntityType<?> tileType;

    @Inject(method = "<init>(Lnet/minecraft/world/level/Level;DDDLnet/minecraft/world/level/block/state/BlockState;)V", at = @At("TAIL"))
    public void storeTileData(Level level, double d, double e, double f, BlockState blockState, CallbackInfo ci) {
        var pos = new BlockPos(d, e, f);
        var tile = level.getChunkAt(pos).getBlockEntity(pos, LevelChunk.EntityCreationType.IMMEDIATE);//level.getBlockEntity(pos)←これだとパケット通信で送られたServerPlayerのときスレッド違いではじかれる
        //FallingBlockEntityそのものにもTileを落とす仕組みがあったけど、level.getBlockEntity使ってたから続投
        if (tile != null) {
            this.tileData = tile.saveWithFullMetadata();
            this.tileType = tile.getType();
            tile.setRemoved();//これしないと次tickでonRemoveが呼ばれて落ち始めた地点でアイテムぶちまかれる
            //TODO:描画にTile使ってるやつのTile消したら描画しなくなる、チェストをチェストのまま描画させるにはどうすれば？
        }
    }

    //着地成功したらそのままNBT補充
    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;getChunkSource()Lnet/minecraft/server/level/ServerChunkCache;"))
    public void successLanding(CallbackInfo ci) {
        if (tileData != null)//データあれば
            restoreTileData(blockPosition());//補充
    }

    public void restoreTileData(BlockPos pos) {
        tileData.putInt("x", pos.getX());//Meka段ボ―ルにあった処理だから一応入れてる、tileDataあるときにしか使わんからOK
        tileData.putInt("y", pos.getY());
        tileData.putInt("z", pos.getZ());
        level.getBlockEntity(pos).load(tileData);//ブロック置けた後に呼ぶから絶対ある
    }

    //失敗したら出来る限り中身ぶちまける、無理ならNBT入りブロックアイテム上げる💛
    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/item/FallingBlockEntity;spawnAtLocation(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/entity/item/ItemEntity;"))
    public ItemEntity failLanding(FallingBlockEntity instance, ItemLike itemLike) {
        if (tileData == null)//通常の落ちるブロックはそのまま
            return this.spawnAtLocation(itemLike);

        var tempPos = blockPosition().above();//ぶちまけるために一旦ブロック置けそうなとこ、普通上から落ちて初めて固体にあたった際に判定入るから上空いてるやろ！の精神
        if (level.getBlockState(tempPos).isAir() && level.setBlockAndUpdate(tempPos, getBlockState())) {//ブロック置き換えにならない&&置けたら
            restoreTileData(tempPos);//中身補充して
            var strongTool = new ItemStack(Items.DIAMOND_PICKAXE);
            strongTool.enchant(Enchantments.SILK_TOUCH, 1);
            var context = new LootContext.Builder((ServerLevel) level).withRandom(level.random).withParameter(LootContextParams.ORIGIN, position()).withParameter(LootContextParams.TOOL, strongTool);
            level.getBlockState(tempPos).getDrops(context).forEach(this::spawnAtLocation);//本体を破壊したものをドロップ
            level.setBlockAndUpdate(tempPos, Blocks.AIR.defaultBlockState());//空気に戻す&&壊した際にTileから出てくるものもドロップ
            return null;//戻り値使われてなかったから適当でいい
        } else {
            var itemStack = new ItemStack(itemLike);
            addCustomNbtData(itemStack, tileData, tileType);
            return this.spawnAtLocation(itemStack);//NBT入りブロックアイテムをスポーン
        }
    }

    //Minecraft(Client専用クラス)にあった、Ctrl+MiddleClickでNBTを保持したブロックアイテムを取得する機能
    public void addCustomNbtData(ItemStack p_263370_, CompoundTag tileData, BlockEntityType<?> tileType) {
        BlockItem.setBlockEntityData(p_263370_, tileType, tileData);
        if (p_263370_.getItem() instanceof PlayerHeadItem && tileData.contains("SkullOwner")) {
            CompoundTag compoundtag3 = tileData.getCompound("SkullOwner");
            CompoundTag compoundtag4 = p_263370_.getOrCreateTag();
            compoundtag4.put("SkullOwner", compoundtag3);
            CompoundTag compoundtag2 = compoundtag4.getCompound("BlockEntityTag");
            compoundtag2.remove("SkullOwner");
            compoundtag2.remove("x");
            compoundtag2.remove("y");
            compoundtag2.remove("z");
        } else {
            CompoundTag compoundtag1 = new CompoundTag();
            ListTag listtag = new ListTag();
            listtag.add(StringTag.valueOf("\"(+NBT)\""));
            compoundtag1.put("Lore", listtag);
            p_263370_.addTagElement("display", compoundtag1);
        }
    }

    //保存&読み込み
    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    public void addTileSaveData(CompoundTag tag, CallbackInfo ci) {
        if (tileData != null)//nullだと原因が提示されないエラー吐かれた
            tag.put("TileData", tileData);
        if (tileType != null)
            tag.putString("TileType", BuiltInRegistries.BLOCK_ENTITY_TYPE.getKey(tileType).toString());
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    public void readTileSaveData(CompoundTag tag, CallbackInfo ci) {
        if (tag.contains("TileData"))
            tileData = tag.getCompound("TileData");
        if (tag.contains("TileType"))
            tileType = BuiltInRegistries.BLOCK_ENTITY_TYPE.get(new ResourceLocation(tag.getString("TileType")));
    }
}
