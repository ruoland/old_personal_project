package ruo.map.tycoon.block.bread;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.gameevent.TickEvent.Type;
import ruo.map.tycoon.block.TileEntityBase;
import ruo.minigame.api.WorldAPI;
import ruo.minigame.effect.AbstractTick;
import ruo.minigame.effect.ENEffect;
import ruo.minigame.effect.TickRegister;

import javax.annotation.Nullable;

public class TileBreadWorkbench extends TileEntityBase {

	public boolean isStart;
	public int progress, milkCount, eggCount;


	public int getMilkCount() {
		return milkCount;
	}

	public int getEggCount() {
		return eggCount;
	}

	public int getProgress() {
		return progress;
	}

	public void addMilkCount(int count){
		milkCount+=count;
		addProgress(4);
	}

	public void addEggCount(int count){
		eggCount+=count;
		addProgress(3);
	}

	public void addProgress(int progress) {
		this.progress += progress;
		sendUpdates();
	}

	public void itemSetting() {
		if(isStart)
		{
			return;
		}else{
			isStart = true;
			addProgress(1);
		}
	}
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setBoolean("isStart", isStart);
		compound.setInteger("egg", eggCount);
		compound.setInteger("milk", milkCount);
		return super.writeToNBT(compound);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		isStart = compound.getBoolean("isStart");
		eggCount = compound.getInteger("egg");
		milkCount = compound.getInteger("milk");

	}

}
