package map.lopre2.dev;

import cmplus.deb.DebAPI;
import map.lopre2.CommandJB;
import map.lopre2.ItemSpanner;
import map.lopre2.LoPre2;
import map.lopre2.jump3.EntityBoatBuildBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
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
import org.lwjgl.input.Keyboard;
import map.lopre2.jump1.EntityLavaBlock;

import java.lang.reflect.Field;


public class IngameEvent {


    @SubscribeEvent
    public void lavaEvent(TickEvent.PlayerTickEvent e) {
        e.player.extinguish();
        float m = 0.3F;
        AxisAlignedBB aabbPlayer = e.player.getEntityBoundingBox();
        AxisAlignedBB aabb = new AxisAlignedBB(aabbPlayer.minX - m, aabbPlayer.minY + m, aabbPlayer.minZ - m, aabbPlayer.maxX - m, aabbPlayer.maxY, aabbPlayer.maxZ - m);
        System.out.println("-------------------"+aabbPlayer.minY+" - "+ aabbPlayer.maxY +" - "+aabb.minY + " - "+aabb.maxY);
        System.out.println(e.player.isImmuneToFire()+ " - "+e.player.worldObj.isFlammableWithin(aabb) + " - "+ e.player.isCollided + " - "+ e.player.isCollidedVertically + " - " + e.player.onGround);
        if (e.player.isImmuneToFire() && e.player.worldObj.isFlammableWithin(aabb) && !e.player.onGround) {
            e.player.attackEntityFrom(DamageSource.lava, 4);
        }
        if (!e.player.isImmuneToFire()) {
            Class cla = EntityPlayer.class.getSuperclass().getSuperclass();
            try {
                Field field = cla.getDeclaredField("isImmuneToFire");
                field.setAccessible(true);
                field.set(e.player, true);
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
