package minigameLib.minigame.elytra.miniween.old;

import minigameLib.MiniGame;
import oneline.api.WorldAPI;
import oneline.fakeplayer.EntityFakePlayer;
import oneline.fakeplayer.FakePlayerHelper;
import oneline.map.EntityDefaultNPC;
import minigameLib.minigame.elytra.Elytra;
import minigameLib.minigame.elytra.ElytraEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.Blocks;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;


public class EntityElytraWeenCore extends EntityDefaultNPC {
    private static final DataParameter<Boolean> CAN_ITEM_DROP = EntityDataManager.createKey(EntityElytraWeenCore.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> CAN_TARGET_MOVE = EntityDataManager.createKey(EntityElytraWeenCore.class, DataSerializers.BOOLEAN);
    private Vec3d vec;
    protected double targetX, targetY, targetZ;
    public EntityElytraWeenCore(World worldIn) {
        super(worldIn);
        setBlockMode(Blocks.PUMPKIN);
        setCollision(false);
        this.addRotate(-180,0,270);
    }
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.23);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(30D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(2.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(5.0D);
    }

    public EntityDefaultNPC setTarget(double x, double y, double z){
        targetX = x;
        targetY = y;
        targetZ = z;
        vec = new Vec3d(targetX - posX, targetY - posY, targetZ - posZ);
        vec = vec.normalize();
        return this;
    }

    public boolean noTarget(){
        return vec == null;
    }


    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(CAN_ITEM_DROP, true);
        dataManager.register(CAN_TARGET_MOVE, true);
    }

    public void setDropItem(boolean b){
        dataManager.set(CAN_ITEM_DROP, b);
    }

    public boolean canDropItem(){
        return dataManager.get(CAN_ITEM_DROP);
    }
    public void setTargetMove(boolean b){
        dataManager.set(CAN_TARGET_MOVE, b);
    }

    public boolean canTargetMove(){
        return dataManager.get(CAN_TARGET_MOVE);
    }
    @Override
    public void knockBack(Entity entityIn, float strenght, double xRatio, double zRatio) {
        //super.knockBack(entityIn, strenght, xRatio, zRatio);
    }
    protected void collideWithEntity(Entity entityIn) {
        super.collideWithEntity(entityIn);
        if (entityIn instanceof EntityFakePlayer && isEntityAlive()) {
            this.worldObj.createExplosion(this, posX, posY, posZ, 1.5F, false);
            this.setDead();
        }
    }
    @Override
    public void onDeath(DamageSource cause) {
        super.onDeath(cause);
        ElytraEvent a = MiniGame.elytraEvent;
        if (canDropItem() && rand.nextInt(20) == 0 && isServerWorld()) {
        }
        if (!MiniGame.elytra.isStart() || a == null)
            return;
        Elytra.killCount++;

    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if(source.getEntity() instanceof  EntityElytraWeenCore)
            return false;
        return super.attackEntityFrom(source, amount);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (WorldAPI.getPlayer() == null || FakePlayerHelper.fakePlayer == null) {
            this.setDead();
            return;
        }
        if (canTargetMove() && vec != null) {
            if (isEntityAlive()) {
                this.setVelocity(vec.xCoord * 0.4, vec.yCoord * 0.4, vec.zCoord * 0.4);
                if (this.getDistance(targetX, targetY, targetZ) < 0.5) {
                    targetArrival();
                }
            }
        }
    }


    public void targetArrival() {
        this.worldObj.createExplosion(this, posX, posY, posZ, 1.5F, false);
        this.setDead();
    }
}
