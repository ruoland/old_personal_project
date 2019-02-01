package map.lot.tool;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemEnderShot extends Item {


    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {

        if (!worldIn.isRemote)
        {
            EntityEnderShot enderShot = new EntityEnderShot(worldIn, playerIn);
            enderShot.setHeadingFromThrower(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 1.5F, 1.0F);
            worldIn.spawnEntityInWorld(enderShot);
        }

        return super.onItemRightClick(itemStackIn, worldIn, playerIn, hand);
    }
}
