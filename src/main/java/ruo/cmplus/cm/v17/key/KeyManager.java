package ruo.cmplus.cm.v17.key;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.command.CommandException;
import org.lwjgl.input.Keyboard;
import ruo.cmplus.WorldConfig;
import ruo.cmplus.deb.DebAPI;

import java.util.HashMap;
import java.util.Set;

public class KeyManager {

	private static HashMap<Integer, String> keyMap = new HashMap<>();
	private static WorldConfig worldConfig;
	private static KeyManager instance = new KeyManager();
	public static KeyManager instance(){
		worldConfig = WorldConfig.getWorldConfig();
		return instance;
	}
	public void addKey(int key, String string){
		if(keyMap.containsKey(key)) {
			DebAPI.msgKey("추가-키가 이미 있어 해당 키에 추가함 내용:"+ key+"-"+keyMap.get(key) + " "+string.trim());
			keyMap.put(key, keyMap.get(key) + " "+string.trim());
			return;
		}
		keyMap.put(key, string == null ? "" : string);
		DebAPI.msgKey("추가-키코드"+key+" 명령어"+string);
	}
	public void removeKey(int key){
		KeyBinding.setKeyBindState(key, false);
		keyMap.remove(key);
		DebAPI.msgKey("삭제-키코드"+key+" 명령어"+keyMap.get(key));
	}
	public void replaceKey(KeyBinding key, int key2) throws CommandException{
		KeyBinding.unPressAllKeys();
		key.setKeyCode(key2);
		keyMap.put(key2, keyMap.get(key.getKeyCode()) == null ? "" : keyMap.get(key.getKeyCode()));
		keyMap.remove(key.getKeyCode());
		DebAPI.msgKey("리플레이스- 첫번째 키코드"+"---"+key.getDisplayName()+"---"+key.getKeyCode()+"-두번째 키코드"+"---"+key.getDisplayName()+"---"+keyMap.get(key.getKeyCode()));
	}
	
	public Set<Integer> getKey(){
		return ((HashMap<Integer, String>) keyMap.clone()).keySet();
	}
	public HashMap<Integer,String> getHashMap(){
		return keyMap;
	}
	
	public void saveKey(){
		for(int key: getKey()){
			worldConfig.setProperty(String.valueOf(key), keyMap.get(key));
		}
	}

	public void loadKey(){
		for(Object key : worldConfig.keySet()){
			String str = (String) key;
			keyMap.put(Integer.valueOf(str), worldConfig.getProperty(str).getString());
		}

	}
	
	public KeyBinding getKeyBinding(String key) throws CommandException{
		KeyBinding keyBinding = getKeyB(key);
		if(keyBinding == null)
			throw new CommandException(key+"라는 키코드를 가진 키를 찾을 수 없습니다.");
		else
			return keyBinding;
		
	}
	private KeyBinding getKeyB(String key){
		key = key.trim();
		for(int i = 0; i < Minecraft.getMinecraft().gameSettings.keyBindings.length;i++){
			KeyBinding keybind = Minecraft.getMinecraft().gameSettings.keyBindings[i];
			String keyDesc = I18n.format(keybind.getKeyDescription(), new Object[0]);
			String keyName = keybind.getDisplayName();
			String keyCode = ""+keybind.getKeyCode();

			if(keyDesc.equalsIgnoreCase(key) || keyName.equalsIgnoreCase(key)  || keyCode.equalsIgnoreCase(key) || keybind.getKeyCode() == getKey(key)){
				DebAPI.msgKey("getKeyB-"+key+"-"+keyName+"키를 반환했습니다.");
				return keybind;
			}
		}
		DebAPI.msgKey("getKeyB-"+key+"키가 없습니다.");
		return null;
	}
	public int getKey(String key) {
		try {
			return Keyboard.getKeyIndex(key);
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	

	//아래 주석은 언제 생긴 걸까? 2018년 8월 15일 남김

	//키 코드는 문제가 많네..
	//슬프다.
	//이번에는 왜 게임이 멈춰버리는 걸까?..
	
	
	//System.out.println("대체 뭐지? 왜 폴문을 한번만 작동 하는 거야? 오류가 나는데 안보이는 건가?");
	//}
	//System.out.println("키가 없어서 널값을 반환했습니다 그러니까 좀 제대로 좀 반환해. 왜 폴문이 한번만 작동하는데 진짜 미스테리다 이건. 조건문을 전부 거짓이라고 표시됐는데 왜 이 메세지는 안뜨고 폴문이 한번만 작동하는 건데. 진짜 대체 뭔데 아짜증나ㅇ내ㅔㅔ매아ㅔ매ㅏ에ㅐ만에ㅐ마ㅔㄹ마ㅔ메메메메애메메메ㅔ메메메");
	//System.out.println("이 메세지가 나온다면 왜 그런 걸까 애초에 이건 나올 수가 없잖아. 왜 왜 왜 폴문이 지ㅏㄱ동해거헴더게(@ㅑ_밍;ㅓㄹ");
	//System.out.println("와. 조금만 더 깊게 살폈다면 쉽게 발견할 수 있는 버그였네. 황당해서 기록한다. 키의 영어 이름을 입력하면  키코드를 반환하는 코드가 문제였음. 그 메서드에서 키를 찾지 못하면 오류를 띄우고 ");

}
