package map.tycoon.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import map.tycoon.BreadData;

import javax.annotation.Nullable;

public class TileBreadDisplay extends TileEntity {
	private BreadData displayBread;
	protected boolean canConsumerUse = true, isGlass = false;

	public BreadData getBread() {
		return displayBread;
	}

	public BreadData splitBread(int amount) {
		BreadData bre = new BreadData(getItem().splitStack(amount));
		sendUpdates();
		return bre;
	}

	public boolean canDisplayUse() {
		return displayBread != null && displayBread.getEntityItem() != null;
	}

	/**
	 * 소비자가 빵을 고르고 있는 중이면 true
	 * 빵 고르기를 끝내면 flase
	 */
	public void setConsumerUse(boolean is) {
		this.canConsumerUse = !is;
		sendUpdates();
	}
	/**
	 * 소비자가 빵을 고를 수 있는가
	 */
	public boolean canConsumerUse() {
		return canConsumerUse && !isGlass;
	}
	
	public void setItem(ItemStack itemstack) {
		if(itemstack == null)
			displayBread = null;
		else
			displayBread = new BreadData(itemstack);
		sendUpdates();
	}

	public ItemStack getItem() {
		if (displayBread == null || displayBread.getEntityItem() == null)
			return new ItemStack(Items.APPLE);
		return displayBread.getEntityItem().getEntityItem();
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
		if (displayBread != null) {
			compound.setTag("displayitem", displayBread.getEntityItem().getEntityItem().serializeNBT());
		}
		compound.setBoolean("isGlass", isGlass);
		//compound.setBoolean("conUse", canConsumerUse);
		
		return super.writeToNBT(compound);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		if (compound.hasKey("displayitem")) {
			ItemStack stack = ItemStack.loadItemStackFromNBT((NBTTagCompound) compound.getTag("displayitem"));
			System.out.println("아이템을 불러옴"+stack.getDisplayName()+stack.stackSize);
			displayBread = new BreadData(stack);
		}
		isGlass = compound.getBoolean("isGlass");
		//canConsumerUse = compound.getBoolean("conUse");

	}
}
