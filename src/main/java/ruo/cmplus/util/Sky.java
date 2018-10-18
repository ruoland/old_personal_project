package ruo.cmplus.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.lang.reflect.Field;

public class Sky {

	private static Minecraft mc = Minecraft.getMinecraft();
	private static EntityRenderer r = mc.entityRenderer;
	public static RenderGlobal global = (RenderGlobal) mc.renderGlobal;
	public static WorldProvider10 pro;
	protected static int sunbright = -1;
	protected static int skyR=0,skyG=0,skyB=0;
	protected static float cloudHeight = 128F, cloudX = 12F, cloudY = 1, cloudZ = 12F;
	protected static float fogDistance = -1, fogDestiny;
	protected static float cloudRed = 0, cloudGreen = 0, cloudBlue = 0, cloudAlpha = 1;
	protected static int starCount = 1500;
	private static boolean layer = true, fogOn;
	private static String fogMode = "";
	public static Sky instance;
	public Sky() {
		instance = this;
	}
	
	public static void fogDistance(float fog){
		if(fog == -1) {
			fogDistance = (5.0F + ((Minecraft.getMinecraft().gameSettings.renderDistanceChunks * 16) - 5.0F) * (1.0F - (float) 0 / 20.0F));
			fogOn = false;
		}
		else {
			fogDistance = fog;
			fogOn = true;
		}
	}
	public static void fogOnOff(boolean on){
		fogOn = on;
	}

	public static void setFogMode(String fogMode) {
		Sky.fogMode = fogMode;
	}

	public static String getFogMode() {
		return fogMode;
	}

	public static void setFogDestiny(float fogDestiny) {
		Sky.fogDestiny = fogDestiny;
	}

	public static float getFogDestiny() {
		return fogDestiny;
	}

	public static boolean isFogOn() {
		return fogOn;
	}

	public static void cloudSize(float x, float y , float z){
		cloudX = x;
		cloudY = y;
		cloudZ = z;
	}
	public static void cloudColor(int r, int g , int b){
		cloudRed = r;
		cloudGreen = g;
		cloudBlue = b;
	}
	public static void sunBrightness(int bright){
		sunbright = bright;
	}

	public static int getSunbright() {
		return sunbright;
	}

	public static void cloudHeight(int height){
		cloudHeight = height;
	}
	public static void skyRGB(int r, int g, int b){
		skyR = r;
		skyG = g;
		skyB = b;
	}
	
	public static void starGenerate(int count){
		if(global != null){
			starCount = count;
			((SkyRenderer)pro.getSkyRenderer()).generateStars();
		}
	}
	public static void setSunTexture(String sun){
		SkyRenderer renderer = (SkyRenderer) pro.getSkyRenderer();
		renderer.SUN_TEXTURES = sun == null ? new ResourceLocation("textures/environment/sun.png") : new ResourceLocation(sun);

	}
	public static void setMoonTexture(String sun){
		SkyRenderer renderer = (SkyRenderer) pro.getSkyRenderer();
		renderer.MOON_PHASES_TEXTURES = sun == null ? new ResourceLocation("textures/environment/moon_phases.png") : new ResourceLocation(sun);
	}

	public static void setEndSkyTexture(String sun){
		if(sun.equals(""))
		{
			((CloudRenderer)pro.getCloudRenderer()).END_SKY_TEXTURES = new ResourceLocation("textures/environment/end_sky.png");
		}else{
			((CloudRenderer)pro.getCloudRenderer()).END_SKY_TEXTURES = new ResourceLocation(sun);
		}
		
	}
	
	public static void enableBlockLayer(){
		setLayer(true);
	}
	public static void disableBlockLayer(){
		setLayer(false);
	}
	public static boolean isLayer() {
		return layer;
	}
	public static void setLayer(boolean layer) {
		Sky.layer = layer;
	}

	public static float getFogDistance() {
		if (fogDistance == -1) {
			fogDistance = (5.0F + ((Minecraft.getMinecraft().gameSettings.renderDistanceChunks * 16) - 5.0F) * (1.0F - (float)0 / 20.0F));
		}
		return fogDistance;
	}

	public static boolean isFogDistanceDefault(){
		return (5.0F + ((Minecraft.getMinecraft().gameSettings.renderDistanceChunks * 16) - 5.0F) * (1.0F - (float)0 / 20.0F)) == fogDistance;
	}

	public static void setRain(ResourceLocation res){
		Field f = ReflectionHelper.findField(EntityRenderer.class, "RAIN_TEXTURES");
		try {
			f.set(r, res);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	public static void setSnow(ResourceLocation res){
		Field f = ReflectionHelper.findField(EntityRenderer.class, "SNOW_TEXTURES");
		try {
			f.set(r, res);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
}
