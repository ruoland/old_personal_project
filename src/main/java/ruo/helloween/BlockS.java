package ruo.helloween;

import net.minecraft.world.World;

public class BlockS {
	public static EntityBlock setBlock(World worldObj) {
		return new EntityBlock(worldObj);
	}
	public static EntityBlock setFallExplosion(World worldObj, float strength) {
		return new EntityBlockFallAttack(worldObj).setStrength(strength);
	}
	
	public static EntityBlock setMoveXYZ(World worldObj, double x, double y, double z, boolean attack, float strength) {
		return new EntityBlockMoveAttack(worldObj).setMoveXYZ(x, y, z, attack, strength);
	}
	
	public static EntityBlock setMoveXYZ(World worldObj, double x, double y, double z) {
		return new EntityBlockMoveAttack(worldObj).setMoveXYZ(x, y, z, false, 0);
	}
}
