package map.tycoon.map;

import oneline.api.WorldAPI;
import map.tycoon.TyconHelper;

public class MapHelper {
	private static boolean tyconOpen = true;
	private static int flowerBonus;
	public static void createSecondFloor() {
		
	}

	public static int npcPercent(){
		int percent = 700;
		if(WorldAPI.getWorld().isRaining()){
			percent += 300;
		}
		if(WorldAPI.getWorld().getWorldTime() >= 6600 && WorldAPI.getWorld().getWorldTime() <= 11616){
			percent -= 300;
		}
		percent -= flowerBonus;

		if(percent < 0)
			percent = 0;
		return percent;
	}

	public static int getFlowerBonus() {
		return flowerBonus;
	}
	public static void subFlowerBonus(int flowerBonus) {
		MapHelper.flowerBonus -= flowerBonus;
	}
	public static void setFlowerBonus(int flowerBonus) {
		MapHelper.flowerBonus = flowerBonus;
	}
	public static void addFlowerBonus(int flowerBonus) {
		MapHelper.flowerBonus += flowerBonus;
	}
	public static boolean isTyconOpen()
	{
		return tyconOpen;
	}

	public static void setTyconOpen(boolean tyconopen) {
		MapHelper.tyconOpen = tyconopen;
		if(!tyconopen){
			System.out.println("문이 닫혔음"+ TyconHelper.waitConsumerSize());
		}
	}
}
