package ruo.halloween;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.gameevent.TickEvent.Type;
import ruo.cmplus.util.Sky;
import ruo.halloween.miniween.EntityBlockWeen;
import ruo.halloween.miniween.EntityMiniWeen;
import ruo.minigame.api.WorldAPI;
import ruo.minigame.effect.AbstractTick;
import ruo.minigame.effect.TickRegister;
import ruo.minigame.map.EntityDefaultNPC;

import java.util.Random;

/**
 * ************************************************************************
 * ************************************************************************
 * ************************************************************************
 * *************************MGEffect로 fall 빼고 다 옮김***************************
 * ************************************************************************
 * ************************************************************************
 * ************************************************************************
 */
public class WeenEffect {

	public static void spawnBlockWeen(World worldObj, double posX, double posY, double posZ, double distanceXZ, double distanceY){
		Random rand = worldObj.rand;
		WorldAPI.blockTick(worldObj, posX - distanceXZ, posX + distanceXZ, posY - distanceY, posY + distanceY, posZ - distanceXZ, posZ + distanceXZ,
				new AbstractTick.BlockXYZ() {
					@Override
					public void run(Type type) {
						if (rand.nextInt(3) == 0) {
								EntityBlockWeen blockWeen = new EntityBlockWeen(worldObj);
								blockWeen.setPosition(getPos());
								blockWeen.addRotate(rand.nextInt(90), rand.nextInt(90), rand.nextInt(90));
								blockWeen.addVelocity(WorldAPI.rand(9), rand.nextInt(3), WorldAPI.rand(9));
								System.out.println("소환됨"+blockWeen);
							}
						}
					});
	}
	static double currentY, currentZ, currentX;
	public static void entityJumpMap(int maxCount, double startX, double startY, double startZ, boolean minus, boolean isY) {
		currentY = 0;
		currentZ = 0;
		for (int i = 0; i < maxCount; i++) {
			if (currentY == 0 && currentZ == 0) {
				EntityBlock miniween = new EntityBlock(WorldAPI.getWorld());
				miniween.isFly = true;
				miniween.setPosition(startX, startY + currentY, startZ + currentZ);
				WorldAPI.getWorld().spawnEntityInWorld(miniween);
				currentZ++;
				continue;
			}
			if (isY && WorldAPI.rand.nextInt(2) == 0) {
				currentY++;
				if(!minus) {
					currentZ +=WorldAPI.rand.nextInt(4)+Double.valueOf("0."+WorldAPI.rand.nextInt(10));
				}
				if(minus) {
					currentZ -=WorldAPI.rand.nextInt(4)+Double.valueOf("0."+WorldAPI.rand.nextInt(10));
				}
				System.out.println("Y 증가함");
			} else {
				if(!minus) {
					currentZ += Double.valueOf("0."+WorldAPI.rand.nextInt(10))+ WorldAPI.rand.nextInt(4);
				}
				if(minus) {
					currentZ -= Double.valueOf("0."+WorldAPI.rand.nextInt(10))+ WorldAPI.rand.nextInt(4);
				}
				
				if (currentY == 0)
					currentY++;
			}
			
			EntityBlock miniween = new EntityBlock(WorldAPI.getWorld());
			miniween.isFly = true;

			if(!minus)
				miniween.setPosition(startX + WorldAPI.rand(4), startY - 1 + currentY, startZ - 45 + currentZ);
			if(minus)
				miniween.setPosition(startX + WorldAPI.rand(4), startY - 1 + currentY, startZ + 45 + currentZ);
			WorldAPI.getWorld().spawnEntityInWorld(miniween);
		}
	}
	public static void entityRotateX(EntityDefaultNPC entity, double x, AbstractTick tick2) {
		TickRegister.register(new AbstractTick(1, true) {
			
			@Override
			public boolean stopCondition() {
				return entity.isDead;
			}
			@Override
			public void run(Type type) {
				boolean minus = (""+x).startsWith("-");
				entity.addRotate(minus ? -2.5F : 2.5F,0,0);
				if ((minus && entity.getRotateX() <= x) || (!minus && entity.getRotateX() >= x)) {
					absLoop = false;
					tick2.run(type);
				}
			}
		});
	}

	public static void entityRotateY(EntityDefaultNPC entity, double x, AbstractTick tick2) {
		TickRegister.register(new AbstractTick(1, true) {
			@Override
			public boolean stopCondition() {
				
				return entity.isDead;
			}
			@Override
			public void run(Type type) {
				boolean minus = (""+x).startsWith("-");
				entity.addRotate(0,minus ? -1.5F : 1.5F,0);
				if ((minus && entity.getRotateY() <= x) || (!minus && entity.getRotateY() >= x)) {
					absLoop = false;
					tick2.run(type);
				}
			}
		});
	}

	public static void entityRotateZ(EntityDefaultNPC entity, double x, AbstractTick tick2) {
		TickRegister.register(new AbstractTick(1, true) {
			
			@Override
			public boolean stopCondition() {
				
				return entity.isDead;
			}
			@Override
			public void run(Type type) {
				boolean minus = (""+x).startsWith("-");
				entity.addRotate(0,0,minus ? -1.5F : 1.5F);
				if ((minus && entity.getRotateZ() <= x) || (!minus && entity.getRotateZ() >= x)) {
					absLoop = false;
					tick2.run(type);
				}
			}
		});
	}
	public static void entityPumpkinStairs(EntityWeen entity, int maxCount, double posX, double posY, double posZ, boolean minus) {
		currentY = 0;
		currentZ = 0;
		for (int i = 0; i < maxCount; i++) {
			if (currentY == 0 && currentZ == 0) {
				EntityMiniWeen miniween = new EntityMiniWeen(entity.worldObj);
				miniween.isFly = true;
				miniween.ween = entity;
				miniween.setPosition(posX, posY + currentY, posZ + currentZ);
				entity.worldObj.spawnEntityInWorld(miniween);
				currentZ++;
				continue;
			}
			if (WorldAPI.rand.nextInt(2) == 0) {
				currentY++;
				if(!minus) {
					currentZ +=WorldAPI.rand.nextInt(4)+Double.valueOf("0."+WorldAPI.rand.nextInt(10));
				}
				if(minus) {
					currentZ -=WorldAPI.rand.nextInt(4)+Double.valueOf("0."+WorldAPI.rand.nextInt(10));
				}
				System.out.println("Y 증가함");
			} else {
				if(!minus) {
					currentZ += Double.valueOf("0."+WorldAPI.rand.nextInt(10))+ WorldAPI.rand.nextInt(4);
				}
				if(minus) {
					currentZ -= Double.valueOf("0."+WorldAPI.rand.nextInt(10))+ WorldAPI.rand.nextInt(4);
				}
				
				if (currentY == 0)
					currentY++;
			}
			
			EntityMiniWeen miniween = new EntityMiniWeen(entity.worldObj);
			miniween.isFly = true;
			miniween.ween = entity;

			if(!minus)
				miniween.setPosition(posX + WorldAPI.rand(4), posY - 1 + currentY, posZ - 45 + currentZ);
			if(minus)
				miniween.setPosition(posX + WorldAPI.rand(4), posY - 1 + currentY, posZ + 45 + currentZ);
			entity.worldObj.spawnEntityInWorld(miniween);
		}
	}

	public static void entityPumpkinStairs2(EntityWeen entity, int maxCount, double posX, double posY, double posZ, boolean minus) {
		currentY = 0;
		currentX = 0;
		for (int i = 0; i < maxCount; i++) {
			if (currentY == 0 && currentX == 0) {
				EntityMiniWeen miniween = new EntityMiniWeen(entity.worldObj);
				miniween.isFly = true;
				miniween.ween = entity;
				miniween.setPosition(posX+ currentX, posY + currentY, posZ );
				entity.worldObj.spawnEntityInWorld(miniween);
				currentX++;
				continue;
			}
			if (WorldAPI.rand.nextInt(2) == 0) {
				currentY++;
				if(!minus) {
					currentX +=WorldAPI.rand.nextInt(4)+Double.valueOf("0."+WorldAPI.rand.nextInt(10));
				}
				if(minus) {
					currentX -=WorldAPI.rand.nextInt(4)+Double.valueOf("0."+WorldAPI.rand.nextInt(10));
				}
				System.out.println("Y 증가함");
			} else {
				if(!minus) {
					currentX += Double.valueOf("0."+WorldAPI.rand.nextInt(10))+ WorldAPI.rand.nextInt(4);
				}
				if(minus) {
					currentX -= Double.valueOf("0."+WorldAPI.rand.nextInt(10))+ WorldAPI.rand.nextInt(4);
				}
				
				if (currentY == 0)
					currentY++;
			}
			
			EntityMiniWeen miniween = new EntityMiniWeen(entity.worldObj);
			miniween.isFly = true;
			miniween.ween = entity;

			if(!minus)
				miniween.setPosition(posX - 45 + currentX , posY - 1 + currentY, posZ + WorldAPI.rand(5));
			if(minus)
				miniween.setPosition(posX + 45 + currentX, posY - 1 + currentY, posZ + WorldAPI.rand(5));
			entity.worldObj.spawnEntityInWorld(miniween);
		}
	}
	
	/**
	 * 엔티티를 현재 posY + maxY 좌표까지 올라가게 함
	 */
	public static void entityJump(EntityLivingBase base, double maxY, AbstractTick abs) {
		if(!base.isServerWorld()) {
			System.out.println("서버 월드가 아니므로 캔슬됨");
			return;
		}
		double startPosY = base.posY;
		boolean completeY = false;//목표 좌표에 도달하면 true로 설정 됨

		TickRegister.register(new AbstractTick(Type.SERVER, 2, true) {
			@Override
			public boolean stopCondition() {
				
				return base.isDead;
			}
			@Override
			public void run(Type type) {
				base.addVelocity(0, 0.23, 0);
				if(startPosY + maxY <= base.posY) {
					absLoop = false;
					if(abs != null)
						abs.run(type);
				}
			}
		});
	}
	/**
	 * 주변 엔티티를 넉백 시킴(onground 는 땅에 붙어있을 때만 넉백 됨)
	 */
	public static void entityKnockBack(EntityLivingBase base2, double x, double y, double z, float strength, boolean onground) {
		for (EntityLivingBase base : base2.worldObj.getEntitiesWithinAABB(EntityLivingBase.class,
				base2.getEntityBoundingBox().expand(x,y,z))) {
			if (base == base2 || (onground && !base.onGround))
				continue;
			base.knockBack(base2, strength, base2.posX - base.posX, base2.posZ - base.posZ);
		}
	}
	
	/**
	 * 주변 엔티티를 데미지를 줌
	 */
	public static void entityDamage(EntityLivingBase base2, double x, double y, double z, DamageSource source, float amount) {
		for (EntityLivingBase base : base2.worldObj.getEntitiesWithinAABB(EntityLivingBase.class,
				base2.getEntityBoundingBox().expand(x,y,z))) {
			if (base == base2)
				continue;
			base.attackEntityFrom(source, amount);
		}
	}
	/**
	 * 주변 엔티티를 넉백 시키고 데미지를 줌(onground 는 땅에 붙어있을 때만 넉백 됨)
	 */
	public static void entityKnockBackDamage(EntityLivingBase base2, double x, double y, double z, float strength, boolean onground, DamageSource source, float amount) {
		for (EntityLivingBase base : base2.worldObj.getEntitiesWithinAABB(EntityLivingBase.class,
				base2.getEntityBoundingBox().expand(x,y,z))) {
			if(onground && base.motionY > 0) {
				System.out.println("넉백 됨!");
				base.knockBack(base2, strength, base2.posX - base.posX, base2.posZ - base.posZ);
				continue;
			}
			if (base == base2) {
				continue;
			}
			base.knockBack(base2, strength, base2.posX - base.posX, base2.posZ - base.posZ);
			base.attackEntityFrom(source, amount);
		}
	}
	/**
	 * 주변 엔티티를 넉백 시키고 데미지를 줌(onground 는 땅에 붙어있을 때만 넉백 됨)
	 */
	public static void entityKnockBackDamageJump(EntityLivingBase base2, double x, double y, double z, float strength, boolean onground, DamageSource source, float amount) {
		for (EntityLivingBase base : base2.worldObj.getEntitiesWithinAABB(EntityLivingBase.class,
				base2.getEntityBoundingBox().expand(x,y,z))) {
			if (base == base2 || (onground && base.motionY > 0))
				continue;
			base.knockBack(base2, strength, base2.posX - base.posX, base2.posZ - base.posZ);
			base.motionY+=0.3;
			base.attackEntityFrom(source, amount);
		}
	}
	/**
	 * speed가 낮으면 속도는 빨라짐
	 */
	public static void entityMoveFly(EntityLivingBase base, double x, double y, double z, float speed, AbstractTick abs) {
		TickRegister.register(new AbstractTick(1, true) {
			@Override
			public boolean stopCondition() {
				
				return base.isDead;
			}
			@Override
			public void run(Type type) {
				base.setVelocity((x - base.posX) / speed, (y - base.posY) / speed, (z - base.posZ) / speed);
				if(base.getDistance(x, y, z) < 1) {
					if(abs != null)
						abs.run(type);
				}
			}
		});
	}
	/**
	 * speed가 낮으면 속도는 빨라짐
	 * 해당 좌표에 도달시 폭발함
	 */
	public static void entityMoveFlyExplosion(EntityLivingBase base, double x, double y, double z, float speed, float strength) {
		TickRegister.register(new AbstractTick(1, true) {
			@Override
			public boolean stopCondition() {
				
				return base.isDead;
			}
			@Override
			public void run(Type type) {
				base.setVelocity((x - base.posX) / speed, (y - base.posY) / speed, (z - base.posZ) / speed);
				if(base.getDistance(x, y, z) < 1) {
					base.worldObj.createExplosion(base, base.posX, base.posY, base.posZ, strength, true);

				}
			}
		});
	}
	/**
	 * 떨어질 때 나타나는 파티클을 소환함
	 */
	public static void fallParticle(EntityLivingBase base,Block block, float fallDistance, double x, double y, double z) {
		float f = (float) MathHelper.ceiling_float_int(fallDistance * 100 - 3.0F);
		double d0 = Math.min((double) (0.2F + f / 15.0F), 2.5D);
		int i = (int) (150.0D * d0);
		IBlockState state = block.getDefaultState();
		if (!state.getBlock().addLandingEffects(state, (WorldServer) base.worldObj, base.getPosition(), state, base, i)) {
			((WorldServer) base.worldObj).spawnParticle(EnumParticleTypes.BLOCK_DUST, x,y,z, i, 0.0D, 0.0D, 0.0D, 0.15000000596046448D, new int[] { Block.getStateId(state) });
		}
	}
	/**
	 * 떨어질 때 나타나는 파티클을 소환함
	 */
	public static void fallParticle(EntityLivingBase base,IBlockState block, float fallDistance, double x, double y, double z) {
		fallParticle(base, block.getBlock(), fallDistance, x, y, z);
	}
	
	/**
	 * 떨어질 때 나타나는 파티클을 소환함
	 */
	public static void fallParticle(EntityLivingBase base,Block block, float fallDistance) {
		fallParticle(base, block, fallDistance, base.posX, base.posY, base.posZ);
	}
	/**
	 * 떨어질 때 나타나는 파티클을 소환함
	 */
	public static void fallParticle(EntityLivingBase base,IBlockState block, float fallDistance) {
		fallParticle(base, block.getBlock(), fallDistance, base.posX, base.posY, base.posZ);
	}
	
	/**
	 * 화면의 밝기를 천천히 낮춤
	 */
	public static void gammaDown(AbstractTick run) {
		float gamma = Minecraft.getMinecraft().gameSettings.gammaSetting;
		
		TickRegister.register(new AbstractTick(5, true) {
			
			@Override
			public void run(Type type) {
				WorldAPI.command("/gamma sub 0.01");
				if(gamma <= 0) {
					absLoop = false;
					run.run(type);
				}
			}
		});
	}
	
	
	/**
	 * 안개를 천천히 가깝게 만들음
	 */
	public static void fog(double maxDistance, AbstractTick run) {
		Sky.fogDistance(-1);

		TickRegister.register(new AbstractTick(1, true) {
			@Override
			public void run(Type type) {
				Sky.fogDistance(Sky.getFogDistance()-0.5F);
				if(Sky.getFogDistance() <= maxDistance) {
					absLoop = false;
					run.run(type);
				}
			}
		});
	}
}
