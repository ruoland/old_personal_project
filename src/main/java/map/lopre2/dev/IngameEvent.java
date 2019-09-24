package map.lopre2.dev;

import cmplus.deb.DebAPI;
import map.lopre2.CommandJB;
import map.lopre2.ItemSpanner;
import map.lopre2.LoPre2;
import map.lopre2.jump1.EntityLoopFallingBlock;
import map.lopre2.jump3.EntityBoatBuildBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.world.WorldEvent;
import olib.api.LoginEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import olib.api.WorldAPI;
import olib.effect.TickRegister;
import olib.effect.TickTask;
import org.lwjgl.input.Keyboard;
import map.lopre2.jump1.EntityLavaBlock;

import java.lang.reflect.Field;


public class IngameEvent {


    @SubscribeEvent
    public void inLavaEvent(CommandEvent e) {
        if(e.getCommand().getCommandName().equalsIgnoreCase("spawnpoint")){
            e.setCanceled(true);
            System.out.println(e.getSender().getPosition().getX()+" "+e.getSender().getPosition().getY()+" "+e.getSender().getPosition().getZ());
            WorldAPI.getPlayer().setSpawnPoint(e.getSender().getPosition().add(0,2.5,0),true);
        }
    }
    @SubscribeEvent
    public void inLavaEvent(TickEvent.PlayerTickEvent e) {
        e.player.extinguish();
        e.player.getFoodStats().setFoodLevel(20);

        float m = 0.2F;
        if (e.phase == TickEvent.Phase.START) {
            AxisAlignedBB aabbPlayer = e.player.getEntityBoundingBox();
            AxisAlignedBB aabb = new AxisAlignedBB(aabbPlayer.minX - m, aabbPlayer.minY + 0.2F, aabbPlayer.minZ - m, aabbPlayer.maxX - m, aabbPlayer.maxY, aabbPlayer.maxZ - m);
            if (e.player.isImmuneToFire() && e.player.worldObj.isFlammableWithin(aabb) && !e.player.onGround) {
                e.player.attackEntityFrom(DamageSource.lava, 4);
            }
        }
        if (!e.player.isImmuneToFire()) {
            int i = 1;
            Class cla = EntityPlayer.class.getSuperclass().getSuperclass();
            try {
            for(Field field : cla.getDeclaredFields()){

                if(field.getType() == boolean.class){
                    if(!field.isAccessible()){
                        System.out.println("@@@@@@@@@@@@ "+field.getName());
                        if(i == 14)
                        {
                            field.setAccessible(true);
                            field.set(e.player, true);
                            System.out.println(field.getName());
                            break;
                        }
                        i++;

                    }
                }
            }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    @SubscribeEvent
    public void playerTick(TickEvent.PlayerTickEvent e) {
        if (!CommandJB.isDebMode && e.side == Side.SERVER && e.phase == TickEvent.Phase.END) {
            for (ItemStack stack : e.player.inventory.mainInventory) {
                if (stack != null && stack.getItem() instanceof ItemSpanner) {
                    CommandJB.isDebMode = true;
                }
            }
        }
    }


    @SubscribeEvent
    public void event(LoginEvent event) {
        if (LoPre2.checkWorld()) {
            LoPre2.worldload();
            if (Minecraft.getMinecraft().gameSettings.limitFramerate > 60) {
                Minecraft.getMinecraft().gameSettings.limitFramerate = 60;
            }
        }
    }

    @SubscribeEvent
    public void event(WorldEvent.Unload event) {
        LoPre2.worldUnload();
    }

}
