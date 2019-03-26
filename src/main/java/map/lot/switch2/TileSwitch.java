package map.lot.switch2;

import oneline.api.WorldAPI;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nullable;

public class TileSwitch extends TileEntity {
    private String command = "없음";
    private boolean isOn;

    public void setCommand(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public void setOn( boolean on) {
        if(!worldObj.isRemote) {
            boolean prevOn = false;
            if (isOn) {
                prevOn = true;
            }
            isOn = on;
            if (isOn && !prevOn) {
                WorldAPI.command(getCommand());
            }
        }
    }

    public boolean isOn() {
        return isOn;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setString("com", command);
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        command = compound.getString("com");
    }
    private IBlockState getState() {
        return worldObj.getBlockState(pos);
    }

    public void sendUpdates() {
        worldObj.markBlockRangeForRenderUpdate(pos, pos);
        worldObj.notifyBlockUpdate(pos, getState(), getState(), 3);
        worldObj.scheduleBlockUpdate(pos,this.getBlockType(),0,0);
        markDirty();
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

}
