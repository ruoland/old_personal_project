package ruo.helloween;

import net.minecraft.entity.IEntityLivingData;
import net.minecraft.init.Blocks;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

//이 엔티티는 나중에 몬스터 패턴이나 그런 곳에 쓰라고 만들어 놓은 엔티티임
public class EntityBlockMoveAttack extends EntityBlock {
	private boolean moveMode = false, moveAttack = false;
	protected float moveExplosionStrength, targetX, targetY, targetZ;

	public EntityBlockMoveAttack(World worldIn) {
		super(worldIn);
		setBlockMode(Blocks.STONE);
	}
	public EntityBlock setMoveXYZ(double x, double y, double z, boolean attack, float strength) {
		moveMode = true;
		moveAttack = attack;
		moveExplosionStrength = strength;
		return this;
	}
	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
		EntityBlockMoveAttack b = this;
		if(moveAttack)
			WeenEffect.entityMoveFlyExplosion(this, targetX, targetY, targetZ, 12, moveExplosionStrength);
		return super.onInitialSpawn(difficulty, livingdata);
	}
}
