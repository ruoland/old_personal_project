package ruo.minigame.minigame.bomber;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import ruo.minigame.api.WorldAPI;

import java.util.ArrayList;

public class ItemBomber extends Item{
	private ArrayList<EntityBomb> pri2 = new ArrayList<EntityBomb>();
	private int count;
	public static int countPreloop;
	public ItemBomber() {
		
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn,
			EnumHand hand) {
		WorldAPI.command("/sound block set 2");

		for(int i = 0; i < pri2.size();i++){
			EntityBomb pri = pri2.get(i);
			if(pri.isDead){
				count --;
				pri2.remove(i);
			}
		}
		EntityBomb pri = null;
		if((pri == null || pri.isDead) && count < itemStackIn.stackSize && hand == EnumHand.MAIN_HAND && !worldIn.isRemote)
		{
			pri = new EntityBomb(worldIn, playerIn.posX,  playerIn.posY,  playerIn.posZ, playerIn);
			worldIn.spawnEntityInWorld(pri);
			count ++;
			pri2.add(pri);
		}
		
		return super.onItemRightClick(itemStackIn, worldIn, playerIn, hand);
	}

}
