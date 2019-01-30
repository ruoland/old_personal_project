package minigameLib.minigame.minerun;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemSpawnCoin extends Item {

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(playerIn.isSneaking()) {
            EntityItem enderstar = new EntityItem(worldIn, hitX, hitY, hitZ, new ItemStack(Items.NETHER_STAR));
            enderstar.setInfinitePickupDelay();
            worldIn.spawnEntityInWorld(enderstar);
        }else {
            EntityItem enderstar = new EntityItem(worldIn, hitX, hitY, hitZ, new ItemStack(Items.POTIONITEM));
            enderstar.setInfinitePickupDelay();
            worldIn.spawnEntityInWorld(enderstar);
        }
        return super.onItemUse(stack, playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }
}