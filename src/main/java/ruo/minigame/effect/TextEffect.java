package ruo.minigame.effect;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import ruo.cmplus.cm.v17.Deb;
import ruo.minigame.api.EntityAPI;
import ruo.minigame.api.WorldAPI;
import ruo.minigame.text.Monologue;
import ruo.minigame.text.TextEvent;

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

					Deb.msgText("메세지:"+s2);
					if(message.indexOf("/com:") != -1){
						String command = parsing(message, "/com:", ":com/");
						System.out.println("명령어 "+command);
						WorldAPI.command(command);
						s2 = s2.replace(command, "").replace("/com:", "").replace(":com/", "");

					}
					if(message.indexOf("/tp:") != -1){
						String look = parsing(message, "/tp:", ":tp/");
						String[] split = look.split(",");
						double[] xyz = WorldAPI.valueOfStr(split[1], split[2], split[3]);
						mob.setPositionAndUpdate(xyz[0], xyz[1], xyz[2]);
						Deb.msgText("텔레포트 발견함-"+xyz[0]+"   "+xyz[1]+"   "+xyz[2]);
					}
					if(message.indexOf("/look:") != -1){
						String look = parsing(message, "/look:", ":lo/");
						if(look.indexOf(",") != -1){
							String[] split = look.split(",");
							EntityAPI.look(lookMob.get(split[0]), lookMob.get(split[1]));
							Deb.msgText("특정 몬스터 LOOK 발견함-"+look);
							Deb.msgText("대상 몬스터-"+lookMob.get(split[0]));
							Deb.msgText("대상 몬스터2-"+lookMob.get(split[1]));
						}
						else if(look.equals("player"))
							EntityAPI.look((EntityMob) mob, WorldAPI.getPlayer());
						
						else if(look.equals("remove"))
							EntityAPI.removeLook((EntityMob) mob);
						else
							EntityAPI.look((EntityMob) mob, lookMob.get(look));
						Deb.msgText("LOOK 발견함-"+look);
					}
				
					if(message.indexOf("/move:") != -1){
						String move = parsing(message, "/move:", ":mo/");
						String[] split = move.split(",");
						if(split.length == 3){
							double[] xyz = WorldAPI.valueOfStr(split[0], split[1], split[2]);
							EntityAPI.move((EntityMob) mob, xyz[0], xyz[1], xyz[2]);
							Deb.msgText("이동 발견함-"+move+xyz[0]+"   "+xyz[1]+"   "+xyz[2]);
						}
						if(split.length == 4){
							double[] xyz = WorldAPI.valueOfStr(split[1], split[2], split[3]);
							EntityAPI.move(lookMob.get(split[0]), xyz[0], xyz[1], xyz[2]);
							Deb.msgText("특정 몬스터 이동 발견함-"+move+xyz[0]+"   "+xyz[1]+"   "+xyz[2]);
							Deb.msgText("대상 몬스터-"+lookMob.get(split[0]));
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
						Deb.msgText("거리 발견함-"+move);
						Deb.msgText("거리값:"+distance);
						Deb.msgText("몬스터와 플레이어 거리:"+mob.getDistanceToEntity(WorldAPI.getPlayer()));
						Deb.msgText("거리 비교:"+(distance >= mob.getDistanceToEntity(WorldAPI.getPlayer())));

						if(distance >= mob.getDistanceToEntity(WorldAPI.getPlayer())){
							WorldAPI.getPlayer().addChatComponentMessage(new TextComponentString(mob.getName()+":"+s2).setStyle(style));
						}
						return;

					}
					Deb.msgText("최종 메세지:"+s2);
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
					Deb.msgText("최종 메세지:"+s2);

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
				Deb.msgText("클래스가 없음!"+name);
				return null;
			}
			Deb.msgText("메소드 이름:"+name);
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
