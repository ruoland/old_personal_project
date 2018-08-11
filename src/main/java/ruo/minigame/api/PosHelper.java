package ruo.minigame.api;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import ruo.minigame.map.EntityDefaultNPC;

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
    public BlockPos getPosition(){
        return base.getPosition();
    }
    public Vec3d getXZ(SpawnDirection spawnDirection, double plus, boolean pos) {
        return new Vec3d(EntityAPI.getX(base, spawnDirection, plus, pos), pos ? base.posY : 0, EntityAPI.getZ(base, spawnDirection, plus, pos));
    }
    public Vec3d getXZ(SpawnDirection spawnDirection, double plus, double rlplus, boolean pos) {
        return new Vec3d(EntityAPI.getX(base, spawnDirection, plus, rlplus, pos), pos ? base.posY : 0, EntityAPI.getZ(base, spawnDirection, plus, rlplus, pos));
    }
    public double getX(SpawnDirection spawnDirection, double plus, boolean pos) {
        return EntityAPI.getX(base, spawnDirection, plus, pos);
    }
    public double getX(SpawnDirection spawnDirection, double plus, double rlplus, boolean pos) {
        return EntityAPI.getX(base, spawnDirection, plus, rlplus, pos);
    }
    public double getZ(SpawnDirection spawnDirection, double plus, boolean pos) {
        return EntityAPI.getZ(base, spawnDirection, plus, pos);
    }
    public double getZ(SpawnDirection spawnDirection, double plus, double rlplus, boolean pos) {
        return EntityAPI.getZ(base, spawnDirection, plus, rlplus, pos);
    }

    public EnumFacing getFacing(SpawnDirection spawnDirection){
        if(spawnDirection == SpawnDirection.LEFT)
            return EnumFacing.fromAngle(base.getHorizontalFacing().getHorizontalAngle() - 90);
        if(spawnDirection == SpawnDirection.RIGHT)
            return EnumFacing.fromAngle(base.getHorizontalFacing().getHorizontalAngle() + 90);
        if(spawnDirection == SpawnDirection.FORWARD)
            return EnumFacing.fromAngle(base.getHorizontalFacing().getHorizontalAngle());
        if(spawnDirection == SpawnDirection.BACK)
            return EnumFacing.fromAngle(base.getHorizontalFacing().getHorizontalAngle() + 180);
        return null;
    }

}
