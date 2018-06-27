package ruo.map.tycoon.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.gameevent.TickEvent.Type;
import ruo.minigame.api.WorldAPI;
import ruo.minigame.effect.AbstractTick;
import ruo.minigame.effect.ENEffect;
import ruo.minigame.effect.TickRegister;

import javax.annotation.Nullable;

public class TileBreadCall extends TileEntity {
	private int breadCallCount = 0;
	public int breadCallSecond = 5;
	public boolean isStart;
	
	
	public void addBread() {
		breadCallCount ++;
	}
	
	public void subBread() {
		breadCallCount --;
	}
    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        return new SPacketUpdateTileEntity(this.pos, 1, this.getUpdateTag());
    }

    public NBTTagCompound getUpdateTag()
    {
        return this.writeToNBT(new NBTTagCompound());
    }
    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
    	super.onDataPacket(net, pkt);
    	readFromNBT(pkt.getNbtCompound());
    }

	public void callBread(int second) {
		if(isStart)
		{
			WorldAPI.addMessage("빵 재료가 이미 오고 있습니다.");
			return;
		}
		TickRegister.register(new AbstractTick(Type.SERVER, 1, true) {
			@Override
			public void run(Type type) {
				isStart = true;
				if(breadCallSecond == 0) {
					absLoop = false;
					isStart = false;
					ENEffect.spawnItem(Items.WHEAT, pos.add(0, 1, 0), getBreadCount());
					breadCallSecond = 5;
					WorldAPI.addMessage("빵 재료가 도착했습니다.");
				}
				if(absRunCount % 20 == 0)
					breadCallSecond--;
				sendUpdates();

			}
		});
	}
	public int getBreadCount() {
		return breadCallCount;
	}
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setInteger("breadCallCount", breadCallCount);
		compound.setInteger("breadCallSecond", breadCallSecond);
		compound.setBoolean("isStart", isStart);

		return super.writeToNBT(compound);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		breadCallCount = compound.getInteger("breadCallCount");
		breadCallSecond = compound.getInteger("breadCallSecond");
		isStart = compound.getBoolean("isStart");

	}
	public void sendUpdates() {
		worldObj.markBlockRangeForRenderUpdate(pos, pos);
		worldObj.notifyBlockUpdate(pos, getState(), getState(), 3);
		worldObj.scheduleBlockUpdate(pos,this.getBlockType(),0,0);
		markDirty();
	}
	private IBlockState getState() {
		return worldObj.getBlockState(pos);
	}

}
