package ruo.map.lopre2;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.command.ICommand;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import ruo.cmplus.deb.DebAPI;
import ruo.map.lopre2.jump1.EntityLavaBlock;
import ruo.minigame.MiniGame;
import ruo.minigame.api.LoginEvent;
import ruo.minigame.api.WorldAPI;
import ruo.minigame.effect.Move;
import ruo.minigame.minigame.scroll.EntityJumpCreeper;


public class LooPreThreeEvent {
    private static String pressUsername;
    private static int inputDelay;
    @SubscribeEvent
    public  void minecart(PlayerTickEvent event) {
        if (event.player.getLowestRidingEntity() instanceof EntityMinecart) {
            inputDelay--;
            EntityMinecart entityMinecart = (EntityMinecart) event.player.getRidingEntity();
            if(event.player.getLowestRidingEntity().motionY == 0)
                entityMinecart.setCanUseRail(true);
            if (event.side == Side.CLIENT) {
                if (Minecraft.getMinecraft().gameSettings.keyBindJump.isPressed()) {
                    pressUsername = event.player.getName();
                    System.out.println("키 클릭");
                    inputDelay = 20;
                }
                else if(inputDelay < 0){
                    pressUsername = null;
                }
            }
            else{
                if(pressUsername != null && inputDelay > 0 && entityMinecart.motionY == 0) {
                    entityMinecart.setCanUseRail(false);

                    System.out.println(MiniGame.scroll.getForwardXZ());
                    entityMinecart.moveEntity(0,1, MiniGame.scroll.getForwardXZ() * 5);
                    System.out.println(Minecraft.getMinecraft().thePlayer.rotationYaw);
                    inputDelay = 0;
                }
            }
        }
    }
}
