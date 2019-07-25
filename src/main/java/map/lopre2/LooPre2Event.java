package map.lopre2;

import cmplus.deb.DebAPI;
import map.lopre2.jump3.EntityBoatBuildBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
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
import org.lwjgl.input.Keyboard;
import map.lopre2.jump1.EntityLavaBlock;


public class LooPre2Event {

    @SubscribeEvent
    public void lavaEvent(LivingHurtEvent e) {
        if(e.getEntityLiving() instanceof EntityPlayer){
            System.out.println(e.getSource().getDamageType());
            if(e.getSource().isFireDamage() || e.getSource() == DamageSource.onFire || e.getSource() == DamageSource.inFire) {
                e.setCanceled(true);
                e.getEntityLiving().setFire(0);
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
    public void a(ServerChatEvent event) {
        String dis = event.getMessage();
        if (dis != null) {
            if (dis.startsWith("dis:")) {
                CommandJB.isDebMode = true;
                ItemCopy.setDistance(Integer.valueOf(dis.replace("dis:", "")));
            }
            if (dis.startsWith("yinte:")) {
                CommandJB.isDebMode = true;
                ItemCopy.setYinterval(Double.valueOf(dis.replace("yinte:", "")));
            }
            if (dis.startsWith("inte:")) {
                CommandJB.isDebMode = true;
                ItemCopy.setInterval(Double.valueOf(dis.replace("inte:", "")));
            }
            if (dis.startsWith("mox:")) {
                CommandJB.isDebMode = true;
                EntityBoatBuildBlock.moX = (Float.valueOf(dis.replace("mox:", "")));
            }
            if (dis.startsWith("moz:")) {
                CommandJB.isDebMode = true;
                EntityBoatBuildBlock.moZ = (Float.valueOf(dis.replace("moz:", "")));
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
        LoPre2.worldUnload();;


    }

    @SubscribeEvent
    public void event(MouseEvent event) {
        if (CommandJB.isDebMode) {
            if (event.getDwheel() == 120) {
                if (DebAPI.isKeyDown(Keyboard.KEY_LCONTROL)) {
                    EntityLavaBlock.ax -= 0.3;
                } else
                    EntityLavaBlock.ax -= 0.05;
            }
            if (event.getDwheel() == -120) {
                if (DebAPI.isKeyDown(Keyboard.KEY_LCONTROL)) {
                    EntityLavaBlock.ax += 0.3;
                } else
                    EntityLavaBlock.ax += 0.05;
            }
        }
    }
}
