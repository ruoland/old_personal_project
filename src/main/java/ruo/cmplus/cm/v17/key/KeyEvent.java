package ruo.cmplus.cm.v17.key;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import org.lwjgl.input.Keyboard;
import ruo.cmplus.deb.DebAPI;
import ruo.minigame.api.WorldAPI;

import java.util.Iterator;

public class KeyEvent {
	@SubscribeEvent
	public void keyInput(KeyInputEvent e){
		//Deb.msgKey("키 입력됨"+KeyManager.instance().getKey()+KeyManager.instance().getKey().size());
		Iterator<Integer> i = KeyManager.instance().getKey().iterator();
		while(i.hasNext()){
			int key = i.next();
			//Deb.msgKey("키 찾는중-찾을 키"+Keyboard.getKeyName(key)+Keyboard.isKeyDown(key));
			if(Keyboard.isKeyDown(key) && Minecraft.getMinecraft().currentScreen == null){
				String com = KeyManager.instance().getHashMap().get(key);
				DebAPI.msgKey("키가 인식됨-"+Keyboard.getKeyName(key));
				DebAPI.msgKey("키가 인식됨-실행할 명령어:"+com);
				if(com != null && !com.equals("")){
					WorldAPI.command("/multi "+(com.startsWith("/") ? com : "/"+com));
				}else {
					DebAPI.msgKey("키가 인식됨-키를 실행하지 못함! 이 메세지는 KeyEvent클래스에서 나옴:"+com);

				}
			}
		}
		
	}
}
