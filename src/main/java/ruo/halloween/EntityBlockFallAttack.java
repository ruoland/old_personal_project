package ruo.halloween;

import net.minecraft.world.World;

//이 엔티티는 나중에 몬스터 패턴이나 그런 곳에 쓰라고 만들어 놓은 엔티티임
public class EntityBlockFallAttack extends EntityBlock {
	private boolean fallExplosionMode = false;
	protected float fallExplosionStrength = 0, moveExplosionStrength;
	public EntityBlockFallAttack(World worldIn) {
		super(worldIn);
	}
	
	public EntityBlockFallAttack setStrength(float fall) {
		fallExplosionStrength = fall;
		return this;
	}

	@Override
	public void fall(float distance, float damageMultiplier) {
		super.fall(distance, damageMultiplier);
		if (fallExplosionMode) {
			worldObj.createExplosion(this, posX, posY, posZ, fallExplosionStrength, true);
			this.setDead();
		}
	}
}
