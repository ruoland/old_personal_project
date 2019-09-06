package cmplus;

import cmplus.camera.Camera;
import cmplus.util.CloudRenderer;
import cmplus.util.Sky;
import cmplus.util.SkyRenderer;
import cmplus.util.WorldProvider10;
import olib.api.WorldAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.event.EntityViewRenderEvent.RenderFogEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.lwjgl.input.Keyboard;

import java.lang.reflect.Field;

public class CMPlusClientEvent {
    private static final String[] uiList = "ALL,HELMET,PORTAL,CROSSHAIRS,BOSSHEALTH,ARMOR,HEALTH,FOOD,AIR,HOTBAR,EXPERIENCE,HEALTHMOUNT,JUMPBAR,CHAT,PLAYER_LIST,DEBUG"
            .split(",");
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

    //@SubscribeEvent
    public void event(WorldEvent.Load e) {
        if (FMLCommonHandler.instance().getSide() == Side.CLIENT && e.getWorld().provider.getDimensionType() == DimensionType.OVERWORLD && !(e.getWorld().provider instanceof WorldProvider10)) {
            try {
                WorldProvider10 pro = new WorldProvider10();
                pro.registerWorld(e.getWorld());
                for (Field f : e.getWorld().provider.getClass().getDeclaredFields()) {
                    Field prof;
                    try {
                        prof = pro.getClass().getDeclaredField(f.getName());
                    } catch (Exception e2) {
                        continue;
                    }
                    prof.setAccessible(true);
                    f.setAccessible(true);
                    //prof.set(pro, f.get(e.getWorld().provider));
                }
                pro.setSpawnPoint(e.getWorld().provider.getSpawnPoint());

                for (Field f : World.class.getDeclaredFields()) {
                    if (f.getName().equals("provider")) {
                        f.setAccessible(true);
                        f.set(e.getWorld(), pro);
                        e.getWorld().provider.setCloudRenderer(new CloudRenderer());
                        e.getWorld().provider.setSkyRenderer(new SkyRenderer());
                        Sky.pro = (WorldProvider10) e.getWorld().provider;

                        break;
                    }
                }

            } catch (Exception e2) {
                e2.printStackTrace();
            }
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
