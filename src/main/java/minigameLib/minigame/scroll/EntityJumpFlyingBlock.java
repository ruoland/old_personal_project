package minigameLib.minigame.scroll;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import rmap.lopre2.CommandJB;
import rmap.lopre2.EntityPreBlock;

public class EntityJumpFlyingBlock extends EntityPreBlock {
    private static final DataParameter<Integer> INVISIBLE_TIME = EntityDataManager.createKey(EntityJumpFlyingBlock.class, DataSerializers.VARINT);

    public EntityJumpFlyingBlock(World world) {
        super(world);
        this.setBlockMode(Blocks.STONE);
        this.isFly = true;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(INVISIBLE_TIME, 0);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(200.0D);

    }

    @Override
    public void onCollideWithPlayer(EntityPlayer entityIn) {
        super.onCollideWithPlayer(entityIn);
        if (!isInv() && dataManager.get(INVISIBLE_TIME) <= 0 && entityIn.posY > posY + 0.5) {
            entityIn.motionY = 0.7;
            setInv(true);
            setCollision(false);
            dataManager.set(INVISIBLE_TIME, 40);
        }
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if(isServerWorld()){
            dataManager.set(INVISIBLE_TIME, dataManager.get(INVISIBLE_TIME)-1);
            if(dataManager.get(INVISIBLE_TIME) <= 0){
                setCollision(true);
                setInv(false);
            }
        }
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (CommandJB.isDebMode) {
            if (source.getEntity() instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) source.getEntity();
                if (player.isSneaking()) {
                    setDead();
                }
            }
        }
        return super.attackEntityFrom(source, amount);
    }

    @Override
    public EntityPreBlock spawn(double x, double y, double z) {
        EntityJumpFlyingBlock lavaBlock = new EntityJumpFlyingBlock(worldObj);
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
        if (isServerWorld() || canForceSpawn()) {
            worldObj.spawnEntityInWorld(lavaBlock);

        }
        return lavaBlock;
    }

}
