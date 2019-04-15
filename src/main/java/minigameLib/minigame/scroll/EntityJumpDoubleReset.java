package minigameLib.minigame.scroll;

import oneline.action.ActionEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import map.lopre2.CommandJB;
import map.lopre2.EntityPreBlock;

//TODO 설치할 때 무조건 블럭 가운데 설치되게 하기
//TODO  충돌 범위 늘리기
public class EntityJumpDoubleReset extends EntityPreBlock {
    private static final DataParameter<Integer> INVISIBLE_TIME = EntityDataManager.createKey(EntityJumpDoubleReset.class, DataSerializers.VARINT);

    public EntityJumpDoubleReset(World world) {
        super(world);
        this.setBlockMode(Blocks.WOOL);
        setScale(0.5F,0.5F,0.5F);
        this.setSize(0.3F,0.3F);
        this.setTra(0,0.35F,0);
        this.setCollision(false);
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
    protected void collideWithEntity(Entity entityIn) {
        super.collideWithEntity(entityIn);
        if(isServerWorld() && !isTeleport() && entityIn instanceof EntityPlayer && !isInv() && getEntityBoundingBox().intersectsWith(entityIn.getEntityBoundingBox())) {
            ActionEffect.canDoubleJump = true;
            ActionEffect.isPlayerJump = true;
            ActionEffect.forceJump = true;
            setInv(true);
            setInvisible(true);
            entityIn.fallDistance = 0;
            dataManager.set(INVISIBLE_TIME, 40);
        }

    }

    @Override
    public void onCollideWithPlayer(EntityPlayer entityIn) {
        super.onCollideWithPlayer(entityIn);

    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if(isServerWorld()){
            setCollision(false);
            if(dataManager.get(INVISIBLE_TIME) <= 0){
                setInv(false);
            }else
                dataManager.set(INVISIBLE_TIME, dataManager.get(INVISIBLE_TIME)-1);
        }else{
            if(dataManager.get(INVISIBLE_TIME) == 40) {
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
        EntityJumpDoubleReset lavaBlock = new EntityJumpDoubleReset(worldObj);
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
