package cmplus.cm.beta.customnpc;

import cmplus.cm.v18.function.Function;
import oneline.api.WorldAPI;
import oneline.map.EntityDefaultNPC;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityCMNPC extends EntityDefaultNPC{
	public Function functionNPC;
	public Function npcCollision, interaction, attackFrom, fall, knockback, death, onkill;

	public EntityCMNPC(World worldIn) {
		super(worldIn);
		functionNPC = Function.addFunction("Entity", this.getCustomNameTag(), "이름", this.getName());
		npcCollision = Function.addFunction("Entity", getCustomNameTag()+"-충돌", "이름", this.getName());
		npcCollision.replace = functionNPC.replace;
		interaction = Function.addFunction("Entity", getCustomNameTag()+"-상호작용", "이름", this.getName());
		interaction.replace = functionNPC.replace;
		attackFrom = Function.addFunction("Entity", getCustomNameTag()+"-공격받음", "이름", this.getName());
		attackFrom.replace = functionNPC.replace;
		fall = Function.addFunction("Entity", getCustomNameTag()+"-추락", "이름락", this.getName());
		fall.replace = functionNPC.replace;
		knockback = Function.addFunction("Entity", getCustomNameTag()+"-넉백", "이름", this.getName());
		knockback.replace = functionNPC.replace;
		death = Function.addFunction("Entity", getCustomNameTag()+"-죽음", "이름", this.getName());
		death.replace = functionNPC.replace;
		onkill = Function.addFunction("Entity", getCustomNameTag()+"-엔티티죽임", "이름", this.getName());
		onkill.replace = functionNPC.replace;
	}

	@Override
	public void applyEntityCollision(Entity entityIn) {
		super.applyEntityCollision(entityIn);
		npcCollision.addOne("엔티티이름", entityIn.getName());
		npcCollision.runScript();
	}
	
	@Override
	public EnumActionResult applyPlayerInteraction(EntityPlayer player, Vec3d vec, ItemStack stack, EnumHand hand) {
		interaction.addOne("엔티티이름", player.getName(), "아이템", WorldAPI.getStackName(stack));
		interaction.runScript();
		return super.applyPlayerInteraction(player, vec, stack, hand);
	}
	
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		attackFrom.addOne("엔티티이름",WorldAPI.getEntityName(source.getEntity()),
				"타입", source.getDamageType(), "데미지", ""+amount);
		attackFrom.runScript();
		return super.attackEntityFrom(source, amount);
	}
	@Override
	public void fall(float distance, float damageMultiplier) {
		super.fall(distance, damageMultiplier);
		fall.addOne("높이",""+distance);
		fall.runScript("추락");
	}
	
	@Override
	public void knockBack(Entity entityIn, float strenght, double xRatio, double zRatio) {
		super.knockBack(entityIn, strenght, xRatio, zRatio);
		knockback.addOne("엔티티이름",entityIn.getCustomNameTag());
		knockback.runScript();
	}
	
	@Override
	public void onDeath(DamageSource cause) {
		super.onDeath(cause);
		death.addOne("사유", cause.damageType);
		death.runScript();
	}
	@Override
	public void onKillEntity(EntityLivingBase entityLivingIn) {
		super.onKillEntity(entityLivingIn);
		onkill.addOne("엔티티이름",entityLivingIn.getCustomNameTag());
		onkill.runScript();
	}
}
