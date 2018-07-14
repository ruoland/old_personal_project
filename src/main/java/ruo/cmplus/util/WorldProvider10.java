package ruo.cmplus.util;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;

public class WorldProvider10 extends WorldProvider {
	private Vec3d vec3d = new Vec3d(Sky.skyR, Sky.skyG, Sky.skyB);;

	public DimensionType getDimensionType() {
		return DimensionType.OVERWORLD;
	}

	@Override
	public Vec3d getSkyColor(Entity cameraEntity, float partialTicks) {
		if (cameraEntity != null && Sky.skyR == 0 && Sky.skyG == 0 && Sky.skyB == 0) {
			return super.getSkyColor(cameraEntity, partialTicks);
		} else if (vec3d.xCoord == Sky.skyR && vec3d.yCoord == Sky.skyG && vec3d.zCoord == Sky.skyB) {
			return vec3d;
		} else
			return vec3d = new Vec3d(Sky.skyR, Sky.skyG, Sky.skyB);
	}
	

	@Override
	public float getCloudHeight() {
		if (Sky.cloudHeight == 0)
			return super.getCloudHeight();
		else
			return Sky.cloudHeight;
	}

	@Override
	public float getSunBrightness(float par1) {

		if (Sky.sunbright == -1)
			return super.getSunBrightness(par1);
		else
			return Sky.sunbright;
	}


}