package ruo.awild.ship;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemShipFloorCreator extends Item {
    public static BlockPos start, end;

    @Override
    public boolean onBlockStartBreak(ItemStack stack, BlockPos pos, EntityPlayer player) {
        start = pos;
        return super.onBlockStartBreak(stack, pos, player);
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(pos != null) {
            end = pos;
            EntityShipFloor shipFloor = new EntityShipFloor(worldIn);
            shipFloor.setPosition(pos);
            worldIn.spawnEntityInWorld(shipFloor);
            shipFloor.startRiding(ItemShipCreator.ship);
            shipFloor.addBlock(start, end);
        }
        return super.onItemUse(stack, playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }



}
