package ruo.minigame.minigame;

import net.minecraft.init.Items;
import net.minecraftforge.common.MinecraftForge;
import ruo.cmplus.cm.CommandChat;
import ruo.minigame.api.WorldAPI;

public abstract class AbstractMiniGame {
	public long startTime, endTime;

	private boolean isStart;
	public boolean start(Object... obj){
		if(!isStart){
			isStart = true;
			startTime = System.currentTimeMillis();

		}
		return isStart;

	}
	
	
	public boolean end(Object... obj){
		if(isStart){
			isStart = false;


			endTime = System.currentTimeMillis();
			long se = endTime - startTime;
			long sec = se / (1000);
			long minute = sec / 60;
			long second = sec - sec / 60 * 60;
			System.out.println((endTime - startTime) / 1000 + "초.");
			WorldAPI.addMessage("클리어 시간:" + minute + "분 " + second + "초");
		}
		return isStart;
	}
	
	public boolean isStart(){
		return isStart;
	}
}
