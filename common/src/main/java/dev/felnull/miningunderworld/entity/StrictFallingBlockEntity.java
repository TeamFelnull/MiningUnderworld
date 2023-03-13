package dev.felnull.miningunderworld.entity;

import dev.felnull.miningunderworld.mixin.FallingBlockEntityAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PlayerHeadItem;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

//ブロックエンティティのドロップ処理をちゃんとした落下ブロック
public class StrictFallingBlockEntity extends FallingBlockEntity {

    public static void strictFall(Level level, BlockPos blockPos, BlockState blockState) {
        var entity = new StrictFallingBlockEntity(level, (double) blockPos.getX() + 0.5, blockPos.getY(), (double) blockPos.getZ() + 0.5,
                blockState.hasProperty(BlockStateProperties.WATERLOGGED) ? blockState.setValue(BlockStateProperties.WATERLOGGED, false) : blockState);
        level.setBlock(blockPos, blockState.getFluidState().createLegacyBlock(), 3);
        level.addFreshEntity(entity);
    }

    @Nullable
    public BlockEntityType<?> tileType;

    public StrictFallingBlockEntity(EntityType<? extends FallingBlockEntity> entityType, Level level) {
        super(entityType, level);
    }

    private StrictFallingBlockEntity(Level level, double x, double y, double z, BlockState blockState) {
        this(MUEntityTypes.STRICT_FALLING_BLOCK.get(), level);
        ((FallingBlockEntityAccessor) this).setBlockState(blockState);
        this.blocksBuilding = true;
        this.setPos(x, y, z);
        this.setDeltaMovement(Vec3.ZERO);
        this.xo = x;
        this.yo = y;
        this.zo = z;
        this.setStartPos(this.blockPosition());
        var tile = level.getBlockEntity(new BlockPos(x, y, z));
        if (tile != null) {
            blockData = tile.saveWithFullMetadata();
            this.tileType = tile.getType();
            tile.setRemoved();//これしないと次tickでonRemoveが呼ばれて落ち始めた地点でアイテムぶちまかれる
            //TODO:描画にTile使ってるやつのTile消したら描画しなくなる、チェストをチェストのまま描画させるにはどうすれば？
        }
    }

    @Nullable
    @Override
    public ItemEntity spawnAtLocation(ItemLike itemLike) {
        if (blockData == null)//通常の落ちるブロックはそのまま
            return super.spawnAtLocation(itemLike);

        var tempPos = blockPosition().above();//ぶちまけるために一旦ブロック置けそうなとこ、普通上から落ちて初めて固体にあたった際に判定入るから上空いてるやろ！の精神
        if (level.getBlockState(tempPos).isAir() && level.setBlockAndUpdate(tempPos, getBlockState())) {//ブロック置き換えにならない&&置けたら
            level.getBlockEntity(tempPos).load(blockData);//中身補充して
            level.setBlockAndUpdate(tempPos, Blocks.AIR.defaultBlockState());//空気に戻す&&壊した際にTileから出てくるものをドロップ
            return super.spawnAtLocation(itemLike);//本体をドロップ
        } else {
            var itemStack = new ItemStack(itemLike);
            addCustomNbtData(itemStack, blockData, tileType);
            return super.spawnAtLocation(itemStack);//NBT入りブロックアイテムをスポーン
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

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        if (tileType != null)
            compoundTag.putString("TileType", BuiltInRegistries.BLOCK_ENTITY_TYPE.getKey(tileType).toString());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        if (compoundTag.contains("TileType"))
            tileType = BuiltInRegistries.BLOCK_ENTITY_TYPE.get(new ResourceLocation(compoundTag.getString("TileType")));
    }
}
