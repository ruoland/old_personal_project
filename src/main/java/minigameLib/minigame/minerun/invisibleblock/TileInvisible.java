package minigameLib.minigame.minerun.invisibleblock;

import map.tycoon.BreadData;
import map.tycoon.block.TileBreadDisplay;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class TileInvisible extends TileEntity {
    private String command = "";
    private int defaultDelay = 20, runDelay, maxCount=1, runCount;
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setString("command", command);
        compound.setInteger("defaultDelay", defaultDelay);
        compound.setInteger("runDelay", runDelay);
        //compound.setInteger("maxCount", maxCount);
        compound.setInteger("runCount", runCount);
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        command = compound.getString("command");
        defaultDelay = compound.getInteger("defaultDelay");
        runDelay = compound.getInteger("runDelay");
        maxCount = compound.getInteger("maxCount");
        runCount = compound.getInteger("runCount");
        if(maxCount == 0)
            maxCount = 1;
        super.readFromNBT(compound);
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

    public void setCommand(String command) {
        this.command = command;
        sendUpdates();
    }
    public void setDefaultDelay(int delay){
        this.defaultDelay = delay;
        sendUpdates();
    }

    public void setRunDelay(int runDelay) {
        this.runDelay = runDelay;
        sendUpdates();
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
        sendUpdates();
    }

    public void setRunCount(int runCount) {
        this.runCount = runCount;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public int getRunDelay() {
        return runDelay;
    }

    public int getRunCount() {
        return runCount;
    }

    public int getDefaultDelay() {
        return defaultDelay;
    }

    public String getCommand() {
        return command;
    }
}
