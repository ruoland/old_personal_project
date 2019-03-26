package oneline.effect;

import cmplus.cm.beta.EntityEnderPearl2;
import oneline.api.EntityAPI;
import oneline.api.WorldAPI;
import oneline.map.EntityDefaultNPC;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.gameevent.TickEvent.Type;

import java.util.HashMap;

/**
 * @author oprond
 * 엔티티 관련 연출이나 기능을 정리해놓은 코드
 */
public class ENEffect {
	private static HashMap<EntityLiving, AbstractTick> look = new HashMap<EntityLiving, AbstractTick>();
	public static void rotateYawRotate(EntityLivingBase base, EnumFacing facing) {
		TickRegister.register(new AbstractTick(1, true) {
			@Override
			public void run(Type type) {
				EnumFacing lookfacing = base.getHorizontalFacing();
				if(lookfacing != facing) {
					if(base instanceof EntityPlayer) {
						EntityPlayerMP p = WorldAPI.getPlayerMP();
						p.connection.setPlayerLocation(p.posX, p.posY, p.posZ, (float) p.rotationYaw+1, p.rotationPitch);
					}
					base.rotationYaw+=1;
					base.rotationYawHead+=1;
				} else {
					absLoop = false;
				} 

			}
		});
	}
	public static void rotateYawRotate(EntityLivingBase base, float yaw) {
		TickRegister.register(new AbstractTick(1, true) {
			@Override
			public void run(Type type) {
				if(base.rotationYaw == yaw) {
					if(base instanceof EntityPlayer) {
						EntityPlayerMP p = WorldAPI.getPlayerMP();
						p.connection.setPlayerLocation(p.posX, p.posY, p.posZ, (float) p.rotationYaw+1, p.rotationPitch);
					}
					base.rotationYaw+=1;
					base.rotationYawHead+=1;
				} else {
					absLoop = false;
				} 

			}
		});
	}
	/**
	 * 플레이어가 자고 있으면 일어나게 함 문제가 은근 많은 것 같음
	 */
	public static void wakeUp() {
		if (Minecraft.getMinecraft().thePlayer.isPlayerSleeping()) {
			NetHandlerPlayClient nethandlerplayclient = Minecraft.getMinecraft().thePlayer.connection;
			nethandlerplayclient.sendPacket(new CPacketEntityAction(Minecraft.getMinecraft().thePlayer,
					CPacketEntityAction.Action.STOP_SLEEPING));
		}
	}

	
	public static EntityItem spawnItem(Block items, double x, double y, double z, int count) {
		return spawnItem(new ItemStack(items, count), x, y, z);
	}

	public static EntityItem spawnItem(Item items, BlockPos pos, int count) {
		return spawnItem(new ItemStack(items, count), pos.getX(), pos.getY(), pos.getZ());
	}

	public static EntityItem spawnItem(Item items, double x, double y, double z, int count) {
		return spawnItem(new ItemStack(items, count), x, y, z);
	}

	public static EntityItem spawnItem(Block items, EntityLiving mob, int count) {
		return spawnItem(new ItemStack(items, count), mob.posX, mob.posY + mob.getEyeHeight(), mob.posZ);
	}

	public static EntityItem spawnItem(Item items, EntityLiving mob, int count) {
		return spawnItem(new ItemStack(items, count), mob.posX, mob.posY + mob.getEyeHeight(), mob.posZ);
	}

	public static EntityItem spawnItem(ItemStack items, EntityLiving mob) {
		return spawnItem(items, mob.posX, mob.posY + mob.getEyeHeight(), mob.posZ);
	}

	public static EntityItem spawnItem(ItemStack items, double x, double y, double z) {
		EntityItem item = new EntityItem(WorldAPI.getWorld(), x, y, z, items);
		WorldAPI.getWorld().spawnEntityInWorld(item);
		return item;
	}

	public static void spawnPotion(EntityLivingBase dea, EntityLivingBase target, PotionType type) {
		double d0 = target.posY + (double) target.getEyeHeight() - 1.1D;
		double d1 = target.posX + target.motionX - dea.posX;
		double d2 = d0 - dea.posY;
		double d3 = target.posZ + target.motionZ - dea.posZ;
		float f = MathHelper.sqrt_double(d1 * d1 + d3 * d3);
		PotionType potiontype = type;
		EntityPotion entitypotion = new EntityPotion(WorldAPI.getWorld(), dea,
				PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), potiontype));
		entitypotion.rotationPitch -= -20.0F;
		entitypotion.setThrowableHeading(d1, d2 + (double) (f * 0.2F), d3, 0.75F, 8.0F);
		WorldAPI.getWorld().playSound((EntityPlayer) null, dea.posX, dea.posY, dea.posZ, SoundEvents.ENTITY_WITCH_THROW,
				dea.getSoundCategory(), 1.0F, 0.8F + WorldAPI.getWorld().rand.nextFloat() * 0.4F);
		WorldAPI.getWorld().spawnEntityInWorld(entitypotion);
	}

	public static void spawnPotion(EntityLivingBase dea, double posX, double posY, double posZ, PotionType type) {
		double d0 = posY - 1;
		double d1 = posX + 0.1D - dea.posX;
		double d2 = d0 - dea.posY;
		double d3 = posZ + 0.1D - dea.posZ;
		float f = MathHelper.sqrt_double(d1 * d1 + d3 * d3);
		PotionType potiontype = type;
		EntityPotion entitypotion = new EntityPotion(WorldAPI.getWorld(), dea,
				PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), potiontype));
		entitypotion.rotationPitch -= -20.0F;
		setThrowableHeading(entitypotion, d1, d2 + (double) (f * 0.2F), d3,
				(float) dea.getDistance(posX, posY, posZ) / 5, 8.0F);
		WorldAPI.getWorld().playSound((EntityPlayer) null, dea.posX, dea.posY, dea.posZ, SoundEvents.ENTITY_WITCH_THROW,
				dea.getSoundCategory(), 1.0F, 0.8F + WorldAPI.getWorld().rand.nextFloat() * 0.4F);
		WorldAPI.getWorld().spawnEntityInWorld(entitypotion);
	}

	public static EntityItem throwItem(EntityLivingBase dea, double posX, double posY, double posZ, ItemStack items) {
		double d0 = posY - 1;
		double d1 = posX + 0.1D - dea.posX;
		double d2 = d0 - dea.posY;
		double d3 = posZ + 0.1D - dea.posZ;
		float f = MathHelper.sqrt_double(d1 * d1 + d3 * d3);
		EntityItem item = new EntityItem(WorldAPI.getWorld(), posX, posY, posZ, items);
		setThrowableHeading(item, d1, d2 + (double) (f * 0.2F), d3, (float) dea.getDistance(posX, posY, posZ) / 5,
				8.0F);

		WorldAPI.getWorld().spawnEntityInWorld(item);
		return item;
	}

	/**
	 * 0초 뒤에 아이템을 들고 1초가 지나면 아이템을 던집니다.
	 */
	public static void throwItemTime(final EntityLiving living, final EntityLivingBase target, final ItemStack item) {
		TickRegister.register(new AbstractTick(20, true) {
			@Override
			public void run(Type type) {
				if (absRunCount == 0) {
					living.setHeldItem(EnumHand.MAIN_HAND, item);
				}
				if (absRunCount == 1) {
					living.setHeldItem(EnumHand.MAIN_HAND, null);
					throwItem(living, target.posX, target.posY, target.posZ, item);
				}
			}
		});
	}

	/**
	 * 0초 뒤에 아이템을 들고 1초가 지나면 아이템을 던집니다.
	 */
	public static void throwItemTime(final EntityLiving living, final double x, final double y, final double z,
			final ItemStack item) {
		TickRegister.register(new AbstractTick(20, true) {
			@Override
			public void run(Type type) {
				if (absRunCount == 0) {
					living.setHeldItem(EnumHand.MAIN_HAND, item);
				}
				if (absRunCount == 1) {
					living.setHeldItem(EnumHand.MAIN_HAND, null);
					throwItem(living, x, y, z, item);
				}
			}
		});
	}

	public static void setThrowableHeading(Entity entity, double x, double y, double z, float velocity,
			float inaccuracy) {
		float f = MathHelper.sqrt_double(x * x + y * y + z * z);
		x = x / (double) f;
		y = y / (double) f;
		z = z / (double) f;
		x = x + WorldAPI.rand.nextGaussian() * 0.007499999832361937D * (double) inaccuracy;
		y = y + WorldAPI.rand.nextGaussian() * 0.007499999832361937D * (double) inaccuracy;
		z = z + WorldAPI.rand.nextGaussian() * 0.007499999832361937D * (double) inaccuracy;
		x = x * (double) velocity;
		y = y * (double) velocity;
		z = z * (double) velocity;
		entity.motionX = x;
		entity.motionY = y;
		entity.motionZ = z;
		float f1 = MathHelper.sqrt_double(x * x + z * z);
		entity.rotationYaw = (float) (MathHelper.atan2(x, z) * (180D / Math.PI));
		entity.rotationPitch = (float) (MathHelper.atan2(y, (double) f1) * (180D / Math.PI));
		entity.prevRotationYaw = entity.rotationYaw;
		entity.prevRotationPitch = entity.rotationPitch;
	}

	public static void ender(EntityLivingBase entity, float pitch, float yaw) {
		if (entity.isServerWorld()) {
			entity.rotationPitch = pitch;
			entity.rotationYaw = yaw;
			EntityEnderPearl2 entityenderpearl = new EntityEnderPearl2(WorldAPI.getWorld(), entity);
			entityenderpearl.setHeadingFromThrower(entity, pitch, yaw, 0.0F, 1.5F, 1.0F);
			WorldAPI.getWorld().spawnEntityInWorld(entityenderpearl);
		}
	}

	public static void ender(EntityLivingBase entity, double x, double y, double z, float pitch, float yaw) {
		if (entity.isServerWorld()) {
			EntityEnderPearl2 entityenderpearl = new EntityEnderPearl2(WorldAPI.getWorld(), entity, x, y, z, pitch,
					yaw);
			setThrowableHeading(entity, x, y + 10, z, (float) entity.getDistance(x, y, z), 8.0F);
			WorldAPI.getWorld().spawnEntityInWorld(entityenderpearl);
		}
	}

	/**
	 * 틱마다 엔더진주를 던짐
	 */
	public static void enderTick(int tick, final int count2, final EntityLivingBase entity, final float pitch,
			final float yaw) {
		TickRegister.register(new AbstractTick(tick, true) {
			@Override
			public void run(Type type) {
				if (count2 == this.absRunCount) {
					absLoop = false;
					return;
				}
				ender(entity, pitch, yaw);
			}
		});
	}

	/**
	 * 틱마다 엔더진주를 던짐
	 */
	public static void enderTick(int tick, final int count2, final EntityLivingBase entity, double x, double y,
			double z, final float pitch, final float yaw) {
		TickRegister.register(new AbstractTick(tick, true) {
			@Override
			public void run(Type type) {
				if (count2 == this.absRunCount) {
					absLoop = false;
					return;
				}
				ender(entity, x, y, z, pitch, yaw);
			}
		});
	}

	/**
	 * 엔티티를 현재 posY + maxY 좌표까지 올라가게 함
	 */
	public static void entityJump(EntityLivingBase base, double maxY, AbstractTick abs) {
		if (!base.isServerWorld()) {
			System.out.println("서버 월드가 아니므로 캔슬됨");
			return;
		}
		double startPosY = base.posY;
		boolean completeY = false;// 목표 좌표에 도달하면 true로 설정 됨

		TickRegister.register(new AbstractTick(Type.SERVER, 2, true) {
			@Override
			public boolean stopCondition() {

				return base.isDead;
			}

			@Override
			public void run(Type type) {
				base.addVelocity(0, 0.23, 0);
				System.out.println("상승중");
				if (startPosY + maxY <= base.posY) {
					absLoop = false;
					System.out.println("도달함");
					if (abs != null)
						abs.run(type);
				}
			}
		});
	}

	/**
	 * 주변 엔티티를 넉백 시킴(onground 는 땅에 붙어있을 때만 넉백 됨)
	 */
	public static void entityKnockBack(EntityLivingBase base2, double x, double y, double z, float strength,
			boolean onground) {
		for (EntityLivingBase base : base2.worldObj.getEntitiesWithinAABB(EntityLivingBase.class,
				base2.getEntityBoundingBox().expand(x, y, z))) {
			if (base == base2 || (onground && !base.onGround))
				continue;
			base.knockBack(base2, strength, base2.posX - base.posX, base2.posZ - base.posZ);
		}
	}

	/**
	 * 주변 엔티티를 데미지를 줌
	 */
	public static void entityDamage(EntityLivingBase base2, double x, double y, double z, DamageSource source,
			float amount) {
		for (EntityLivingBase base : base2.worldObj.getEntitiesWithinAABB(EntityLivingBase.class,
				base2.getEntityBoundingBox().expand(x, y, z))) {
			if (base == base2)
				continue;
			base.attackEntityFrom(source, amount);
		}
	}

	/**
	 * 주변 엔티티를 넉백 시키고 데미지를 줌(onground 는 땅에 붙어있을 때만 넉백 됨)
	 */
	public static void entityKnockBackDamage(EntityLivingBase base2, double x, double y, double z, float strength,
			boolean onground, DamageSource source, float amount) {
		for (EntityLivingBase base : base2.worldObj.getEntitiesWithinAABB(EntityLivingBase.class,
				base2.getEntityBoundingBox().expand(x, y, z))) {
			if (base == base2 || (onground && base.motionY > 0))
				continue;
			base.knockBack(base2, strength, base2.posX - base.posX, base2.posZ - base.posZ);
			base.attackEntityFrom(source, amount);
		}
	}

	/**
	 * speed가 낮으면 속도는 빨라짐
	 */
	public static void entityMoveFly(EntityLivingBase base, double x, double y, double z, float speed,
			AbstractTick abs) {
		TickRegister.register(new AbstractTick(1, true) {
			@Override
			public boolean stopCondition() {
				return base.isDead;
			}

			@Override
			public void run(Type type) {
				base.setVelocity((x - base.posX) / speed, (y - base.posY) / speed, (z - base.posZ) / speed);
				if (base.getDistance(x, y, z) < 1) {
					if (abs != null)
						abs.run(type);
				}
			}
		});
	}

	/**
	 * speed가 낮으면 속도는 빨라짐 해당 좌표에 도달시 폭발함
	 */
	public static void entityMoveFlyExplosion(EntityLivingBase base, double x, double y, double z, float speed,
			float strength) {
		TickRegister.register(new AbstractTick(1, true) {
			@Override
			public boolean stopCondition() {

				return base.isDead;
			}

			@Override
			public void run(Type type) {
				base.setVelocity((x - base.posX) / speed, (y - base.posY) / speed, (z - base.posZ) / speed);
				if (base.getDistance(x, y, z) < 1) {
					base.worldObj.createExplosion(base, base.posX, base.posY, base.posZ, strength, true);

				}
			}
		});
	}

	public static void entityRotateX(EntityDefaultNPC entity, double x, AbstractTick tick2) {
		TickRegister.register(new AbstractTick(1, true) {

			@Override
			public boolean stopCondition() {
				return entity.isDead;
			}

			@Override
			public void run(Type type) {
				boolean minus = ("" + x).startsWith("-");
				entity.addRotate(minus ? -1.5F : 1.5F,0,0);
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
				boolean minus = ("" + x).startsWith("-");
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
				boolean minus = ("" + x).startsWith("-");
				entity.addRotate(0,0,minus ? -1.5F : 1.5F);
				if ((minus && entity.getRotateZ() <= x) || (!minus && entity.getRotateZ() >= x)) {
					absLoop = false;
					tick2.run(type);
				}
			}
		});
	}

	public void blockBreak(final EntityLivingBase base, int x, int y, int z){
		final BlockPos pos = new BlockPos(x,y,z);
		final IBlockState s = base.worldObj.getBlockState(pos);
		final Minecraft mc = Minecraft.getMinecraft();
		final SoundType soundtype = s.getBlock().getSoundType(s, mc.theWorld, pos, mc.thePlayer);
		final PositionedSoundRecord posound = new PositionedSoundRecord(soundtype.getHitSound(),
				SoundCategory.NEUTRAL, (soundtype.getVolume() + 1.0F) / 8.0F, soundtype.getPitch() * 0.5F, pos);
		EntityAPI.look((EntityMob) base, x,y,z);
		TickRegister.register(new AbstractTick(10, true) {
			@Override
			public void run(Type type) {
				base.swingArm(EnumHand.MAIN_HAND);
				mc.getSoundHandler().playSound(posound);
				if (absRunCount == 48)
					base.worldObj.destroyBlock(pos, false);
				if (base.worldObj.isAirBlock(pos))
					absLoop = false;
			}
		});
	}
	
	public static EntityDefaultNPC createNPC(String name, double posx, double posy, double posz){
		EntityDefaultNPC defaultNPC = new EntityDefaultNPC(WorldAPI.getWorld());
		defaultNPC.setPosition(posx, posy, posz);
		defaultNPC.setCustomNameTag(name);
		WorldAPI.getWorld().spawnEntityInWorld(defaultNPC);
		return defaultNPC;
	}
	public static EntityDefaultNPC[] spawnNPC(EntityDefaultNPC... defaultNPC){
		for(int i = 0; i < defaultNPC.length;i++){
			WorldAPI.getWorld().spawnEntityInWorld(defaultNPC[i]);
		}
		return defaultNPC;
	}

}
