package oneline.action;

import minigameLib.MiniGame;
import oneline.api.BlockAPI;
import oneline.api.WorldAPI;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBasePressurePlate;
import net.minecraft.block.BlockLiquid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.ConfigCategory;

import java.util.ArrayList;
import java.util.HashMap;

public class ActionEffect {
	static String mapName;
	public static boolean canDoubleJump, isPlayerJump;
	public static boolean isUseShield(EntityPlayer player) {
		return ItemStack.areItemsEqualIgnoreDurability(player.getActiveItemStack(), new ItemStack(Items.SHIELD));
	}
    private static HashMap<String, ActionData> actionMap = new HashMap<>();

    public static ActionData getActionData(String mapName){
        if(actionMap.containsKey(mapName))
        return actionMap.get(mapName);
        else {
			return actionMap.put(mapName, new ActionData(mapName));
		}
    }
	public static void crawl(boolean onoff) {
	    getActionData(mapName).crawl(onoff);
	}

	public static boolean canCrawl() {
		return getActionData(mapName).canCrawl();
	}

	public static void doubleJump(boolean on) {
        getActionData(mapName).mapDoubleJump(on);
	}

    public static boolean isPlayerJump() {
        return getActionData(mapName).isPlayerJump();
    }

    public static void setPlayerJump(boolean playerJump) {
        getActionData(mapName).setPlayerJump(playerJump);
    }

	public static boolean canMapDoubleJump() {
		return getActionData(mapName).canMapDoubleJump();
	}

	public static void teleportSpawnPoint(EntityPlayer player){
        getActionData(mapName).teleportSpawnPoint(player);
	}
	public static void setYTP(double tpy, float pitch, float yaw){
        getActionData(mapName).setYTP(tpy, pitch, yaw);
	}
	public static void setYP(float yaw, float pitch){
        getActionData(mapName).setYP(yaw, pitch);
	}
	public static double getYTP() {
		return getActionData(mapName).getYTP();
	}

	public static float getYaw() {
		return getActionData(mapName).getYaw();
	}

	public static float getPitch() {
		return getActionData(mapName).getPitch();
	}


	public static void save() {
        getActionData(mapName).save();
	}

	public static void load() {
    	System.out.println(getActionData(mapName));
        getActionData(mapName).load();
	}
}


