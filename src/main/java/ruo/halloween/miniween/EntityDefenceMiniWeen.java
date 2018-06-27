package ruo.halloween.miniween;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import ruo.halloween.EntityWeen;

public class EntityDefenceMiniWeen extends EntityMiniWeen {
	public boolean goWeen;
	public EntityDefenceMiniWeen(World worldIn) {
		super(worldIn);
	}
	public EntityDefenceMiniWeen(World worldIn, EntityWeen ween) {
		this(worldIn);
		this.ween = ween;
		if(ween == null)
			this.setDead();
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		return false;
	}
	@Override
	public void onCollideWithPlayer(EntityPlayer entityIn) {
		super.onCollideWithPlayer(entityIn);
		((EntityLivingBase) this).knockBack(entityIn, 0.2F, entityIn.posX - this.posX, entityIn.posZ - this.posZ);
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		if (target!=null) {
			this.setVelocity(targetVec.xCoord, targetVec.yCoord, targetVec.zCoord);
		}
		if (goWeen && getDistance(target.xCoord, target.yCoord, target.zCoord) <= 1) {
			this.setDead();
		}
	}
}
