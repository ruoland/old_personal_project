package ruo.awild.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.Comparator;

public class FindBlockComparator implements Comparator<BlockPos> {
    private EntityLiving zombie;
    public FindBlockComparator(EntityLiving zombie){
        this.zombie = zombie;
    }
	@Override
	public int compare(BlockPos o1, BlockPos o2) {
	    Vec3d vec3d = new Vec3d(o1);
        Vec3d vec3d2 = new Vec3d(o2);
        Double dis = zombie.getPositionVector().distanceTo(vec3d);
        Double dis2 = zombie.getPositionVector().distanceTo(vec3d2);
		return dis.compareTo(dis2);
	}

}
