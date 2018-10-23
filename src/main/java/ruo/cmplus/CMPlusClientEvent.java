package ruo.cmplus;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.command.CommandException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.DimensionType;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.event.EntityViewRenderEvent.RenderFogEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import org.lwjgl.input.Keyboard;
import ruo.cmplus.camera.Camera;
import ruo.cmplus.cm.v18.function.FunctionIF;
import ruo.cmplus.cm.v18.function.VAR;
import ruo.cmplus.deb.DebAPI;
import ruo.cmplus.util.*;
import ruo.minigame.api.WorldAPI;

import java.lang.reflect.Field;

public class CMPlusClientEvent {
    private static final String[] uiList = "ALL,HELMET,PORTAL,CROSSHAIRS,BOSSHEALTH,ARMOR,HEALTH,FOOD,AIR,HOTBAR,EXPERIENCE,HEALTHMOUNT,JUMPBAR,CHAT,PLAYER_LIST,DEBUG"
            .split(",");
    private Camera cm = Camera.getCamera();
    private Minecraft mc = Minecraft.getMinecraft();
    private double prevDistance;

    @SubscribeEvent
    public void event(RenderFogEvent e) {
        if (Sky.isFogOn()) {
            if (Sky.getFogDistance() == -1) {
                Sky.fogDistance(5.0F + ((Minecraft.getMinecraft().gameSettings.renderDistanceChunks * 16) - 5.0F)
                        * (1.0F - (float) 0 / 20.0F));
            }
            if (Sky.getFogDistance() != -1) {
                GlStateManager.enableFog();
                GlStateManager.setFogStart(Sky.getFogDistance());
                GlStateManager.setFogEnd(Sky.getFogDistance() + 1);
            }
        }
    }

    @SubscribeEvent
    public void event(DrawBlockHighlightEvent e) {
        if (!Sky.isLayer()) {
            e.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void login(RenderHandEvent event) {
        event.setCanceled(!CMManager.isActiveUI("HAND"));
    }

    @SubscribeEvent
    public void mouse(MouseEvent event) {
        if (Minecraft.getMinecraft().currentScreen == null) {
            event.setCanceled(CMManager.isMouse());
        }
    }

    @SubscribeEvent
    public void bgmStop(PlaySoundEvent e) {
        if (e.getName().contains("music.")) {
            e.getManager().stopSound(e.getSound());
            e.getManager().stopSound(e.getResultSound());
            Minecraft.getMinecraft().getSoundHandler().stopSound(e.getSound());
            Minecraft.getMinecraft().getSoundHandler().stopSound(e.getResultSound());
        }
    }

    @SubscribeEvent
    public void playerTick(PlayerTickEvent e) {
        if (Keyboard.isKeyDown(Keyboard.KEY_INSERT)) {
            Entity entity = Minecraft.getMinecraft().objectMouseOver.entityHit;
            if (entity != null) {
                System.out.println(entity.getCustomNameTag() + entity.getPosition());
            } else if (WorldAPI.getLookBlock() != null) {
                System.out.println(WorldAPI.getLookBlock().getX() + "," + WorldAPI.getLookBlock().getY() + ","
                        + WorldAPI.getLookBlock().getZ());
                System.out.println(WorldAPI.getBlock(WorldAPI.getLookBlock()).getLocalizedName());
            }
        }
    }

    @SubscribeEvent
    public void renderUI(RenderGameOverlayEvent.Pre event) {
        if (event.getType() == ElementType.ALL)
            for (String s : uiList) {
                if (event.getType() == ElementType.valueOf(s)) {
                    if (!CMManager.isActiveUI(s)) {
                        event.setCanceled(true);
                    }
                }
            }
    }

    @SubscribeEvent
    public void event(PlayerRespawnEvent event) {
        if (CMManager.getGameOver() != null) {
            Minecraft.getMinecraft().currentScreen = null;
        }
    }

    @SubscribeEvent
    public void event(GuiOpenEvent event) {
        if (CMManager.getGameOver() != null && event.getGui() instanceof GuiGameOver) {
            event.setGui(CMManager.getGameOver());
            CMManager.setGameOverNull();
        }
    }

}
