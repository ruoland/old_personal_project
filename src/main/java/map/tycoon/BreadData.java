package map.tycoon;

import minigameLib.api.WorldAPI;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;

import java.io.Serializable;

public class BreadData implements Serializable{
	private EntityItem entityitem;
	private ItemStack itemstack;
	public int money = 0;
	public BreadData(ItemStack stack) {
		this.money = 1000 * stack.stackSize;
		itemstack = stack;

	}
	public EntityItem getEntityItem() {
		if(entityitem == null) {
			entityitem = new EntityItem(WorldAPI.getWorld());
			entityitem.setEntityItemStack(getItemstack());
		}
		return entityitem;
	}
	public String getDisplayName() {
		return getItemstack().getDisplayName();
	}
	public int getAmount() {
		return getItemstack().stackSize;
	}

	public boolean nameCheck(ItemStack heldItem) {
		if(heldItem == null)
			return false;
		return getDisplayName().equals(heldItem.getDisplayName());
	}
	public ItemStack getItemstack() {
		return itemstack;
	}
	public void addAmount(int count) {
		getItemstack().stackSize += count;
		getEntityItem().setEntityItemStack(getItemstack());
	}

	public void subAmount(int count) {
		getItemstack().stackSize -= count;
		getEntityItem().setEntityItemStack(getItemstack());
	}
}
