package dev.felnull.miningunderworld.item;

import dev.felnull.miningunderworld.data.builtin.OreGetter;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TestItem extends Item {

    public TestItem(Properties properties) {
        super(properties);
    }

    @Override//アイテム使用(
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        if (!level.isClientSide()) {
            // var hit = level.isBlockInLine(new ClipBlockStateContext(player.position(), player.position().add(0, -10, 0), (it) -> !it.isAir()));
            // level.setBlockAndUpdate(hit.getBlockPos(), Blocks.DIAMOND_BLOCK.defaultBlockState());
        } else {

        }

        return super.use(level, player, interactionHand);
    }

    @Override//インベントリに存在してる場合に毎ティック呼び出し
    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int i, boolean bl) {
        // System.out.println("ｳｧｧ!!ｵﾚﾓｲｯﾁｬｳｩｩｩ!!!ｳｳｳｳｳｳｳｳｳｩｩｩｩｩｩｩｩｳｳｳｳｳｳｳｳ!ｲｨｨｲｨｨｨｲｲｲｨｲｲｲｲ");
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        list.add(Component.literal("F.C.O.H"));
    }

///execute in miningunderworld:mining_underworld run tp @p ~ ~ ~

    /* @Override
    public Component getName(ItemStack itemStack) {
        return Component.literal("ｳｧｧ!!ｵﾚﾓｲｯﾁｬｳｩｩｩ!!!ｳｳｳｳｳｳｳｳｳｩｩｩｩｩｩｩｩｳｳｳｳｳｳｳｳ!ｲｨｨｲｨｨｨｲｲｲｨｲｲｲｲ");
    }*/


}

//生きているエンティティにエフェクトを与える
//            livingEntity.addEffect(new MobEffectInstance(this.effect, 20, 3));

//スニークしたら
//        if(livingEntity.isCrouching())
//            level.explode(entity,entity.getX(),entity.getY(),entity.getZ(),10f, Level.ExplosionInteraction.MOB);

