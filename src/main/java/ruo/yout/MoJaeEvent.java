package ruo.yout;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityDragonPart;
import net.minecraft.entity.boss.dragon.phase.PhaseList;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import oneline.api.LoginEvent;

import java.util.ArrayList;

public class MoJaeEvent {
    public static double attackDelay = -1;
    public static ArrayList<String> lockList = new ArrayList<>();
    @SubscribeEvent
    public void event(LivingHurtEvent event) {
        if(event.getEntityLiving() instanceof EntityPlayer && event.getSource().damageType.equalsIgnoreCase("outOfWorld")) {
            event.setCanceled(true);
            event.getEntityLiving().motionY -= 1000;
        }
    }
    @SubscribeEvent
    public void event(LivingAttackEvent event) {
        String name = event.getEntityLiving().getCustomNameTag();
        if (event.getEntityLiving().isPotionActive(Mojae.godPotion) || name.startsWith("무적 상태")) {
            event.setCanceled(true);
            System.out.println(event.getEntityLiving() + "가 무적이라 공격 취소됨");
        }
    }

    @SubscribeEvent
    public void joinWorld(EntityJoinWorldEvent event) {
        for (String str : lockList) {//소환된 엔티티가 잠금 대상인지 아닌지
            if (event.getEntity() instanceof EntityLivingBase) {
                if (str.contains("all") || EntityList.isStringEntityName(event.getEntity(), str)) {
                    event.getEntity().setCustomNameTag("잠금");
                }
            }
        }
    }

    @SubscribeEvent
    public void event(LivingEvent.LivingUpdateEvent event) {
        String name = event.getEntityLiving().getCustomNameTag();
        EntityLivingBase livingBase = event.getEntityLiving();
        if (attackDelay != -1 && livingBase instanceof EntityMob) {
            livingBase.hurtResistantTime = (int) attackDelay;
        }
        if (name.startsWith("잠금해제")) {
            livingBase.removePotionEffect(Mojae.lockPotion);
            livingBase.setCustomNameTag("");
        }
        for (String str : lockList) {
            if (EntityList.isStringEntityName(event.getEntityLiving(), str))
                event.getEntityLiving().setCustomNameTag("잠금");
        }
        if (name.startsWith("잠금")) {
            livingBase.setVelocity(0, 0, 0);
            NBTTagCompound tag = livingBase.getEntityData();

            if (livingBase instanceof EntityDragon) {
                EntityDragon dragon = (EntityDragon) livingBase;
                dragon.getPhaseManager().setPhase(PhaseList.SITTING_FLAMING);
                for (EntityDragonPart dragonPart : dragon.dragonPartArray) {
                    dragonPart.setVelocity(0, 0, 0);
                    dragonPart.setPosition(tag.getDouble("LPX"), tag.getDouble("LPY")
                            , tag.getDouble("LPZ"));
                }
            }
            if (tag.getDouble("LPX") == 0 || tag.getDouble("LPY") == 0 || tag.getDouble("LPZ") == 0) {
                return;
            }
            livingBase.setPosition(tag.getDouble("LPX"), tag.getDouble("LPY")
                    , tag.getDouble("LPZ"));
        }
    }

}
