package minigameLib.api;

import minigameLib.map.EntityDefaultNPC;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class PosHelper {
    private EntityLivingBase base;
    public PosHelper(EntityLivingBase base){
        this.base = base;
    }
    public PosHelper(Vec3d vec3d, EnumFacing facing){
        base = new EntityDefaultNPC(WorldAPI.getWorld());
        ((EntityDefaultNPC) base).setPosition(vec3d);
        base.rotationYaw = facing.getHorizontalAngle();
    }
    public PosHelper(double x, double y, double z, EnumFacing facing){
        base = new EntityDefaultNPC(WorldAPI.getWorld());
        base.setPosition(x,y,z);
        ((EntityDefaultNPC) base).setSpawnXYZ(x,y,z);
        base.rotationYaw = facing.getHorizontalAngle();
    }
    public BlockPos getPosition(){
        return base.getPosition();
    }
    public Vec3d getXZ(Direction spawnDirection, double plus, boolean pos) {
        return new Vec3d(EntityAPI.getX(base, spawnDirection, plus, pos), pos ? base.posY : 0, EntityAPI.getZ(base, spawnDirection, plus, pos));
    }

    public Vec3d getXZ(Direction spawnDirection, double plus, double rlplus, boolean pos) {
        return new Vec3d(EntityAPI.getX(base, spawnDirection, plus, rlplus, pos), pos ? base.posY : 0, EntityAPI.getZ(base, spawnDirection, plus, rlplus, pos));
    }
    public double getX(Direction spawnDirection, double plus, boolean pos) {
        return EntityAPI.getX(base, spawnDirection, plus, pos);
    }
    public double getX(Direction spawnDirection, double plus, double rlplus, boolean pos) {
        return EntityAPI.getX(base, spawnDirection, plus, rlplus, pos);
    }
    public double getY(double plus, boolean pos) {
        if (base.rotationPitch <= -60 && base.rotationPitch >= -90) {
            return pos ? base.posY + plus : plus;
        }
        if (base.rotationPitch >= 60 && base.rotationPitch <= 90) {
            return pos ? base.posY - plus : -plus;
        }
        return pos ? base.posY : 0;
    }
    public double getZ(Direction spawnDirection, double plus, boolean pos) {
        return EntityAPI.getZ(base, spawnDirection, plus, pos);
    }
    public double getZ(Direction spawnDirection, double plus, double rlplus, boolean pos) {
        return EntityAPI.getZ(base, spawnDirection, plus, rlplus, pos);
    }

    public EnumFacing getFacing(Direction spawnDirection){
        if(spawnDirection == Direction.LEFT)
            return EnumFacing.fromAngle(base.getHorizontalFacing().getHorizontalAngle() - 90);
        if(spawnDirection == Direction.RIGHT)
            return EnumFacing.fromAngle(base.getHorizontalFacing().getHorizontalAngle() + 90);
        if(spawnDirection == Direction.FORWARD)
            return EnumFacing.fromAngle(base.getHorizontalFacing().getHorizontalAngle());
        if(spawnDirection == Direction.BACK)
            return EnumFacing.fromAngle(base.getHorizontalFacing().getHorizontalAngle() + 180);
        return null;
    }

    public Direction getDirection(EntityLivingBase entity){
        PosHelper posHelper = new PosHelper(entity);
        double baseX=  getX(Direction.FORWARD, 1, false);
        double baseZ=  getZ(Direction.FORWARD, 1, false);
        double entityX=  posHelper.getX(Direction.FORWARD, 1, false);
        double entityZ=  posHelper.getZ(Direction.FORWARD, 1, false);
        if(baseX < entity.posX && baseZ > entity.posZ){
            return Direction.FORWARD_RIGHT;
        }
        if(baseX > entity.posX && baseZ > entity.posZ){
            return Direction.FORWARD_LEFT;
        }
        if(baseX < entity.posX && baseZ < entity.posZ){
            return Direction.BACK_RIGHT;
        }
        if(baseX > entity.posX && baseZ < entity.posZ){
            return Direction.BACK_LEFT;
        }
        if(baseX < entityX){
            return Direction.RIGHT;
        }
        if(baseX > entityX){
            return Direction.LEFT;
        }
        if(baseZ > entityZ){
            return Direction.FORWARD;
        }
        if(baseZ < entityZ){
            return Direction.BACK;
        }
        return null;
    }

    public double getPosX(){
        return base.posX;
    }
    public double getPosY(){
        return base.posY;
    }
    public double getPosZ(){
        return base.posZ;
    }
}
