package cmplus;

import cmplus.camera.Camera;
import olib.api.EntityAPI;
import olib.api.WorldAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class CMPlusCameraEvent {
    private Minecraft mc = Minecraft.getMinecraft();
    public static double x,y,z;
    @SubscribeEvent
    public void event2(EntityViewRenderEvent.FOVModifier e) {
        e.setFOV(Camera.getZoom());
    }
    @SubscribeEvent
    public void event222(EntityViewRenderEvent.CameraSetup e) {
        if (x != 0 && y != 0 && z != 0) {
            GL11.glTranslated(mc.thePlayer.posX-x, 0, 0);
            GL11.glTranslated(0, mc.thePlayer.posY-y, 0);
            GL11.glTranslated(0, 0, mc.thePlayer.posZ-z);
        }
    }
    @SubscribeEvent
    public void event2(EntityViewRenderEvent.CameraSetup e) {
        GL11.glTranslated(Camera.traX, 0, 0);
        GL11.glTranslated(0, Camera.traY, 0);
        GL11.glTranslated(0, 0, Camera.traZ);
    }

    @SubscribeEvent
    public void event(EntityViewRenderEvent.CameraSetup e) {
        GL11.glRotated(Camera.rotateX, 1, 0, 0);
        GL11.glRotated(Camera.rotateY, 0, 1, 0);
        GL11.glRotated(Camera.rotateZ, 0, 0, 1);

        if (Camera.lockCamera) {
            if(Camera.lockCameraPitch == 0 && Camera.lockCameraYaw == 0){
                Camera.lockCameraPitch = e.getPitch();
                Camera.lockCameraYaw = e.getYaw();
            }
            e.setPitch(Camera.lockCameraPitch);
            e.setYaw(Camera.lockCameraYaw);
            EntityLivingBase entitylivingbase = Minecraft.getMinecraft().thePlayer;
            entitylivingbase.rotationPitch = entitylivingbase.prevRotationPitch = Camera.lockPlayerPitch;
            entitylivingbase.rotationYaw = entitylivingbase.prevRotationYaw = entitylivingbase.rotationYawHead = entitylivingbase.renderYawOffset = Camera.lockPlayerYaw;
        }
        if (Camera.yp) {
            e.setRoll(0);
            e.setYaw(0);
            e.setPitch(0);
        }
    }

    @SubscribeEvent
    public void event(MouseEvent e) {
        if (Camera.freeCam) {
            Minecraft mc = Minecraft.getMinecraft();
            GameSettings gameSettings = mc.gameSettings;

        }
    }

    @SubscribeEvent
    public void event(PlayerTickEvent e) {
        if (Camera.freeCam) {
            Minecraft mc = Minecraft.getMinecraft();
            GameSettings gameSettings = mc.gameSettings;
            if (gameSettings.keyBindJump.isKeyDown()) {
                Camera.moveAdd(0, 0.1, 0);
            }
            if (gameSettings.keyBindSneak.isKeyDown()) {
                Camera.moveAdd(0, -0.1, 0);
            }
            if (gameSettings.keyBindLeft.isKeyDown()) {
                Camera.moveAdd(0.1, 0, 0);
            }
            if (gameSettings.keyBindRight.isKeyDown()) {
                Camera.moveAdd(-0.1, 0, 0);
            }
            if (gameSettings.keyBindForward.isKeyDown()) {
                Camera.moveAdd(0, 0, 0.1);
            }
            if (gameSettings.keyBindBack.isKeyDown()) {
                Camera.moveAdd(0, 0, -0.1);
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_TAB) && Keyboard.isKeyDown(Keyboard.KEY_J)) {
                Camera.rotateZ -= 1;
                return;
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_J)) {
                Camera.rotateZ += 1;
                return;
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_TAB) && Keyboard.isKeyDown(Keyboard.KEY_K)) {
                Camera.rotateY -= 1;
                return;
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_I)) {
                Camera.rotateY += 1;
                return;
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_TAB) && Keyboard.isKeyDown(Keyboard.KEY_L)) {
                Camera.rotateX -= 1;
                return;
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_L)) {
                Camera.rotateX += 1;
                return;
            }
        }

    }

    @SubscribeEvent
    public void renderUI(RenderGameOverlayEvent.Post event) {
        if (event.getType() == ElementType.ALL && Camera.isDebug && !Minecraft.getMinecraft().gameSettings.showDebugInfo) {
            Minecraft.getMinecraft().fontRendererObj.drawString("카메라 ROTATE X" + Camera.rotateX, 0, 20,
                    0xFFFFFF);
            Minecraft.getMinecraft().fontRendererObj.drawString("카메라 ROTATE Y" + Camera.rotateY, 0, 30,
                    0xFFFFFF);
            Minecraft.getMinecraft().fontRendererObj.drawString("카메라 ROTATE Z" + Camera.rotateZ, 0, 40,
                    0xFFFFFF);
            Minecraft.getMinecraft().fontRendererObj.drawString("카메라 TRA X" + Camera.traX, 0, 50, 0xFFFFFF);
            Minecraft.getMinecraft().fontRendererObj.drawString("카메라 TRA Y" + Camera.traY, 0, 60, 0xFFFFFF);
            Minecraft.getMinecraft().fontRendererObj.drawString("카메라 TRA Z" + Camera.traZ, 0, 70, 0xFFFFFF);
            Minecraft.getMinecraft().fontRendererObj.drawString("카메라 YAW" + Camera.lockPlayerYaw, 0, 80, 0xFFFFFF);
            Minecraft.getMinecraft().fontRendererObj.drawString("카메라 PITCH" + Camera.lockPlayerPitch, 0, 90,
                    0xFFFFFF);
            Minecraft.getMinecraft().fontRendererObj.drawString("카메라 락" + Camera.lockCamera, 0, 100,
                    0xFFFFFF);
            Minecraft.getMinecraft().fontRendererObj.drawString("카메라 YP" + Camera.yp, 0, 110, 0xFFFFFF);
            Minecraft.getMinecraft().fontRendererObj.drawString("플레이어 YAW" + WorldAPI.getPlayer().rotationYaw, 0, 120,
                    0xFFFFFF);
            Minecraft.getMinecraft().fontRendererObj.drawString("플레이어 PITCH" + WorldAPI.getPlayer().rotationPitch, 0,
                    130, 0xFFFFFF);
            Minecraft.getMinecraft().fontRendererObj.drawString("보고있는 X:" + EntityAPI.lookX(WorldAPI.getPlayer(), 1)+" Z:"+EntityAPI.lookZ(WorldAPI.getPlayer(), 1), 0,
                    140, 0xFFFFFF);
        }
    }
}
