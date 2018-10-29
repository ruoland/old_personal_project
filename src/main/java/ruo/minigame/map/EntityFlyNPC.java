package ruo.minigame.map;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityFlyNPC extends EntityDefaultNPC {
    private static final DataParameter<Float> SPEED = EntityDataManager.createKey(EntityFlyNPC.class, DataSerializers.FLOAT);
    private static final DataParameter<Boolean> ARRIVE = EntityDataManager.createKey(EntityFlyNPC.class,DataSerializers.BOOLEAN);
    protected Vec3d targetVec, target;
    public EntityFlyNPC(World worldIn) {
        super(worldIn);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(ARRIVE, false);
        dataManager.register(SPEED, 1F);
    }

    public boolean noTarget(){
        return targetVec == null;
    }

    public void targetRemove(){
        targetVec = null;
        target = null;
    }

    @Override
    public EntityDefaultNPC setTarget(double x, double y, double z, double speed) {
        double targetX = x != 0 ? (x - posX) : 0;
        double targetY = y != 0 ? (y - posY) : 0;
        double targetZ = z != 0 ? (z - posZ) : 0;
        if(targetX == 0 && targetY == 0 && targetZ == 0)
        {
            targetVec = null;
            target = null;
            return this;
        }
        targetVec = new Vec3d(targetX,targetY,targetZ).normalize().scale(speed);
        target = new Vec3d(x,y,z);
        setArrive(false);
        return this;
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (target!=null) {
            this.setVelocity((targetVec.xCoord / 20 )* getSpeed(), (targetVec.yCoord/20) * getSpeed(), (targetVec.zCoord/20) * getSpeed());
            getLookHelper().setLookPosition(target.xCoord, target.yCoord, target.zCoord, 360, 360);
            if(target.distanceTo(this.getPositionVector()) < 1){
                System.out.println("도착함");
                this.setVelocity(0,0,0);
                targetRemove();
                setArrive(true);

                targetArrive();
            }
        }
    }

    public void targetArrive(){

    }

    public void addSpeed(float speed){
        dataManager.set(SPEED, getSpeed()+speed);
    }
    public void setSpeed(float speed){
        dataManager.set(SPEED, speed);
    }
    public float getSpeed(){
        return dataManager.get(SPEED);
    }
    public void setArrive(boolean arrive){
        dataManager.set(ARRIVE, arrive);
    }
    public boolean isArrive(){
        return dataManager.get(ARRIVE).booleanValue();
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setBoolean("arrive", isArrive());
        compound.setFloat("speed", getSpeed());
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        setArrive(compound.getBoolean("arrive"));
        setSpeed(compound.getFloat("speed"));
    }
}
