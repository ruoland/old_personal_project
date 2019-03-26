package oneline.effect;

import cmplus.deb.DebAPI;
import oneline.api.EntityAPI;
import oneline.api.WorldAPI;
import oneline.text.Monologue;
import oneline.text.TextEvent;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class TextEffect {
	private EntityLivingBase base;
	private static TextEffect nullEffect;
	public static HashMap<String, EntityMob> lookMob = new HashMap<String, EntityMob>();//
	
	public TextEffect() {
	}
	public TextEffect(EntityLivingBase base) {
		this.base = base;
	}
	public static TextEffect getHelper(){
		if(nullEffect == null)
			nullEffect = new TextEffect();
		return nullEffect;
	}
	public static TextEffect getHelper(EntityLivingBase base){
		if(base != null){
			lookMob.put(base.getName(), base instanceof EntityMob ? (EntityMob) base : null);
		}
		return new TextEffect(base);
	}
	public static TextEffect getPlayerHelper(){
		return new TextEffect(WorldAPI.getPlayer());
	}

	public void addChat(int time, String text){
		addChat(time, text, null);
	}
	public void addChat(int distance, int time, String text){
		addChat(distance, time, text, null);
	}
	public void addChat(int time, String text, String tick){
 		addChat(base, time, text, tick);
	}
	public void addChat(int distance, int time, String text, String tick){
 		addChat(distance, base, time, text, tick);
	}
	public void addChat(EntityLivingBase mob, int time, String text){
		addChat(mob, time, text, null);
	}
	public void addChat(int distance, EntityLivingBase mob, int time, String text){
		addChat(mob, time, text+"/distance:"+distance+":dis/", null);
	}
	public void addChat(int distance, EntityLivingBase mob, int time, String text, String tick){
		addChat(mob, time, text+"/distance:"+distance+":dis/", tick);
	}
	Timer timer;
	public void addChat(EntityLivingBase mob, int time, String text, String tick){
 		TimerTask t = timerChat(mob, text, gm(tick));
 		timer = new Timer();
		timer.schedule(t, time);
	}
	
	public void cancel(){
		if(timer != null)
		timer.cancel();
		timer = new Timer();
	}
	private TimerTask timerChat(final EntityLivingBase mob, final String message, final Method tick){
		return new TimerTask() {
			@Override
			public void run() {
				if(mob != null && WorldAPI.getPlayer() != null && WorldAPI.getWorld() != null){
					invoke(tick);
					String s2 = message.replace("/look:player:lo/", "").replace("/look:remove:lo/", "").replace("/bold/", "");

					DebAPI.msgText("MiniGame","메세지:"+s2);
					if(message.indexOf("/com:") != -1){
						String command = parsing(message, "/com:", ":com/");
						DebAPI.msgText("MiniGame","명령어 "+command);
						WorldAPI.command(command);
						s2 = s2.replace(command, "").replace("/com:", "").replace(":com/", "");

					}
					if(message.indexOf("/tp:") != -1){
						String look = parsing(message, "/tp:", ":tp/");
						String[] split = look.split(",");
						double[] xyz = WorldAPI.valueOfStr(split[1], split[2], split[3]);
						mob.setPositionAndUpdate(xyz[0], xyz[1], xyz[2]);
						DebAPI.msgText("MiniGame","텔레포트 발견함-"+xyz[0]+"   "+xyz[1]+"   "+xyz[2]);
					}
					if(message.indexOf("/look:") != -1){
						String look = parsing(message, "/look:", ":lo/");
						if(look.indexOf(",") != -1){
							String[] split = look.split(",");
							EntityAPI.look(lookMob.get(split[0]), lookMob.get(split[1]));
							DebAPI.msgText("MiniGame","특정 몬스터 LOOK 발견함-"+look);
							DebAPI.msgText("MiniGame","대상 몬스터-"+lookMob.get(split[0]));
							DebAPI.msgText("MiniGame","대상 몬스터2-"+lookMob.get(split[1]));
						}
						else if(look.equals("player"))
							EntityAPI.look((EntityMob) mob, WorldAPI.getPlayer());
						
						else if(look.equals("remove"))
							EntityAPI.removeLook((EntityMob) mob);
						else
							EntityAPI.look((EntityMob) mob, lookMob.get(look));
						DebAPI.msgText("MiniGame","LOOK 발견함-"+look);
					}
				
					if(message.indexOf("/move:") != -1){
						String move = parsing(message, "/move:", ":mo/");
						String[] split = move.split(",");
						if(split.length == 3){
							double[] xyz = WorldAPI.valueOfStr(split[0], split[1], split[2]);
							EntityAPI.move((EntityMob) mob, xyz[0], xyz[1], xyz[2]);
							DebAPI.msgText("MiniGame","이동 발견함-"+move+xyz[0]+"   "+xyz[1]+"   "+xyz[2]);
						}
						if(split.length == 4){
							double[] xyz = WorldAPI.valueOfStr(split[1], split[2], split[3]);
							EntityAPI.move(lookMob.get(split[0]), xyz[0], xyz[1], xyz[2]);
							DebAPI.msgText("MiniGame","특정 몬스터 이동 발견함-"+move+xyz[0]+"   "+xyz[1]+"   "+xyz[2]);
							DebAPI.msgText("MiniGame","대상 몬스터-"+lookMob.get(split[0]));
						}

					}
					Style style = new Style();

					if(message.indexOf("/bold/")!= -1){
						style.setBold(true);
					}
					if(message.indexOf("/color:") != -1) {
						s2 = s2.replace(parsing(message, "/color:"+"/color:", ":color/")+":color/", "");
						style.setColor(TextFormatting.getValueByName(parsing(message, "/color:", ":color/")));
					}
					if(message.indexOf("/distance:") != -1){
						String move = message.substring(message.indexOf("/distance:"), message.indexOf(":dis/"));
						s2 = s2.replace(move, "").replace("/distance:", "").replace(":dis/", "");
						int distance = Integer.valueOf(move.replace("/distance:", ""));
						DebAPI.msgText("MiniGame","거리 발견함-"+move);
						DebAPI.msgText("MiniGame","거리값:"+distance);
						DebAPI.msgText("MiniGame","몬스터와 플레이어 거리:"+mob.getDistanceToEntity(WorldAPI.getPlayer()));
						DebAPI.msgText("MiniGame","거리 비교:"+(distance >= mob.getDistanceToEntity(WorldAPI.getPlayer())));

						if(distance >= mob.getDistanceToEntity(WorldAPI.getPlayer())){
							WorldAPI.getPlayer().addChatComponentMessage(new TextComponentString(mob.getName()+":"+s2).setStyle(style));
						}
						return;

					}
					DebAPI.msgText("MiniGame","최종 메세지:"+s2);
					WorldAPI.getPlayer().addChatComponentMessage(new TextComponentString(mob.getName()+":"+s2).setStyle(style));
				}
				else{
					invoke(tick);
					Style style = new Style();
					if(message.indexOf("/bold/")!= -1){
						style.setBold(true);
					}
					String s2 = message.replace("/look:player/", "").replace("/look:remove/", "").replace("/bold/", "");
					for(String message : replaceList) {
						s2 = s2.replace(message, "");
					}
					WorldAPI.getPlayer().addChatComponentMessage(new TextComponentString(s2).setStyle(style));
					DebAPI.msgText("MiniGame","최종 메세지:"+s2);

				}
			}
		};
	}
	private ArrayList<String> replaceList = new ArrayList<String>();
	
	private String parsing(String message, String first, String second) {
		String move = message.substring(message.indexOf(first), message.indexOf(second)).replace(first, "");
		if(message.indexOf(message) != -1){
			message = message.replace(first+move+second, "");
			replaceList.add(first+move+second);
		}
		return move;
	}
	private void invoke(Method tick){
		if(tick != null){
			try {
				tick.setAccessible(true);
				if(command != null)
					tick.invoke(null, command == null ? null : command);
				else
					tick.invoke(null);

				command = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
 	public static boolean isTextEnd() {
		return TextEvent.isTextEnd();
	}
	private TimerTask timerText(EntityMob mob, Timer timer, String[] s){
		return new TimerTask() {
			@Override
			public void run() {

				TextEvent.getTextEvent().addNext();
			}
		};
	}
	
	private Class gmClass;
	private String command;
	
	public Method gm(String name){
		try {
			if(gmClass == null && name != null) {
				DebAPI.msgText("MiniGame","클래스가 없음!"+name);
				return null;
			}
			DebAPI.msgText("MiniGame","메소드 이름:"+name);
			if(name != null){
				if(name.startsWith("명령어:")){
					command = name.replace("명령어:", "");
					return WorldAPI.class.getMethod("command", String.class);
				}
				else
					return gmClass.getDeclaredMethod(name);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void setClass(Class c){
		gmClass = c;
	}
	
	public static Monologue monologue(StringBuffer... s){
		Monologue monologue = new Monologue();
		for(StringBuffer text : s){
			monologue.addText(0, text.toString());
		}
		return monologue;
	}
	public static Monologue monologue(){
		Monologue monologue = new Monologue();
		return monologue;
	}
	public static Monologue monologue(String... s){
		Monologue monologue = new Monologue();
		for(String text : s){
			monologue.addText(0, text);
		}
		return monologue;
	}
	public void addText(EntityMob mob, String... s){
		for(String text : s)
			TextEvent.addText(text, mob);
		TextEvent.end();
	}

}
