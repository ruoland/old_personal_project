package ruo.minigame.action;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBasePressurePlate;
import net.minecraft.block.BlockLiquid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.ConfigCategory;
import ruo.minigame.MiniGame;
import ruo.minigame.api.BlockAPI;
import ruo.minigame.api.WorldAPI;

import java.util.ArrayList;
import java.util.HashMap;

public class ActionEffect {
	static String mapName;
	public static void inWaterRemove() {
		inWaterMap.remove(mapName);
	}
	public static void inWater(boolean inWater) {
		inWaterMap.put(mapName, inWater);
	}
	//CommandJB , MiniGameClientPlayer 에 있음
	public static boolean isInWater() {
		return inWaterMap.containsKey(mapName);
	}
	public static boolean getInWater() {
		return inWaterMap.containsKey(mapName) && inWaterMap.get(mapName);
	}

	public static boolean isUseShield(EntityPlayer player) {
		return ItemStack.areItemsEqualIgnoreDurability(player.getActiveItemStack(), new ItemStack(Items.SHIELD));
	}

	public static void crawl(boolean onoff) {
		if(onoff && !crawlMapList.contains(mapName)) {
			crawlMapList.add(mapName);
		}else if(!onoff && crawlMapList.indexOf(mapName) != -1) {
			crawlMapList.remove(crawlMapList.get(crawlMapList.indexOf(mapName)));

		}
	}

	public static boolean canCrawl() {
		return crawlMapList.contains(mapName);
	}

	public static void doubleJump(boolean on) {
		if(on && !djList.contains(mapName)) {
			djList.add(mapName);
		}else if(!on && djList.indexOf(mapName) != -1) {
			djList.remove(djList.get(djList.indexOf(mapName)));
			if(djList.indexOf(mapName) != -1)
				djList.remove(djList.get(djList.indexOf(mapName)));

		}
	}

	public static boolean canDoubleJump() {
		return djList.contains(mapName);
	}

	public static void teleportSpawnPoint(EntityPlayer player){
		Block block = player.worldObj.getBlockState(player.getBedLocation().add(0, -1, 0)).getBlock();
		if(block instanceof BlockLiquid || player.worldObj.isAirBlock(player.getBedLocation().add(0, -1, 0))){
			BlockAPI blockAPI = WorldAPI.getBlock(player.worldObj, player.getBedLocation(), 4D);
			for(int i=0;i<blockAPI.size();i++){
				if(blockAPI.getBlock(i) instanceof BlockBasePressurePlate){
					player.setSpawnPoint(blockAPI.getPos(i), true);
					WorldAPI.teleport(player.getBedLocation().add(0, 1, 0), ActionEffect.getYaw(), ActionEffect.getPitch());
					break;
				}
			}
		}
	}
	public static void setYTP(double tpy, float pitch, float yaw){
		tpYMap.put(mapName, tpy);
		tpPitchMap.put(mapName, pitch);
		tpYawMap.put(mapName, yaw);
	}
	public static void setYP(float yaw, float pitch){
		tpPitchMap.put(mapName, pitch);
		tpYawMap.put(mapName, yaw);
	}
	public static double getYTP() {
		return tpYMap.containsKey(mapName) ? tpYMap.get(mapName) : 0;
	}

	public static float getYaw() {
		return tpYawMap.get(mapName);
	}

	public static float getPitch() {
		System.out.println(mapName);
		return tpPitchMap.get(mapName);
	}


	private static ArrayList<String> crawlMapList = new ArrayList<>();
	private static HashMap<String, Boolean> inWaterMap = new HashMap<>();//물 속에 있는 효과를 내거나 물 속에 없는 효과를 냄
	private static ArrayList<String> djList = new ArrayList<>();
	private static HashMap<String, Double> tpYMap = new HashMap<>();
	private static HashMap<String, Float> tpYawMap = new HashMap<>();
	private static HashMap<String, Float> tpPitchMap = new HashMap<>();

	public static void save() {
		String worldName = ActionEffect.mapName;
		System.out.println(worldName+"세이브 "+djList.contains(worldName));
		if (!worldName.equalsIgnoreCase("noworld")) {
			MiniGame.instance.minigameConfig.get(worldName, "crawl", false).set(crawlMapList.contains(worldName));
			if(inWaterMap.containsKey(worldName))
				MiniGame.instance.minigameConfig.get(worldName, "inWater", false).set(inWaterMap.get(worldName));
			MiniGame.instance.minigameConfig.get(worldName, "doubleJump", false).set(djList.contains(worldName));
			if (tpYMap.containsKey(worldName)) {
				MiniGame.instance.minigameConfig.get(worldName, "tpY", 0).set(tpYMap.get(worldName));
				MiniGame.instance.minigameConfig.get(worldName, "tpYaw", 0).set(tpYawMap.get(worldName));
				MiniGame.instance.minigameConfig.get(worldName, "tpPitch", 0).set(tpPitchMap.get(worldName));
			}
		}
	}

	public static void load() {
		for (String category : MiniGame.instance.minigameConfig.getCategoryNames()) {
			if(category.equalsIgnoreCase(mapName))
				category = mapName;
			ConfigCategory action = MiniGame.instance.minigameConfig.getCategory(category);
			if (action.containsKey("crawl") && action.get("crawl").getBoolean())
				crawlMapList.add(category);
			if (action.containsKey("inWater") )
				inWaterMap.put(category, action.get("inWater").getBoolean());
			if (action.containsKey("doubleJump")&& action.get("doubleJump").getBoolean())
				djList.add(category);
			if (action.containsKey("tpY")) {
				tpYMap.put(category, action.get("tpY").getDouble());
				tpYawMap.put(category, (float) action.get("tpYaw").getDouble());
				tpPitchMap.put(category, (float) action.get("tpPitch").getDouble());
			}
			System.out.println(category+ action.get("doubleJump").getBoolean());
		}
	}
}


