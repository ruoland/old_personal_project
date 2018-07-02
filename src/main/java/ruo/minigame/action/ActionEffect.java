package ruo.minigame.action;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBasePressurePlate;
import net.minecraft.block.BlockLiquid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import ruo.minigame.api.BlockAPI;
import ruo.minigame.api.WorldAPI;
import ruo.minigame.minigame.minerun.MineRun;

public class ActionEffect {
	public static String mapName;
	public static void inWaterRemove() {
		ActionData.inWaterMap.remove(mapName);
	}
	public static void inWater(boolean inWater) {
		ActionData.inWaterMap.put(mapName, inWater);
	}
	//CommandJB , MiniGameClientPlayer 에 있음
	public static boolean isInWater() {
		return ActionData.inWaterMap.containsKey(mapName);
	}
	public static boolean getInWater() {
		return ActionData.inWaterMap.containsKey(mapName) && ActionData.inWaterMap.get(mapName);
	}

	public static boolean isUseShield(EntityPlayer player) {
		return ItemStack.areItemsEqualIgnoreDurability(player.getActiveItemStack(), new ItemStack(Items.SHIELD));
	}

	public static void crawl(boolean onoff) {
		if(onoff && !ActionData.crawlMapList.contains(mapName)) {
			ActionData.crawlMapList.add(mapName);
		}else if(!onoff && ActionData.crawlMapList.indexOf(mapName) != -1) {
			ActionData.crawlMapList.remove(ActionData.crawlMapList.indexOf(mapName));

		}
	}

	public static boolean canCrawl() {
		return ActionData.crawlMapList.contains(mapName);
	}

	public static void doubleJump(boolean onoff) {
		if(onoff && !ActionData.doubleJumpList.contains(mapName)) {
			ActionData.doubleJumpList.add(mapName);
		}else if(!onoff && ActionData.doubleJumpList.indexOf(mapName) != -1)
			ActionData.doubleJumpList.remove(ActionData.doubleJumpList.indexOf(mapName));
	}

	public static boolean canDoubleJump() {
		return ActionData.doubleJumpList.contains(mapName);
	}

	public static void teleportSpawnPoint(EntityPlayer player){
		Block block = player.worldObj.getBlockState(player.getBedLocation().add(0, -1, 0)).getBlock();
		if(block instanceof BlockLiquid || player.worldObj.isAirBlock(player.getBedLocation().add(0, -1, 0))){
			BlockAPI blockAPI = WorldAPI.getBlock(player.worldObj, player.getBedLocation(), 4D);
			for(int i=0;i<blockAPI.size();i++){
				if(blockAPI.getBlock(i) instanceof BlockBasePressurePlate){
					MineRun.deadCount++;
					player.setSpawnPoint(blockAPI.getPos(i), true);
					WorldAPI.teleport(player.getBedLocation().add(0, 1, 0), ActionEffect.getYaw(), ActionEffect.getPitch());
					break;
				}
			}
		}
	}
	public static void setYTP(double tpy, float pitch, float yaw){
		ActionData.tpYMap.put(mapName, tpy);
		ActionData.tpPitchMap.put(mapName, pitch);
		ActionData.tpYawMap.put(mapName, yaw);
	}
	public static void setYP(float yaw, float pitch){
		ActionData.tpPitchMap.put(mapName, pitch);
		ActionData.tpYawMap.put(mapName, yaw);
	}
	public static double getYTP() {
		return ActionData.tpYMap.containsKey(mapName) ? ActionData.tpYMap.get(mapName) : 0;
	}

	public static float getYaw() {
		return ActionData.tpYawMap.get(mapName);
	}

	public static float getPitch() {
		return ActionData.tpPitchMap.get(mapName);
	}
}


