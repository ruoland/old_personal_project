package ruo.helloween.miniween;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import ruo.helloween.EntityWeen;

public class EntityNightMiniWeen extends EntityMiniWeen {
	public EntityNightMiniWeen(World worldIn) {
		super(worldIn);
	}
	public EntityNightMiniWeen(World worldIn, EntityWeen ween) {
		this(worldIn);
		if(ween == null)
			this.setDead();
	}

	@Override
	public void onCollideWithPlayer(EntityPlayer entityIn) {
		super.onCollideWithPlayer(entityIn);
		System.out.println("미니윈이 플레이어와 충돌해 폭발함");
		this.worldObj.createExplosion(this, posX, posY, posZ, 1.0F, false);
		this.setDead();
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		return super.attackEntityFrom(source, amount + 2000);
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
	}

	@Override
	public void fall(float distance, float damageMultiplier) {
		super.fall(distance, damageMultiplier);
		System.out.println("밤 모드의 미니윈이 추락함");
		this.worldObj.createExplosion(this, posX, posY, posZ, 1.0F, false);
		this.setDead();
	}

}
