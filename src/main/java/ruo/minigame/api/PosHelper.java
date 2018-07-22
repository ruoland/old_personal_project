package ruo.minigame.api;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;

public class PosHelper {
    private EntityLivingBase base;
    public PosHelper(EntityLivingBase base){
        this.base = base;
    }
    public BlockPos getXZ(SpawnDirection spawnDirection, double plus, boolean pos) {
        return new BlockPos(EntityAPI.getX(base, spawnDirection, plus, pos), pos ? base.posY : 0, EntityAPI.getZ(base, spawnDirection, plus, pos));
    }
    public BlockPos getXZ(SpawnDirection spawnDirection, double plus, double rlplus, boolean pos) {
        return new BlockPos(EntityAPI.getX(base, spawnDirection, plus, rlplus, pos), pos ? base.posY : 0, EntityAPI.getZ(base, spawnDirection, plus, rlplus, pos));
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


}
