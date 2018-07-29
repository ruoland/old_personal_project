package ruo.halloween;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings.Options;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import ruo.halloween.miniween.EntityAttackMiniWeen;
import ruo.halloween.miniween.EntityMiniWeen;
import ruo.minigame.MiniGame;
import ruo.minigame.api.EntityAPI;
import ruo.minigame.api.WorldAPI;
import ruo.minigame.fakeplayer.EntityFakePlayer;

import java.util.List;

public class WeenEvent {
    public static EntityWeen ween;

    @SubscribeEvent
    public void right(LivingFallEvent e) {
        if (e.getEntityLiving() instanceof EntityPlayer && MiniGame.elytra.isStart()) {
            e.setDistance(0);
        }
    }

    @SubscribeEvent
    public void r(PlayerInteractEvent event) {
    }

    @SubscribeEvent
    public void right(ServerChatEvent e) {
        if (e.getMessage().equals("슈팅게임")) {
            MiniGame.elytra.start("0");
        }
        if (e.getMessage().indexOf("나와") != -1 && e.getMessage().indexOf("호박") != -1) {
            if (e.getPlayer().worldObj.getWorldInfo().getTerrainType() != WorldType.FLAT) {
                WorldAPI.addMessage("보스는 평지맵에서만 소환할 수 있습니다.");
                return;
            } else {
                WorldAPI.addMessage("호박파이와 무한 활이 지급되었습니다.");
            }
            if (e.getPlayer().worldObj.getWorldInfo().getTerrainType() == WorldType.FLAT) {
                EntityWeen ween = new EntityWeen(e.getPlayer().worldObj);
                ween.setPosition(EntityAPI.lookPlayerX(15), e.getPlayer().posY, EntityAPI.lookPlayerZ(15));
                e.getPlayer().worldObj.spawnEntityInWorld(ween);
                e.getPlayer().setSpawnPoint(e.getPlayer().getPosition(), true);
                ween.startTime = System.currentTimeMillis();//스폰에그로 소환해햐 onInitialSpawn 메서드가 실행이 되서 여기서 스타트 타임을 설정함

                //WorldAPI.command("/gamma set 100");
                //WorldAPI.command("/distance set 7");
                //WorldAPI.command("/gamerule doDaylightCycle false");
                //WorldAPI.command("/sound blocks 10");
                e.getPlayer().worldObj.setWorldTime(12000);
                e.getPlayer().worldObj.getGameRules().setOrCreateGameRule("keepInventory", "true");
                e.getPlayer().worldObj.getGameRules().setOrCreateGameRule("doDaylightCycle", "false");
                Minecraft.getMinecraft().gameSettings.renderDistanceChunks = 7;
                Minecraft.getMinecraft().gameSettings.setOptionFloatValue(Options.GAMMA, 1F);
                Minecraft.getMinecraft().gameSettings.setSoundLevel(SoundCategory.BLOCKS, 0.10F);

                //WorldAPI.addMessage("호박은 활 공격만 먹히고, 또 기절한 상태에서만 공격이 들어가니 조심하세요.");

                ItemStack itemstack = new ItemStack(Items.BOW, 1);
                itemstack.addEnchantment(Enchantment.getEnchantmentByID(51), 1);
                e.getPlayer().inventory.addItemStackToInventory(itemstack);
                e.getPlayer().inventory.addItemStackToInventory(new ItemStack(Items.ARROW));
                e.getPlayer().inventory.addItemStackToInventory(new ItemStack(Items.PUMPKIN_PIE, 3));
                e.getPlayer().setHealth(e.getPlayer().getMaxHealth());
                e.getPlayer().getFoodStats().setFoodLevel(20);
                this.ween = ween;
            }
        }
    }

    @SubscribeEvent
    public void in(PlayerLoggedOutEvent e) {
        isWeen = false;
    }

    boolean isWeen;

    @SubscribeEvent
    public void in(LivingUpdateEvent e) {
        if (!isWeen) {
            for (Entity ent : e.getEntityLiving().worldObj.loadedEntityList) {
                if (ent instanceof EntityWeen || ent instanceof EntityMiniWeen || ent instanceof EntityBigWeen
                        || ent instanceof EntityPeaceWitch) {
                    isWeen = true;
                    System.out.println("윈이 존재해 true로 설정됨");
                    return;
                }
            }
        }
        if (isWeen && e.getEntityLiving() instanceof EntityMob || e.getEntityLiving() instanceof EntitySlime) {
            if (e.getEntityLiving() instanceof EntityPlayerWeen || e.getEntityLiving() instanceof EntityBlock || e.getEntityLiving() instanceof EntityWeen
                    || e.getEntityLiving() instanceof EntityMiniWeen || e.getEntityLiving() instanceof EntityBigWeen
                    || e.getEntityLiving() instanceof EntityPeaceWitch || e.getEntityLiving() instanceof EntityFakePlayer) {
                return;
            }
            e.getEntityLiving().worldObj.setRainStrength(0);

            e.getEntityLiving().setDead();
        }
    }

}
