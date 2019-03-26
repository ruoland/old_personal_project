package ruo.awild.ship;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemShipCreator extends Item {
    public static EntityShip ship;
    public static BlockPos start, end;

    @Override
    public boolean onBlockStartBreak(ItemStack stack, BlockPos pos, EntityPlayer player) {
        if(!stack.hasTagCompound())
            stack.setTagCompound(new NBTTagCompound());
        NBTTagCompound tag = stack.getTagCompound();
        tag.setInteger("fx", pos.getX());
        tag.setInteger("fy", pos.getY());
        tag.setInteger("fz", pos.getZ());
        start = pos;
        return super.onBlockStartBreak(stack, pos, player);
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(pos != null) {
            if (!stack.hasTagCompound())
                stack.setTagCompound(new NBTTagCompound());
            NBTTagCompound tag = stack.getTagCompound();
            tag.setInteger("sx", pos.getX());
            tag.setInteger("sy", pos.getY());
            tag.setInteger("sz", pos.getZ());
            end = pos;
            ship = new EntityShip(worldIn);
            ship.setPosition(pos);
            worldIn.spawnEntityInWorld(ship);

            ship.addBlock(start, end);


        }
        return super.onItemUse(stack, playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }



}
