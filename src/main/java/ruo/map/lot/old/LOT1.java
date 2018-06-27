package ruo.map.lot.old;

import net.minecraft.entity.player.EntityPlayer;
import ruo.cmplus.cm.v18.function.Function;
import ruo.minigame.api.WorldAPI;

import java.io.File;

public class LOT1 {
	private EntityPlayer player;
	public LOT1() {
		player = WorldAPI.getPlayer();
	}
	public void start() {
		Function function = func("lotStart", false);
		function.write("/render darkscreen minus 20");
		function.write("/camera yp true");
		function.write("/camera player true");
		function.write("/camera lock true");
		function.write("/player lock true");
		function.write("/model sleep true NORTH");
		function.write("/camera yaw set 0");
		function.write("/camera pitch set 0");
		function.write("/camera rotate set 90 -90 0");
		function.write("/camera move set 1.6 0.9 0");
		function.write("/ui hotbar false");
		function.write("/ui hand false");
		function.write("/var int 플요 @플레이어.요");
		function.write("/var int 플피치 @플레이어.피치");
		function.write("/render drawtext @플레이어... 50 30 3 3 3");
		function.write("/timer /render removetext @플레이어... 5");	
		function.write("/timer /render drawtext @플레이어...!!! 50 30 3 3 10");	
		function.write("/timer /render removetext @플레이어...!!! 12");	
		function.write("/timer /camera move set 0 -0 -3 18");
		function.write("/timer /camera rotate set 0 0 0 18");
		function.write("/timer /multi /model sleep false /model sit true 20");	
		function.write("/timer /camera lock false 20");
		function.write("/timer /camera yaw set @플레이어.요+30 20");
		function.write("/timer /camera yaw set @플레이어.요-60 22");
		function.write("/timer /camera yaw set @플레이어.요+30 24");
		function.write("/timer /multi /model sit false /player lock false 26");
		function.write("/timer /camera reset 28");
		function.write("/timer /ui reset 28");	
		function.write("/timer /function createBridge 30");

		function.writeEnd();
	}
	public void createBridge() {
		Function function = func("createBridge", false);
		function.write("/for /npc create @카운트 ~ ~ ~ BLOCK true true 10 5");
		function.write("/for /entity @카운트 velo y 0.2 1 10");
		function.write("/for /entity @카운트 velo y 0.2 1 10");
		function.write("/for /entity @카운트 velo y 0.2 1 10");
		function.write("/for /entity @카운트 velo y 0.2 1 10");//1틱에 0.2 블럭만큼 이동
		function.writeEnd();
	}
	public void openChest() {
		Function function = func("openGreatChest", false);
		function.write("/var int chestX 0");
		function.write("/var int chestY 0");
		function.write("/var int chestZ 0");
		function.write("/camera move set -1 -2 0");
		function.write("/open chest 0 0 0 true");
		function.write("/while chestX < 90 /multi /var int chestX++ /camera rotate set @chestX 0 0 1");
		function.write("/function openGreatChest pause");
		function.write("/for /if chestX == 90 /multi /function openGreatChest resume /var boolean rotateComplete true /for off 1 900");
		function.write("/function openGreatChest pause");
		function.writeEnd();
	}
	public Function func(String name, boolean isForLock) {
		File startFile = new File(WorldAPI.getCurrentWorldFile() + "/commandplus/script/"+name+".txt");
		if(startFile.exists()) {
			startFile.delete();
		}
		Function function = Function.addFunction("커맨드", name);
		return function;
	}
}
