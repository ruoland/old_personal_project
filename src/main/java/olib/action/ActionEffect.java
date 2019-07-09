package olib.action;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.util.HashMap;

/**
 * 방패 사용여부, 맵 액션 데이터 가져오기, 벽타기 기능 활성화, 더블 점프 등의 기능이 있는 클래스
 */
public class ActionEffect {
	//현재 맵 이름
	private static String mapName;
	//맵 정보
	private static HashMap<String, MapActionData> mapDataMap = new HashMap<>();

	public static boolean isUseShield(EntityPlayer player) {
		return ItemStack.areItemsEqualIgnoreDurability(player.getActiveItemStack(), new ItemStack(Items.SHIELD));
	}

    public static MapActionData getActionData(String mapName){
    	if(mapName != null) {
			if (mapDataMap.containsKey(mapName) && mapDataMap.get(mapName) != null) {
				return mapDataMap.get(mapName);
			}
			else {
				mapDataMap.put(mapName, new MapActionData(mapName));
				return mapDataMap.get(mapName);
			}
		}
		mapDataMap.put("널", new MapActionData("널"));;
		return mapDataMap.get("널");


    }
	public static void crawl(boolean onoff) {
	    getActionData(mapName).crawl(onoff);
	}

	public static boolean canCrawl() {
		return getActionData(mapName).canCrawl();
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

	public static void load(String mapName) {
    	ActionEffect.mapName = mapName;
    	DoubleJump.setMapName(mapName);
		mapDataMap.put("널", new MapActionData("널"));
		getActionData(mapName).load();
	}
}


