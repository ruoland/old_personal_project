package ruo.asdfrpg.event;

import atomicstryker.dynamiclights.client.DynamicLights;
import atomicstryker.dynamiclights.client.IDynamicLightSource;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import ruo.asdfrpg.AsdfRPG;
import ruo.asdfrpg.EntityLight;
import ruo.asdfrpg.skill.EntityAsdfBlock;
import ruo.asdfrpg.skill.PlayerSkill;
import ruo.asdfrpg.skill.SkillHelper;
import ruo.asdfrpg.skill.Skills;

public class SkillEvent {

    @SubscribeEvent
    public void village(PlayerInteractEvent.EntityInteract e) {
        if(SkillHelper.getPlayerSkill(e.getEntityPlayer()).isRegister(Skills.RIDING)) {
            e.getEntityPlayer().startRiding(e.getTarget());
            SkillHelper.getPlayerSkill(e.getEntityPlayer()).useSkill(Skills.RIDING, 0);
        }
    }
    @SubscribeEvent
    public void village(PlayerInteractEvent.RightClickBlock e) {
        Block block = e.getWorld().getBlockState(e.getPos()).getBlock();
        if (Blocks.BEACON == block) {
            System.out.println("스킬 사용됨" + e.getEntityPlayer().getUniqueID());
            SkillHelper.getPlayerSkill(e.getEntityPlayer()).useSkill(Skills.VILLAGE_RETURN, 0);
        }
    }

    //        System.out.println(e.getEntityLiving()+" - "+e.getSource().getEntity()+" - "+e.getSource()+e.getSource().getSourceOfDamage());
    //EntityCreeper['Creeper'/11621, l='TEST', x=1170.59, y=4.00, z=196.16] - EntityPlayerMP['Player508'/111, l='TEST', x=1168.61, y=4.00, z=193.21] - net.minecraft.util.EntityDamageSource@33d163d6EntityPlayerMP['Player508'/111, l='TEST', x=1168.61, y=4.00, z=193.21]
    @SubscribeEvent
    public void playerTick(LivingAttackEvent e) {
        if (e.getSource().getEntity() instanceof EntityPlayer) {
            System.out.println("플레이어가 공격함");
            EntityPlayer player = (EntityPlayer) e.getSource().getEntity();
            PlayerSkill playerSkill = SkillHelper.getPlayerSkill(player.getUniqueID());
            if (playerSkill.isRegister(Skills.AUTO_ATTACK)) {
                EntityAsdfBlock asdfBlock = new EntityAsdfBlock(player.worldObj);
                asdfBlock.setPosition(player.getPosition().add(0, 3, 0));
                if (e.getEntityLiving().isServerWorld())
                    player.worldObj.spawnEntityInWorld(asdfBlock);
                asdfBlock.setTarget(e.getEntityLiving());
                asdfBlock.player = player;
            }
        }
    }

    @SubscribeEvent
    public void lightSkill(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof EntityLight) {
            EntityLight arrow = (EntityLight) event.getEntity();
            DynamicLights.addLightSource(new EntityLightAdapter(arrow, SkillHelper.getPlayerSkill().getSkill(Skills.LIGHT).getLevel()));
        }
    }

    @SubscribeEvent
    public void flySkill(TickEvent.PlayerTickEvent e) {
        if (!e.player.isCreative()) {
            if (e.player.isPotionActive(AsdfRPG.flyPotion) && e.player.capabilities.isFlying) {
                e.player.capabilities.isFlying = true;
                e.player.sendPlayerAbilities();
            } else {
                e.player.capabilities.isFlying = false;
                e.player.sendPlayerAbilities();
            }
            if (e.player.isPotionActive(AsdfRPG.ironBodyPotion)) {
                e.player.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(100000);
            } else {
                e.player.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(0);

            }
        }

    }
    private class EntityLightAdapter implements IDynamicLightSource {
        private EntityLight entity;
        private int level;

        public EntityLightAdapter(EntityLight light, int level) {
            entity = light;
            this.level = level;
            System.out.println(level);
        }

        @Override
        public Entity getAttachmentEntity() {
            return entity;
        }

        @Override
        public int getLightLevel() {
            return 15;
        }
    }
}
