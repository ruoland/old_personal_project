package ruo.halloween;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import ruo.minigame.map.EntityDefaultNPC;

//이 엔티티는 나중에 몬스터 패턴이나 그런 곳에 쓰라고 만들어 놓은 엔티티임
public class EntityBlock extends EntityDefaultNPC {
	protected double spawnX, spawnY, spawnZ;
	protected boolean isCollideExplosion, isCollideKnockback, noAttack;
	protected int deadTick, curTick = 0;
	protected Block onGroundBlock;
	public EntityBlock(World worldIn) {
		super(worldIn);
		setTra(0,1,0);
		setBlockMode(Blocks.STONE);
		this.setDeathTimer(60);
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		if(deadTick > 0 && deadTick == curTick) {
			setDead();
			if(onGround && onGroundBlock != null){
				worldObj.setBlockState(getPosition(), onGroundBlock.getDefaultState());
			}
		}
		if(deadTick == 0 && onGround && onGroundBlock != null){
			worldObj.setBlockState(getPosition(), onGroundBlock.getDefaultState());
			this.setDead();
		}

		curTick++;
	}
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		return noAttack ? false : super.attackEntityFrom(source, amount);
	}

	/**
	 * 땅에 떨어지면 블럭을 설치함
	 * 데드타이머가 설정되어 있는 경우에는 죽을 때 블럭이 설정됨
	 */
	public void setFallBlock(Block onGroundBlock) {
		this.onGroundBlock = onGroundBlock;
	}

	/**
	 * 공격 받는지
	 */
	public EntityBlock noAttack(boolean noAttack) {
		this.noAttack = noAttack;
		return this;
	}

	/**
	 * 특정 시간이 지나면 죽음
	 */
	public EntityBlock setTime(int tick) {
		this.deadTick = tick;
		return this;
	}

	/**
	 * 충돌시 폭발함
	 */
	public EntityBlock setColideExplosion() {
		isCollideExplosion = true;
		setBlockMode(Blocks.ANVIL);
		return this;
	}

	/**
	 	충돌시 이 엔티티는 밀려남
	 */
	public EntityBlock setCollideKnockback() {
		isCollideKnockback = true;
		setBlockMode(Blocks.BEDROCK);
		return this;
	}
	
	@Override
	public void onCollideWithPlayer(EntityPlayer entityIn) {
		super.onCollideWithPlayer(entityIn);
		if(isCollideExplosion) {
			System.out.println("블럭이 플레이어와 충돌해 폭발함");
			this.worldObj.createExplosion(this, posX, posY, posZ, 1.5F, false);
			this.setDead();
		}
		if (isCollideKnockback) {
			System.out.println("블럭이 플레이어와 충돌해 밀려남");
			((EntityLivingBase) this).knockBack(entityIn, 0.2F, entityIn.posX - this.posX, entityIn.posZ - this.posZ);
		}
	}

	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
		spawnX = posX;
		spawnY = posY;
		spawnZ = posZ;
		return super.onInitialSpawn(difficulty, livingdata);
	}
	

}
