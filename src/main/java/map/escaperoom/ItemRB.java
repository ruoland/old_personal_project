package map.escaperoom;

import cmplus.cm.v18.CommandWaypoint;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import olib.api.WorldAPI;

public class ItemRB extends Item {

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        if(!worldIn.isRemote)
        WorldAPI.command(playerIn, "room rb");
        return super.onItemRightClick(itemStackIn, worldIn, playerIn, hand);
    }
}
