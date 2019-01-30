package rmap.tycoon.block.shopping;

import minigameLib.effect.AbstractTick;
import minigameLib.effect.TickRegister;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.gameevent.TickEvent.Type;
import rmap.tycoon.BreadData;

import javax.annotation.Nullable;

public class TileShopping extends TileEntity {
	private BreadData displayItem;
	private boolean isSecond;
	public BreadData getBread() {
		return displayItem;
	}

	public BreadData splitBread(int amount) {
		BreadData bre = new BreadData(getItem().splitStack(amount));
		sendUpdates();
		return bre;
	}

	public boolean isDisplayUse() {
		return displayItem != null && displayItem.getEntityItem() != null;
	}

	public void setItem(ItemStack itemstack) {
		displayItem = new BreadData(itemstack);
		sendUpdates();
	}

	public ItemStack getItem() {
		if (displayItem == null || displayItem.getEntityItem() == null)
			return new ItemStack(Items.APPLE);
		return displayItem.getEntityItem().getEntityItem();
	}
	
	public void removeDisplayItem() {
		displayItem = null;
		sendUpdates();
	}

	public boolean isSecondActive() {
		return true;
	}
	
	public void setSecondActive() {
		isSecond = true;
		TickRegister.register(new AbstractTick(20, false) {
			@Override
			public void run(Type type) {
				isSecond = false;
			}
		});
	}
	@Nullable
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(this.pos, 1, this.getUpdateTag());
	}

	public NBTTagCompound getUpdateTag() {
		return this.writeToNBT(new NBTTagCompound());
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		super.onDataPacket(net, pkt);
		readFromNBT(pkt.getNbtCompound());
	}

	public void sendUpdates() {
		worldObj.markBlockRangeForRenderUpdate(pos, pos);
		worldObj.notifyBlockUpdate(pos, getState(), getState(), 3);
		worldObj.scheduleBlockUpdate(pos, this.getBlockType(), 0, 0);
		markDirty();
	}

	private IBlockState getState() {
		return worldObj.getBlockState(pos);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		if (displayItem != null) {
			compound.setTag("displayitem", displayItem.getEntityItem().getEntityItem().serializeNBT());
		}
		
		return super.writeToNBT(compound);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		if (compound.hasKey("displayitem")) {
			ItemStack stack = ItemStack.loadItemStackFromNBT((NBTTagCompound) compound.getTag("displayitem"));
			System.out.println("아이템을 불러옴"+stack.getDisplayName()+stack.stackSize);
			displayItem = new BreadData(stack);
		}

	}
}
