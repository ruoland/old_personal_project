package ruo.halloween.miniween;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.gameevent.TickEvent.Type;
import ruo.halloween.EntityBigWeen;
import ruo.halloween.EntityWeen;
import ruo.minigame.api.WorldAPI;
import ruo.minigame.effect.AbstractTick;
import ruo.minigame.effect.TickRegister;
import ruo.minigame.map.EntityDefaultNPC;

public class EntityMiniWeen2 extends EntityDefaultNPC {
	public EntityWeen ween;
	public double aX, aY, aZ;
	public boolean isNightMode;
	public boolean isAttackReverse;
	public boolean isAttackMode, moveStop, isDefenceMode, isBigWeen;
	
	public EntityMiniWeen2(World worldIn) {
		super(worldIn);
		setTra(0,1,0);
		setBlockMode(Blocks.PUMPKIN);
		setCollision(true);
	}
	public EntityMiniWeen2(World worldIn, EntityWeen ween, double x, double y, double z, boolean isAttack, boolean isDefence) {
		this(worldIn);
		aX = x;
		aY = y;
		aZ = z;
		this.ween = ween;
		this.isAttackMode = isAttack;
		this.isDefenceMode = isDefence;
		TickRegister.register(new AbstractTick(400, false) {
			@Override
			public void run(Type type) {
				setDead();
			}
		});
	
	}

	@Override
	protected void collideWithEntity(Entity entityIn) {
		if (entityIn instanceof EntityBigWeen || entityIn instanceof EntityMiniWeen2)
			return;
		super.collideWithEntity(entityIn);
	}

	@Override
	public void onCollideWithPlayer(EntityPlayer entityIn) {
		super.onCollideWithPlayer(entityIn);
		if(isAttackMode) {
			System.out.println("미니윈이 플레이어와 충돌해 폭발함");
			this.worldObj.createExplosion(this, posX, posY, posZ, 1.0F, false);
			this.setDead();
			moveStop = true;
			aX = 0;
			aY = 0;
			aZ = 0;
		}
		if (isDefenceMode) {
			System.out.println("미니윈이 플레이어와 충돌해 밀려남");
			((EntityLivingBase) this).knockBack(entityIn, 0.2F, entityIn.posX - this.posX, entityIn.posZ - this.posZ);
		}
	}
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		if(isNightMode && isFly) {
			return super.attackEntityFrom(source, amount+2000);
		}
		if(source.isExplosion() || !isDefenceMode && !isAttackMode)
			return false;
		if(isAttackMode) 
		{
			System.out.println("미니윈이 반사됨");
			aX = ween.posX;
			aY = ween.posY+5;
			aZ = ween.posZ;
			isAttackReverse = true;
		}
		return super.attackEntityFrom(source, amount);
	}


	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		if(WorldAPI.getPlayer() == null)
			return;
		if(ween != null && ween.pattern() == 5 && !isNightMode) {
			this.setDead();
		}
		if(ween != null && ween.pattern() == 5 && isNightMode) {
			this.setPosition(posX, ween.posY, posZ);
		}
		if (aX != 0 && aY != 0 && aZ != 0) {
			if(!moveStop) {
				if(isAttackMode) {
					addRotate(rand.nextInt(10),rand.nextInt(10),rand.nextInt(10));
					this.setVelocity((aX - posX) / 12, (aY - posY) / 12, (aZ - posZ) / 12);
					if(isAttackReverse && this.getDistance(aX, aY, aZ) < 10) {
						this.worldObj.createExplosion(this, posX, posY, posZ, 1.5F, false);
						this.setDead();
						moveStop = true;
						aX = 0;
						aY = 0;
						aZ = 0;
						System.out.println("반사된 미니윈이 윈 근처에 도달해 터짐");
						ween.attackEntityFrom(DamageSource.causeExplosionDamage(this), 3);
					}
					if(this.getDistance(aX, aY, aZ) < 0.5){
						this.worldObj.createExplosion(this, posX, posY, posZ, 1.0F, false);
						this.setDead();
					}
				}
				else if(isNightMode)
				{	
					if(this.getDistance(aX, aY, aZ) < 3){
						moveStop = true;
					}
				}
				else{
					this.setVelocity((aX - posX) / 20, (aY - posY) / 20, (aZ - posZ) / 20);
					if(isBigWeen) {
						if(this.getDistance(aX, aY, aZ) < 6){
							this.setDead();
						}
					}
				}
			}
		}
	}

	@Override
	public void fall(float distance, float damageMultiplier) {
		super.fall(distance, damageMultiplier);
		if(!isAttackMode && !isDefenceMode && isNightMode) {
			System.out.println("밤 모드의 미니윈이 추락함");
			this.worldObj.createExplosion(this, posX, posY, posZ, 1.0F, false);
			this.setDead();
		}
	}
	
	/**
	 * double maxX = Math.max(posX, player.posX); double maxY = Math.max(posY,
	 * player.posY); double maxZ = Math.max(posZ, player.posZ); double minX =
	 * Math.min(posX, player.posX); double minY = Math.min(posY, player.posY);
	 * double minZ = Math.min(posZ, player.posZ); if(mode == 0)
	 * this.setVelocity((posX- player.posX) / 100, 0, (posZ - player.posZ) / 100);
	 * 
	 * if(mode == 1) this.setVelocity((player.posX- posX) / 10, player.posY - posY,
	 * (player.posZ - posZ) / 10);
	 * 
	 * if(mode == 2) this.setVelocity((maxX - minX) / 100, 0, (maxZ - minZ) / 100);
	 * 
	 * if(mode == 3) this.setVelocity((minX - maxX) / 100, 0, (minZ - maxZ) / 100);
	 */

}
