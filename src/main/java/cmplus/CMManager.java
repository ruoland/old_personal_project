package cmplus;

import cmplus.cm.v15.GuiCGameOver;
import cmplus.util.Sky;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import olib.api.WorldAPI;
import olib.effect.TickTask;
import olib.effect.TickRegister;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Type;

import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.util.HashMap;

public class CMManager implements Serializable {
    private static final ResourceLocation[] shaderResourceLocations = new ResourceLocation[]{
            new ResourceLocation("shaders/post/notch.json"), new ResourceLocation("shaders/post/fxaa.json"),
            new ResourceLocation("shaders/post/art.json"), new ResourceLocation("shaders/post/bumpy.json"),
            new ResourceLocation("shaders/post/blobs2.json"), new ResourceLocation("shaders/post/pencil.json"),
            new ResourceLocation("shaders/post/color_convolve.json"),
            new ResourceLocation("shaders/post/deconverge.json"), new ResourceLocation("shaders/post/flip.json"),
            new ResourceLocation("shaders/post/invert.json"), new ResourceLocation("shaders/post/ntsc.json"),
            new ResourceLocation("shaders/post/outline.json"), new ResourceLocation("shaders/post/phosphor.json"),
            new ResourceLocation("shaders/post/scan_pincushion.json"), new ResourceLocation("shaders/post/sobel.json"),
            new ResourceLocation("shaders/post/bits.json"), new ResourceLocation("shaders/post/desaturate.json"),
            new ResourceLocation("shaders/post/green.json"), new ResourceLocation("shaders/post/blur.json"),
            new ResourceLocation("shaders/post/wobble.json"), new ResourceLocation("shaders/post/blobs.json"),
            new ResourceLocation("shaders/post/antialias.json")};
    private static EnumFacing sleepFacing;
    public static boolean norain, mouse, RENDER_HAND = true;
    private static boolean moveLock, sit, sleep;
    private static GuiCGameOver gameover;

    public static HashMap<RenderGameOverlayEvent.ElementType, Boolean> uiMap = new HashMap<>();

    public static boolean isActiveUI(RenderGameOverlayEvent.ElementType elementType) {
        if(uiMap.isEmpty()){
            for(RenderGameOverlayEvent.ElementType elementType1 : RenderGameOverlayEvent.ElementType.values())
            {
                uiMap.put(elementType1, true);
            }
        }
        return uiMap.get(elementType);
    }

    public static void setHand(boolean on){
        RENDER_HAND = on;
    }
    public static boolean isRenderHand(){
        return RENDER_HAND;
    }
    public static void setUI(RenderGameOverlayEvent.ElementType elementType, boolean isOn) {
        uiMap.put(elementType, isOn);
    }

    public static void setRenderBlockLayer(boolean on){
        if (on)
                Sky.enableBlockLayer();
            else
                Sky.disableBlockLayer();
    }
    public static GuiCGameOver getGameOver() {
        return gameover;
    }

    public static void setGameOver(String messaage, ITextComponent causeOfDeath, boolean score, boolean title) {
        gameover = new GuiCGameOver(messaage, causeOfDeath, score, title);
    }

    public static void setGameOverNull() {
        gameover = null;
    }

    public static void setMouse(boolean mouse2) {
        mouse = mouse2;
    }

    public static void setSleep(boolean sleep2, EnumFacing fac) {
        sleep = sleep2;
        setSleepFacing(fac);
    }

    public static void setSit(boolean sleep2) {
        sit = sleep2;
    }

    public static void setMoveLock(boolean movelock) {
        moveLock = movelock;
    }

    public static void shader(final int shader) {
        TickRegister.register(new TickTask(Type.RENDER, 1, false) {
            @Override
            public void run(Type type) {
                Minecraft mc = Minecraft.getMinecraft();
                if (OpenGlHelper.shadersSupported) {
                    if (shader == 0) {
                        mc.entityRenderer.stopUseShader();
                        return;
                    }
                    mc.entityRenderer.loadShader(shaderResourceLocations[shader]);
                    mc.entityRenderer.getShaderGroup().createBindFramebuffers(mc.displayWidth, mc.displayHeight);
                }
            }
        });
    }

    public static boolean isMouse() {
        return mouse;
    }

    public static void setReach(float reach) {
        WorldAPI.getPlayerMP().interactionManager.setBlockReachDistance(reach);
    }

    public static boolean isSit() {
        return sit;
    }

    public static boolean isSleep() {
        return sleep;
    }

    public static boolean isMoveLock() {
        return moveLock;
    }

    public static EnumFacing getSleepFacing() {
        return sleepFacing;
    }

    public static void setSleepFacing(EnumFacing sleepFacing) {
        CMManager.sleepFacing = sleepFacing;
    }

}
