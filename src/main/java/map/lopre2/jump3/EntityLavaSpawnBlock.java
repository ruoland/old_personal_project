package map.lopre2.jump3;

import minigameLib.api.WorldAPI;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import map.lopre2.EntityPreBlock;

public class EntityLavaSpawnBlock extends EntityPreBlock {
    private static final DataParameter<Float> DISTANCE = EntityDataManager.createKey(EntityLavaSpawnBlock.class, DataSerializers.FLOAT);

    public EntityLavaSpawnBlock(World worldIn) {
        super(worldIn);
        this.setBlockMode(Blocks.STONE);
    }

    @Override
    public String getCustomNameTag() {
        return "LavaSpawnBlock 거리:"+getSpawnDistance();
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(DISTANCE, 6F);
    }

    public float getSpawnDistance() {
        return dataManager.get(DISTANCE);
    }

    public void setSpawnDistance(float distance) {
        dataManager.set(DISTANCE, distance);
    }

    @Override
    protected boolean processInteract(EntityPlayer player, EnumHand hand, ItemStack stack) {
        if(hand == EnumHand.MAIN_HAND) {
            if (player.isSneaking())
                setSpawnDistance(getSpawnDistance() + 1);
            else
                setSpawnDistance(getSpawnDistance() - 1);
        }
        return super.processInteract(player, hand, stack);

    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if(!isTeleport()) {
            if (getSpawnDistance() > WorldAPI.getPlayer().getDistanceToEntity(this)) {
                motionZ= 0;
                motionY = 0.08;
                motionX = 0;
                this.worldObj.setBlockState(getPosition(), Blocks.LAVA.getDefaultState());
            } else if (worldObj.getBlockState(getPosition()).getBlock() == Blocks.LAVA) {
                if(worldObj.getBlockState(getPosition().add(0,1,0)).getBlock() == Blocks.LAVA)
                this.worldObj.setBlockState(getPosition().add(0,1,0), Blocks.AIR.getDefaultState());
                this.worldObj.setBlockState(getPosition(), Blocks.AIR.getDefaultState());
                if(worldObj.getBlockState(getPosition().add(0,-1,0)).getBlock() == Blocks.LAVA)
                    this.worldObj.setBlockState(getPosition().add(0,-1,0), Blocks.AIR.getDefaultState());

                motionZ= 0;
                motionY = 0;
                motionX = 0;
            }
            if (worldObj.getBlockState(getPosition().add(0,1,0)).getBlock() != Blocks.AIR) {
                motionZ= 0;
                motionY = 0;
                motionX = 0;
            }
        }
    }
    @Override
    public EntityPreBlock spawn(double x, double y, double z) {
        EntityLavaSpawnBlock lavaBlock = new EntityLavaSpawnBlock(worldObj);
        lavaBlock.setTeleportLock(canTeleportLock());
        lavaBlock.setSpawnXYZ(x, y, z);
        lavaBlock.setTeleport(false);
        lavaBlock.setPosition(lavaBlock.getSpawnX(), lavaBlock.getSpawnY(), lavaBlock.getSpawnZ());

        lavaBlock.setBlockMode(getCurrentBlock());
        this.copyModel(lavaBlock);
        lavaBlock.setRotate(getRotateX(), getRotateY(), getRotateZ());
        lavaBlock.setBlockMetadata(getBlockMetadata());
        lavaBlock.setInv(isInv());
        lavaBlock.setInvisible(isInvisible());
        if(isServerWorld() || canForceSpawn()) {
            worldObj.spawnEntityInWorld(lavaBlock);
        }
        return lavaBlock;
    }
}
