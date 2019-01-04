package ruo.map.lopre2;

import net.minecraft.client.Minecraft;
import net.minecraft.command.ICommand;
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
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import org.lwjgl.input.Keyboard;
import ruo.cmplus.deb.DebAPI;
import ruo.map.lopre2.jump1.EntityLavaBlock;
import ruo.minigame.api.LoginEvent;
import ruo.minigame.api.WorldAPI;


public class LooPreClientEvent {
    @SubscribeEvent
    public void client(TickEvent.ClientTickEvent event){
        if(LoPre2.checkWorld() && Minecraft.getMinecraft().currentScreen == null && Keyboard.isKeyDown(Keyboard.KEY_R) && WorldAPI.getPlayer() != null && WorldAPI.getPlayer().getBedLocation() != null){
            WorldAPI.teleport(WorldAPI.getPlayerMP().getBedLocation());
            WorldAPI.getPlayerMP().heal(20);
            WorldAPI.getPlayer().fallDistance = 0;
            WorldAPI.getPlayer().getFoodStats().setFoodLevel(20);
        }
    }
}
