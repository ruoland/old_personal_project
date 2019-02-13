package map.puzzle;

import minigameLib.api.EntityAPI;
import minigameLib.api.WorldAPI;
import minigameLib.map.EntityDefaultNPC;
import minigameLib.map.TypeModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class EntityPuzzlePathCreeper extends EntityPuzzleMonster {
    private static final DataParameter<String> RUN_COMMAND = EntityDataManager.createKey(EntityPuzzleBlock.class, DataSerializers.STRING);

    public EntityPuzzlePathCreeper(World worldIn) {
        super(worldIn);
        typeModel = TypeModel.CREEPER;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(RUN_COMMAND, "");
    }

    public String getCommand() {
        return dataManager.get(RUN_COMMAND);
    }

    public void setCommand(String com) {
        dataManager.set(RUN_COMMAND, com);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if(!onGround)
            teleportSpawnPos();
    }

    @Override
    public void targetArrive() {
        setTarget(0,0,0);
        setVelocity(0,0,0);
        WorldAPI.command(getCommand());
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setString("command", getCommand());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        setCommand(compound.getString("command"));

    }
}
