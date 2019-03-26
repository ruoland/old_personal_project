package cmplus.cm.beta;

import cmplus.util.CommandPlusBase;
import oneline.api.EntityAPI;
import oneline.api.WorldAPI;
import oneline.effect.ENEffect;
import oneline.map.EntityDefaultNPC;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

import java.util.List;

public class CommandEntity extends CommandPlusBase {
	int mode = 1;

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		
		
		Entity get = getPlusEntity(server, sender, args[0]);
		if(get instanceof EntityLivingBase){
			EntityLiving mob = (EntityLiving) get;
			if(args[mode].equalsIgnoreCase("child") || args[mode].equalsIgnoreCase("어린")){
				if(mob instanceof EntityAgeable) {
					EntityAgeable animal = (EntityAgeable) mob;
					animal.setGrowingAge(-10000);
					return;
				}
				EntityDefaultNPC npc = (EntityDefaultNPC) mob;
				npc.setChild(Boolean.valueOf(args[mode+1]));
			}
				
			if(args[mode].equalsIgnoreCase("health") || args[mode].equalsIgnoreCase("체력")){
				mob.setHealth((float) parseDouble(args[mode+1]));
			}
			if(args[mode].equalsIgnoreCase("speed") || args[mode].equalsIgnoreCase("속도")){
				mob.setAIMoveSpeed((float) parseDouble(args[mode+1]));
			}
			if(args[mode].equalsIgnoreCase("name") || args[mode].equalsIgnoreCase("이름")){
				mob.setCustomNameTag(args[mode+1]);
			}
			if(args[mode].equalsIgnoreCase("dead") || args[mode].equalsIgnoreCase("죽음")){
				mob.setDead();
			}
			if(args[mode].equalsIgnoreCase("fire") || args[mode].equalsIgnoreCase("불")){
				mob.setFire(parseInt(args[mode+1]));
			}
			if(args[mode].equalsIgnoreCase("item") || args[mode].equalsIgnoreCase("아이템")){
				mob.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(getItemByText(sender, args[mode+1])));
			}
			if(args[mode].equalsIgnoreCase("invisible") || args[mode].equalsIgnoreCase("투명")  || args[mode].equalsIgnoreCase("투명화")){
				mob.setInvisible(parseBoolean(args[mode+1]));
			}
			if(args[mode].equalsIgnoreCase("jump") || args[mode].equalsIgnoreCase("점프")){
				mob.setJumping(parseBoolean(args[mode+1]));
			}
			if(args[mode].equalsIgnoreCase("tp") || args[mode].equalsIgnoreCase("텔레포트")  || args[mode].equalsIgnoreCase("텔포")){
                int i = 2;
                int lvt_6_2_ = i + 1;
                CoordinateArg x = parseCoordinate(mob.posX, args[i], true);
                CoordinateArg y = parseCoordinate(mob.posY, args[lvt_6_2_++], -4096, 4096, false);
                CoordinateArg z = parseCoordinate(mob.posZ, args[lvt_6_2_++], true);
                CoordinateArg yaw = parseCoordinate((double)mob.rotationYaw, args.length > lvt_6_2_ ? args[lvt_6_2_++] : "~", false);
                CoordinateArg pitch = parseCoordinate((double)mob.rotationPitch, args.length > lvt_6_2_ ? args[lvt_6_2_] : "~", false);
                teleportEntityToCoordinates(mob, x,y,z,yaw,pitch);
			}
			if(args[mode].equalsIgnoreCase("noclip") || args[mode].equalsIgnoreCase("노클립")){
				mob.noClip = true;
			}
			if(args[mode].equalsIgnoreCase("potion") || args[mode].equalsIgnoreCase("포션")){
				potion(mob, args);
			}	
			if(args[mode].equalsIgnoreCase("damage") || args[mode].equalsIgnoreCase("데미지")  || args[mode].equalsIgnoreCase("대미지")){
				mob.attackEntityFrom(getDamageSource(args[mode+1]), parseInt(args[mode+3]));
			}	
			if(args[mode].equalsIgnoreCase("riding") || args[mode].equalsIgnoreCase("라이딩")){
				if(args.length > 1)
				mob.startRiding(getPlusEntity(server, sender, args[mode+1]));
				else
					mob.dismountRidingEntity();
			}
			if(args[mode].equalsIgnoreCase("attackdamage") || args[mode].equalsIgnoreCase("공격력"))
				mob.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(parseDouble(args[mode+1]));
			if(args[mode].equalsIgnoreCase("defence") || args[mode].equalsIgnoreCase("방어력"))
				mob.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(parseDouble(args[mode+1]));
			if(args[mode].equalsIgnoreCase("maxhealth") || args[mode].equalsIgnoreCase("최대체력"))
				mob.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(parseDouble(args[mode+1]));
			if(args[mode].equalsIgnoreCase("knockback") || args[mode].equalsIgnoreCase("넉백저항"))
				mob.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(parseDouble(args[mode+1]));
			
			if(args[mode].equalsIgnoreCase("move") || args[mode].equalsIgnoreCase("이동")){
				EntityAPI.move(mob, Double.valueOf(args[mode+1]), Double.valueOf(args[mode+2]), Double.valueOf(args[mode+3]));		
			}
			if(args[mode].equalsIgnoreCase("look") || args[mode].equalsIgnoreCase("보기")){
				if(args[0].equalsIgnoreCase("remove"))
					EntityAPI.removeLook(mob);
				else if(!args[0].equalsIgnoreCase("player"))
					EntityAPI.look(mob, Integer.valueOf(args[mode+1]), Integer.valueOf(args[mode+2]), Integer.valueOf(args[mode+3]));		
				else if(args[0].equalsIgnoreCase("player"))
					EntityAPI.lookPlayer(mob);		
			}
			if(args[mode].equalsIgnoreCase("ender") || args[mode].equalsIgnoreCase("엔더")){
				if(args.length >= 7){
					double[] pitch = WorldAPI.valueOfStr(args[mode+3],args[4],"0");
					double[] xyz = WorldAPI.valueOfStr(args[5], args[6], args[7]);
					ENEffect.ender((EntityLivingBase) getEntity(server, sender, args[mode+1]), xyz[0], xyz[1], xyz[2], (float) pitch[0],(float) pitch[1]);
				}else{
					double[] pitch = WorldAPI.valueOfStr(args[mode+3],args[4],"0");
					ENEffect.ender((EntityLivingBase) getEntity(server, sender, args[mode+1]), (float) pitch[0],(float) pitch[1]);
				}
			}
			
			if(args[mode].equalsIgnoreCase("velo") || args[mode].equalsIgnoreCase("중력")){
				if(args[mode+1].equalsIgnoreCase("-x") || args[mode+1].equalsIgnoreCase("x-")) {
					mob.motionX -= parseDouble(args[mode+2]);
				}
				if(args[mode+1].equalsIgnoreCase("-y") || args[mode+1].equalsIgnoreCase("y-")){
					mob.motionY -= parseDouble(args[mode+2]);
				}
				if(args[mode+1].equalsIgnoreCase("-z") || args[mode+1].equalsIgnoreCase("z-")){
					mob.motionZ -= parseDouble(args[mode+2]);
				}
				if(args[mode+1].equalsIgnoreCase("-look") && args[mode+1].equalsIgnoreCase("look-")){
					mob.motionX -= mob.getLookVec().xCoord;
					mob.motionZ -= mob.getLookVec().zCoord;
				}
				
				if(args[mode+1].equalsIgnoreCase("+x")){
					mob.motionX += parseDouble(args[mode+2]);
				}
				if(args[mode+1].equalsIgnoreCase("+y")){
					mob.motionY += parseDouble(args[mode+2]);
				}
				if(args[mode+1].equalsIgnoreCase("+z")){
					mob.motionZ += parseDouble(args[mode+2]);
				}
				if(args[mode+1].equalsIgnoreCase("+look")){
					mob.motionX += mob.getLookVec().xCoord;
					mob.motionZ += mob.getLookVec().zCoord;
				}
				
				if(args[mode+1].equalsIgnoreCase("x")){
					mob.motionX = parseDouble(args[mode+2]);
				}
				if(args[mode+1].equalsIgnoreCase("y")){
					mob.motionY = parseDouble(args[mode+2]);
				}
				if(args[mode+1].equalsIgnoreCase("z")){
					mob.motionZ = parseDouble(args[mode+2]);
				}
				if(args[mode+1].equalsIgnoreCase("+ook")){
					mob.motionX = mob.getLookVec().xCoord;
					mob.motionZ = mob.getLookVec().zCoord;
				}
			}
			
			if(args[mode].equalsIgnoreCase("AI") || args[mode].equalsIgnoreCase("지능")){
				Entity entity = getPlusEntity(server, sender, args[mode+2]);
				if(entity == null)
					throw new CommandException(args[mode+2]+" 엔티티가 없습니다");
				EntityAIBase ai = getAI(mob, args[mode+1], entity);
				System.out.println(mob);
				System.out.println(args[mode+1]+"---"+args[mode+2]);
				System.out.println(getAI(mob, args[mode+1], entity));
				System.out.println(entity);
				this.addAI(server, sender, args[0], ai);
			}
			if(args[mode].equalsIgnoreCase("velocity") || args[mode].equalsIgnoreCase("중력")){
				double x=parseDouble(args[mode+1]);
				double y=parseDouble(args[mode+2]);
				double z=parseDouble(args[mode+3]);
				mob.setVelocity(x, y, z);
			}

		}
	}

	@Override
	public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args,
			BlockPos pos) {
		
		if(args.length > 0)
			return getListOfStringsMatchingLastWord(args, "attackdamage,health,speed,name,dead,fire,item,jump,tp,noclip,potion,leashed,riding,move,look,ender,ai,velocity,damage,defence,maxhealth,knockback".split(","));
		return super.getTabCompletionOptions(server, sender, args, pos);
	}
	//공격력,방어력,넉백저항,체력,속도,최대체력,이름,죽음,불,아이템,점프,텔포,노클립,포션,대미지,데미지,줄,라이딩,이동,보기,엔더,지능,중력,
	public EntityAIBase getAI(EntityLiving e, String s, Object... obj) {
		if (s.equalsIgnoreCase("swim")) {
			return new EntityAISwimming(e);
		}
		if (s.equalsIgnoreCase("attack")) {
			return new EntityAINearestAttackableTarget((EntityCreature) e, ((Entity) obj[0]).getClass(), false);
		}
		if (s.equalsIgnoreCase("avoid")) {
			return new EntityAIAvoidEntity((EntityCreature) e, ((Entity) obj[0]).getClass(), 16, e.getAIMoveSpeed(), e.getAIMoveSpeed());
		}
		return null;
	}
	private DamageSource getDamageSource(String s){
		if(s.equalsIgnoreCase("~")){
			return DamageSource.generic;
		}
		if(s.equalsIgnoreCase("모루")){
			return DamageSource.anvil;
		}
		if(s.equalsIgnoreCase("선인장")){
			return DamageSource.cactus;
		}
		if(s.equalsIgnoreCase("드래곤")){
			return DamageSource.dragonBreath;
		}
		if(s.equalsIgnoreCase("익사")){
			return DamageSource.drown;
		}
		if(s.equalsIgnoreCase("낙사")){
			return DamageSource.fall;
		}
		if(s.equalsIgnoreCase("화상")){
			return DamageSource.hotFloor;
		}
		if(s.equalsIgnoreCase("마법")){
			return DamageSource.magic;
		}
		if(s.equalsIgnoreCase("불")){
			return DamageSource.inFire;
		}
		if(s.equalsIgnoreCase("벽")){
			return DamageSource.inWall;
		}
		if(s.equalsIgnoreCase("용암")){
			return DamageSource.lava;
		}
		if(s.equalsIgnoreCase("번개")){
			return DamageSource.lightningBolt;
		}
		if(s.equalsIgnoreCase("세계")){
			return DamageSource.outOfWorld;
		}
		if(s.equalsIgnoreCase("배고픔")){
			return DamageSource.starve;
		}
		if(s.equalsIgnoreCase("위더")){
			return DamageSource.wither;
		}
		return DamageSource.generic;

	}
	
	private void potion(EntityLivingBase mob, String[] args) throws NumberInvalidException{
		Potion potion;
		try
		{
			potion = Potion.getPotionById(parseInt(args[mode+1], 1));
		}
		catch (NumberInvalidException var11)
		{
			potion = Potion.getPotionFromResourceLocation(args[mode+1]);
		}
		int i = 600;
		int j = 30;
		int k = 0;
		if (args.length >= 3)
		{
			j = parseInt(args[mode+3], 0, 1000000);
			
			if (potion.isInstant())
			{
				i = j;
			}
			else
			{
				i = j * 20;
			}
		}
		else if (potion.isInstant())
		{
			i = 1;
		}
		
		if (args.length >= 4)
		{
			k = parseInt(args[4], 0, 255);
		}
		
		boolean flag = true;
		
		if (args.length >= 5 && "true".equalsIgnoreCase(args[5]))
		{
			flag = false;
		}
		
		if (j > 0)
		{
			PotionEffect potioneffect = new PotionEffect(potion, i, k, false, flag);
			mob.addPotionEffect(potioneffect);
		}
		else if (mob.isPotionActive(potion))
		{
			mob.removePotionEffect(potion);
		}
	
	}
	 private static void teleportEntityToCoordinates(Entity p_189863_0_, CoordinateArg p_189863_1_, CoordinateArg p_189863_2_, CoordinateArg p_189863_3_, CoordinateArg p_189863_4_, CoordinateArg p_189863_5_)
	 {
		 float f2 = (float)MathHelper.wrapDegrees(p_189863_4_.getResult());
		 float f3 = (float)MathHelper.wrapDegrees(p_189863_5_.getResult());
		 f3 = MathHelper.clamp_float(f3, -90.0F, 90.0F);
		 p_189863_0_.setLocationAndAngles(p_189863_1_.getResult(), p_189863_2_.getResult(), p_189863_3_.getResult(), f2, f3);
		 p_189863_0_.setRotationYawHead(f2);
		 if (!(p_189863_0_ instanceof EntityLivingBase) || !((EntityLivingBase)p_189863_0_).isElytraFlying())
		 {
			 p_189863_0_.motionY = 0.0D;
			 p_189863_0_.onGround = true;
		 }
	 }
}
