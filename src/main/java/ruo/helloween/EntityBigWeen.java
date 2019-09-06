package ruo.helloween;

import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.gameevent.TickEvent.Type;
import olib.api.WorldAPI;
import olib.effect.TickTask;
import olib.effect.TickRegister;
import olib.map.EntityDefaultNPC;
import ruo.helloween.miniween.EntityMiniWeen;


public class EntityBigWeen extends EntityDefaultNPC {
	public EntityWeen ween;

	public EntityBigWeen(World worldIn) {
		super(worldIn);
		this.setSize(1, 1);
		setBlockMode(Blocks.TNT);
		this.setTra(0,-4,0);
		this.addScale(1);
		this.addRotate(0,0.1F,0);
		this.addRotate(0,0,180);
	}

	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(0);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(60.0D);
	}

	double targetX, targetY, targetZ;
	boolean isFivePattern;

	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
		if(isFivePattern)
			TickRegister.register(new TickTask(20, true) {
				@Override
				public void run(Type type) {
					setPosition(ween.posX + WorldAPI.minRand(5, 20), posY,
							ween.posZ + WorldAPI.minRand(5, 20));
				}
			});
		return super.onInitialSpawn(difficulty, livingdata);
		
	}

	@Override
	protected void collideWithEntity(Entity entityIn) {
		if (entityIn instanceof EntityBigWeen || entityIn instanceof EntityMiniWeen)
			return;
		super.collideWithEntity(entityIn);
	}
	private int delay = 0;

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();

		onGround = true;
		if (isDead || WorldAPI.getPlayer() == null || !isServerWorld())
			return;
		if (targetX == 0 && targetY == 0 && targetZ == 0)
			this.setVelocity(0, 0, 0);
		else
			this.setVelocity((targetX - posX) / 7, (targetY - posY) / 7, (targetZ - posZ) / 7);

		delay++;
		if(delay > 20) {
			delay = 0;
			float size = (15 / 250);
			this.setSize(getScaleX() + size, getScaleZ() +  size);
			addScale(size);
		}

		if (getScaleX() > 15 && targetX == 0 && targetY == 0 && targetZ == 0) {
			System.out.println("완전히 커짐");
			targetX = WorldAPI.x();
			targetY = WorldAPI.y() + 5;
			targetZ = WorldAPI.z();
		}
		if (getDistance(targetX, targetY, targetZ) <= 1) {
			System.out.println("목표 지점 도달함");
			this.worldObj.createExplosion(this, posX, posY, posZ, 20F, false);
			this.setDead();
			ween.setSturn(100);
			TickRegister.register(new TickTask(230, false) {

				@Override
				public void run(Type type) {
					if (!isFivePattern) {
						System.out.println("[세번째 패턴]빅윈이 죽어서 네번째 패턴 더 나이트를 사용함");
						if (ween != null)
							ween.theNight();
					} else {
						System.out.println("[다섯번째 패턴]빅윈이 죽어서 여섯번째 패턴 대점프를 사용함");
						ween.jumpStartSixWeen();
					}
				}
			});
		}
	}

	@Override
	public void onDeath(DamageSource cause) {
		if(ween == null)
			return;
		this.worldObj.createExplosion(this, ween.posX, ween.posY+5, ween.posZ, 5F, false);
		if (isServerWorld())
			TickRegister.register(new TickTask(230, false) {
				@Override
				public void run(Type type) {
					if (!isFivePattern) {
						System.out.println("빅윈이 터지기 전에 죽음 밤으로 설정함");
						if (ween != null)
							ween.theNight();
					} else {
						ween.jumpStartSixWeen();
                        EntityPlayerWeen.thisKill = true;
                        TickRegister.register(new TickTask(40, false) {
                                    @Override
                                    public void run(Type type) {
                                        EntityPlayerWeen.thisKill =false;
                                    }
                                });
					}
				}
			});
		super.onDeath(cause);
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
