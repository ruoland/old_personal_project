package ruo.cmplus;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.gameevent.TickEvent.Type;
import ruo.cmplus.cm.v15.GuiCGameOver;
import ruo.minigame.api.WorldAPI;
import ruo.minigame.effect.AbstractTick;
import ruo.minigame.effect.TickRegister;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

public class CMManager implements Serializable {
	private static final ResourceLocation[] shaderResourceLocations = new ResourceLocation[] {
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
			new ResourceLocation("shaders/post/antialias.json") };

	private static transient Minecraft mc = Minecraft.getMinecraft();
	private static EnumFacing sleepFacing;
	public static boolean norain, mouse, HAND = true, ALL=true,HELMET=true,PORTAL=true,CROSSHAIRS=true,BOSSHEALTH=true,ARMOR=true,HEALTH=true,FOOD=true,AIR=true,HOTBAR=true,EXPERIENCE=true,HEALTHMOUNT=true,JUMPBAR=true,CHAT=true,PLAYER_LIST=true,DEBUG=true;
	private static boolean moveLock, sit, sleep;
	public static HashMap<String, double[]> waypoint = new HashMap<String, double[]>();
	private static GuiCGameOver gameover;
	public static boolean isUI(String name){
		if(name.equalsIgnoreCase("HAND"))
			return HAND;
		if(name.equalsIgnoreCase("all"))
			return ALL;
		if(name.equalsIgnoreCase("HELMET"))
			return HELMET;
		if(name.equalsIgnoreCase("PORTAL"))
			return PORTAL;
		if(name.equalsIgnoreCase("CROSSHAIRS"))
			return CROSSHAIRS;
		if(name.equalsIgnoreCase("BOSSHEALTH"))
			return BOSSHEALTH;
		if(name.equalsIgnoreCase("ARMOR"))
			return ARMOR;
		if(name.equalsIgnoreCase("HEALTH"))
			return HEALTH;
		if(name.equalsIgnoreCase("FOOD"))
			return FOOD;
		if(name.equalsIgnoreCase("AIR"))
			return AIR;
		if(name.equalsIgnoreCase("HOTBAR"))
			return FOOD;
		if(name.equalsIgnoreCase("EXPERIENCE"))
			return EXPERIENCE;
		if(name.equalsIgnoreCase("HEALTHMOUNT"))
			return HEALTHMOUNT;
		if(name.equalsIgnoreCase("JUMPBAR"))
			return JUMPBAR;
		if(name.equalsIgnoreCase("CHAT"))
			return CHAT;
		if(name.equalsIgnoreCase("PLAYER_LIST"))
			return PLAYER_LIST;
		if(name.equalsIgnoreCase("DEBUG"))
			return DEBUG;
		return false;
	}
	
	public static void setUI(String name, boolean a){
		if(name.equalsIgnoreCase("HAND"))
			HAND = a;
		if(name.equalsIgnoreCase("all"))
			ALL = a;
		if(name.equalsIgnoreCase("HELMET"))
			HELMET = a;
		if(name.equalsIgnoreCase("PORTAL"))
			PORTAL = a;
		if(name.equalsIgnoreCase("CROSSHAIRS"))
			CROSSHAIRS = a;
		if(name.equalsIgnoreCase("BOSSHEALTH"))
			BOSSHEALTH = a;
		if(name.equalsIgnoreCase("ARMOR"))
			ARMOR=a;
		if(name.equalsIgnoreCase("HEALTH"))
			HEALTH=a;
		if(name.equalsIgnoreCase("FOOD"))
			FOOD=a;
		if(name.equalsIgnoreCase("AIR"))
			AIR=a;
		if(name.equalsIgnoreCase("HOTBAR"))
			FOOD=a;
		if(name.equalsIgnoreCase("EXPERIENCE"))
			EXPERIENCE=a;
		if(name.equalsIgnoreCase("HEALTHMOUNT"))
			HEALTHMOUNT = a;
		if(name.equalsIgnoreCase("JUMPBAR"))
			JUMPBAR=a;
		if(name.equalsIgnoreCase("CHAT"))
			CHAT = a;
		if(name.equalsIgnoreCase("PLAYER_LIST"))
			PLAYER_LIST = a;
		if(name.equalsIgnoreCase("DEBUG"))
			DEBUG=a;
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
