package map.escaperoom;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import oneline.api.WorldAPI;
import oneline.map.TypeModel;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class EntityRoomPathCreeper extends EntityRoomMonster {
    private static final DataParameter<String> RUN_COMMAND = EntityDataManager.createKey(EntityRoomBlock.class, DataSerializers.STRING);

    public EntityRoomPathCreeper(World worldIn) {
        super(worldIn);
        typeModel = TypeModel.CREEPER;
        this.setTargetSpeed(0.001);
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
    protected boolean processInteract(EntityPlayer player, EnumHand hand, @Nullable ItemStack stack) {
        return super.processInteract(player, hand, stack);
    }

    @Override
    public void knockBack(Entity entityIn, float strenght, double xRatio, double zRatio) {
        //super.knockBack(entityIn, strenght, xRatio, zRatio);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if(getSpawnY() > posY)
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
