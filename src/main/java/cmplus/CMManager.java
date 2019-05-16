package cmplus;

import cmplus.cm.v15.GuiCGameOver;
import cmplus.util.Sky;
import olib.api.WorldAPI;
import olib.effect.AbstractTick;
import olib.effect.TickRegister;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Type;

import java.io.Serializable;

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
    public static boolean norain, mouse, HAND = true, ALL = true, HELMET = true, PORTAL = true, CROSSHAIRS = true, BOSSHEALTH = true, ARMOR = true, HEALTH = true, FOOD = true, AIR = true, HOTBAR = true, EXPERIENCE = true, HEALTHMOUNT = true, JUMPBAR = true, CHAT = true, PLAYER_LIST = true, DEBUG = true;
    private static boolean moveLock, sit, sleep;
    private static GuiCGameOver gameover;

    public static boolean isActiveUI(String name) {
        if (name.equalsIgnoreCase("HAND"))
            return HAND;
        if (name.equalsIgnoreCase("all"))
            return ALL;
        if (name.equalsIgnoreCase("HELMET"))
            return HELMET;
        if (name.equalsIgnoreCase("PORTAL"))
            return PORTAL;
        if (name.equalsIgnoreCase("CROSSHAIRS"))
            return CROSSHAIRS;
        if (name.equalsIgnoreCase("BOSSHEALTH"))
            return BOSSHEALTH;
        if (name.equalsIgnoreCase("ARMOR"))
            return ARMOR;
        if (name.equalsIgnoreCase("HEALTH"))
            return HEALTH;
        if (name.equalsIgnoreCase("FOOD"))
            return FOOD;
        if (name.equalsIgnoreCase("AIR"))
            return AIR;
        if (name.equalsIgnoreCase("HOTBAR"))
            return HOTBAR;
        if (name.equalsIgnoreCase("EXPERIENCE"))
            return EXPERIENCE;
        if (name.equalsIgnoreCase("HEALTHMOUNT"))
            return HEALTHMOUNT;
        if (name.equalsIgnoreCase("JUMPBAR"))
            return JUMPBAR;
        if (name.equalsIgnoreCase("CHAT"))
            return CHAT;
        if (name.equalsIgnoreCase("PLAYER_LIST"))
            return PLAYER_LIST;
        if (name.equalsIgnoreCase("DEBUG"))
            return DEBUG;
        return false;
    }

    public static void setUI(String name, boolean isOn) {
        if (name.equalsIgnoreCase("HAND"))
            HAND = isOn;
        if (name.equalsIgnoreCase("all"))
            ALL = isOn;
        if (name.equalsIgnoreCase("HELMET"))
            HELMET = isOn;
        if (name.equalsIgnoreCase("PORTAL"))
            PORTAL = isOn;
        if (name.equalsIgnoreCase("CROSSHAIRS"))
            CROSSHAIRS = isOn;
        if (name.equalsIgnoreCase("BOSSHEALTH"))
            BOSSHEALTH = isOn;
        if (name.equalsIgnoreCase("ARMOR"))
            ARMOR = isOn;
        if (name.equalsIgnoreCase("HEALTH"))
            HEALTH = isOn;
        if (name.equalsIgnoreCase("FOOD"))
            FOOD = isOn;
        if (name.equalsIgnoreCase("AIR"))
            AIR = isOn;
        if (name.equalsIgnoreCase("HOTBAR"))
            HOTBAR = isOn;
        if (name.equalsIgnoreCase("EXPERIENCE"))
            EXPERIENCE = isOn;
        if (name.equalsIgnoreCase("HEALTHMOUNT"))
            HEALTHMOUNT = isOn;
        if (name.equalsIgnoreCase("JUMPBAR"))
            JUMPBAR = isOn;
        if (name.equalsIgnoreCase("CHAT"))
            CHAT = isOn;
        if (name.equalsIgnoreCase("PLAYER_LIST"))
            PLAYER_LIST = isOn;
        if (name.equalsIgnoreCase("DEBUG"))
            DEBUG = isOn;
        if (name.equalsIgnoreCase("blocklayer")) {
            if (isOn)
                Sky.enableBlockLayer();
            else
                Sky.disableBlockLayer();
        }
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
        TickRegister.register(new AbstractTick(Type.RENDER, 1, false) {
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
