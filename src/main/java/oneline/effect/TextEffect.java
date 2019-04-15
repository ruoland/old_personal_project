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
	private static final TextEffect nullEffect  = new TextEffect();
	public static HashMap<String, EntityMob> lookMob = new HashMap<String, EntityMob>();//
	
	public TextEffect() {
	}
	public TextEffect(EntityLivingBase base) {
		this.base = base;
	}
	public static TextEffect getHelper(){
		return nullEffect;
	}

	public void addChat(int time, String text){
		addChat(time, text, null);
	}
	public void addChat(int time, String text, AbstractTick absTick){
 		addChat(base, time, text, absTick);
	}
	public void addChat(EntityLivingBase mob, int time, String text){
		addChat(mob, time, text, null);
	}
	Timer timer;
	public void addChat(EntityLivingBase mob, int time, String text, AbstractTick absTick){
 		TimerTask t = timerChat(mob, text, absTick);
 		timer = new Timer();
		timer.schedule(t, time);
	}
	
	public void cancel(){
		if(timer != null)
		timer.cancel();
		timer = new Timer();
	}
	private TimerTask timerChat(final EntityLivingBase mob, final String message, final AbstractTick absTick){
		return new TimerTask() {
			@Override
			public void run() {
				if(mob != null && WorldAPI.getPlayer() != null){
					absTick.run();
					String s2 = message.replace("/look:player:lo/", "").replace("/look:remove:lo/", "").replace("/bold/", "");

					DebAPI.msgText("MiniGame","[TextEffect] 출력할 메세지:"+s2);
					if(message.contains("/com:")){
						String command = parsing(message, "/com:", ":com/");
						DebAPI.msgText("MiniGame","[TextEffect] 명령어 "+command);
						WorldAPI.command(command);
						s2 = s2.replace(command, "").replace("/com:", "").replace(":com/", "");

					}
					if(message.contains("/tp:")){
						String look = parsing(message, "/tp:", ":tp/");
						String[] split = look.split(",");
						double[] xyz = WorldAPI.valueOfStr(split[1], split[2], split[3]);
						mob.setPositionAndUpdate(xyz[0], xyz[1], xyz[2]);
						DebAPI.msgText("MiniGame","[TextEffect] 텔레포트 발견함-"+xyz[0]+"   "+xyz[1]+"   "+xyz[2]);
					}
					if(message.contains("/look:")){
						String look = parsing(message, "/look:", ":lo/");
						if(look.contains(",")){
							String[] split = look.split(",");
							EntityAPI.look(lookMob.get(split[0]), lookMob.get(split[1]));
							DebAPI.msgText("MiniGame","[TextEffect] 특정 몬스터 LOOK 발견함-"+look);
							DebAPI.msgText("MiniGame","[TextEffect] 대상 몬스터-"+lookMob.get(split[0]));
							DebAPI.msgText("MiniGame","[TextEffect] 대상 몬스터2-"+lookMob.get(split[1]));
						}
						else if(look.equals("player"))
							EntityAPI.look((EntityMob) mob, WorldAPI.getPlayer());
						
						else if(look.equals("remove"))
							EntityAPI.removeLook((EntityMob) mob);
						else
							EntityAPI.look((EntityMob) mob, lookMob.get(look));
						DebAPI.msgText("MiniGame","[TextEffect] LOOK 발견함-"+look);
					}
				
					if(message.contains("/move:")){
						String move = parsing(message, "/move:", ":mo/");
						String[] split = move.split(",");
						if(split.length == 3){
							double[] xyz = WorldAPI.valueOfStr(split[0], split[1], split[2]);
							EntityAPI.move((EntityMob) mob, xyz[0], xyz[1], xyz[2]);
							DebAPI.msgText("MiniGame","[TextEffect] 이동 발견함-"+move+xyz[0]+"   "+xyz[1]+"   "+xyz[2]);
						}
						if(split.length == 4){
							double[] xyz = WorldAPI.valueOfStr(split[1], split[2], split[3]);
							EntityAPI.move(lookMob.get(split[0]), xyz[0], xyz[1], xyz[2]);
							DebAPI.msgText("MiniGame","[TextEffect] 특정 몬스터 이동 발견함-"+move+xyz[0]+"   "+xyz[1]+"   "+xyz[2]);
							DebAPI.msgText("MiniGame","[TextEffect] 대상 몬스터-"+lookMob.get(split[0]));
						}

					}
					Style style = new Style();

					if(message.contains("/bold/")){
						style.setBold(true);
					}
					if(message.contains("/color:")) {
						s2 = s2.replace(parsing(message, "/color:"+"/color:", ":color/")+":color/", "");
						style.setColor(TextFormatting.getValueByName(parsing(message, "/color:", ":color/")));
					}
					if(message.contains("/distance:")){
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
					absTick.run();
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
		if(message.contains(message)){
			message = message.replace(first+move+second, "");
			replaceList.add(first+move+second);
		}
		return move;
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
