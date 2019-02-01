package map.lot.tool;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import map.lot.old.LOTEffect;

public class ItemWaterWand extends Item{

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn,
			EnumHand hand) {
		LOTEffect.water(playerIn);
		
		return super.onItemRightClick(itemStackIn, worldIn, playerIn, hand);
	}
}
