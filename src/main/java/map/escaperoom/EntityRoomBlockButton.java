package map.escaperoom;

import map.lopre2.EntityPreBlock;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;

public class EntityRoomBlockButton extends EntityRoomBlock {
    private static final DataParameter<String> RUN_COMMAND = EntityDataManager.createKey(EntityRoomBlock.class, DataSerializers.STRING);

    public EntityRoomBlockButton(World worldIn) {
        super(worldIn);
        setBlockMode(Blocks.WOODEN_BUTTON);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(RUN_COMMAND, "");
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        isFly = true;
    }


    public String getCommand() {
        return dataManager.get(RUN_COMMAND);
    }

    public void setCommand(String com) {
        dataManager.set(RUN_COMMAND, com);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setString("Command", dataManager.get(RUN_COMMAND));
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        dataManager.set(RUN_COMMAND, compound.getString("Command"));
        System.out.println("RR  블럭" + getCurrentBlock() + typeModel + getTexture());
    }

    @Override
    public EntityPreBlock spawn(double x, double y, double z) {
        EntityRoomBlockButton lavaBlock = new EntityRoomBlockButton(worldObj);
        dataCopy(lavaBlock, x, y, z);
        if (isServerWorld() || canForceSpawn()) {
            worldObj.spawnEntityInWorld(lavaBlock);
        }
        lavaBlock.setBlock(getCurrentBlock());
        lavaBlock.setCommand(getCommand());
        return lavaBlock;
    }

    @Override
    public String getCustomNameTag() {
        return getJumpName() + " " + dataManager.get(RUN_COMMAND);
    }
}
