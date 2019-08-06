package ruo.yout;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityDragonPart;
import net.minecraft.entity.boss.dragon.phase.PhaseList;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import olib.api.WorldAPI;

import java.util.ArrayList;

//모드 전반적인 것들에 관한 이벤트
//포션 기능 등등
public class MoJaeEvent {
    public static double attackDelay = -1;
    public static ArrayList<String> lockList = new ArrayList<>();
    @SubscribeEvent
    public void event(EntityJoinWorldEvent event) {
        if(event.getEntity() instanceof EntityArrow){
            EntityArrow arrow = (EntityArrow) event.getEntity();
            NBTTagCompound tagCompound = new NBTTagCompound();
            arrow.writeEntityToNBT(tagCompound);
            tagCompound.setShort("life", (short) 1000);
            arrow.readEntityFromNBT(tagCompound);
        }
        if(event.getEntity() instanceof EntityItem){
            EntityItem item = (EntityItem) event.getEntity();
            if(item.getThrower() != null && !(item.getThrower().startsWith("Player") || item.getThrower().equalsIgnoreCase("oprond"))){
                item.setDead();
            }
        }
    }

    @SubscribeEvent
    public void event(LivingExperienceDropEvent event) {
        if(Mojae.morespawn && Mojae.dog_pan)
            event.setCanceled(true);

    }

    @SubscribeEvent
    public void event(LivingDropsEvent event) {
        if(Mojae.morespawn && Mojae.dog_pan)
            event.setCanceled(true);
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
