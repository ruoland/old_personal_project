package olib.text;

import cmplus.cm.CommandChat;
import olib.api.DrawTexture;
import olib.api.RenderAPI;
import olib.api.WorldAPI;
import olib.effect.TickTask;
import olib.effect.TickRegister;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.Type;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;

import java.util.ArrayList;

public class TextEvent {
	private static ArrayList<String> textList = new ArrayList<String>();//
	private boolean nextText;// ���� ���� �ִ���
	private static boolean end;// �ٸ� ���Ǿ��� ��ȭ�ϴ� ��츦 �����ϱ� ����
	
	private boolean look;
	private int yaw = -10000, pitch = -10000;
	
	private boolean move;
	private double x = -10000, y = -10000, z = -10000;
	
	private static int next;// ���°
	private static EntityMob mob2;
	private Minecraft mc = Minecraft.getMinecraft();

	private static TextEvent textevent;
	public TextEvent() {
		this.textevent = this;
	}
	
	@SubscribeEvent
	public void onUpdate(WorldTickEvent e){
		if(e.phase == Phase.END){
			if(move){
				int[] mobPos = WorldAPI.roundPos(mob2);
				int[] mobPos2 = WorldAPI.roundPos(x,y,z);

				if(mobPos[0] == mobPos2[0] && mobPos[1] == mobPos2[1] && mobPos[2] == mobPos2[2]){
					move = false;
					return;
				}
				
				if(x == -10000 && y == -10000){
					double[] pos = WorldAPI.changePosArray(WorldAPI.getPlayer());
					if(mob2.getDistanceSqToEntity(WorldAPI.getPlayer()) < 3){
						return;
					}
					mob2.getMoveHelper().setMoveTo(pos[0], pos[1], pos[2], 1);
					mob2.getLookHelper().setLookPositionWithEntity(WorldAPI.getPlayer(), (float)mob2.getHorizontalFaceSpeed(), (float)mob2.getVerticalFaceSpeed());
				}
				else{
					
					mob2.getMoveHelper().setMoveTo(x,y,z, 0.4);
					mob2.getLookHelper().setLookPosition(x, y, z, (float)mob2.getHorizontalFaceSpeed(), (float)mob2.getVerticalFaceSpeed());
				}
				
				
			}
			if(look){
				if(yaw == -10000){
					mob2.getLookHelper().setLookPositionWithEntity(WorldAPI.getPlayer(), (float)mob2.getHorizontalFaceSpeed(), (float)mob2.getVerticalFaceSpeed());
					mob2.getLookHelper().onUpdateLook();
				}else{
					mob2.rotationYawHead = yaw;
					mob2.rotationPitch=pitch;
				}
			}
			
		}
				
	}
	@SubscribeEvent
	public void event(RenderGameOverlayEvent.Post event) {
		int width = event.getResolution().getScaledWidth();
		int height = event.getResolution().getScaledHeight();

		//if (event.getType() == ElementType.ALL && WorldAPI.getPlayer() != null)
			//RenderAPI.drawString("��:" + WorldAPI.getWorldDay(WorldAPI.getPlayer().worldObj) + " �ð�:" + WorldAPI.getWorldHours() + ":" + WorldAPI.getWorldMinutes(), 0, 0, 0xFFFFFF);

		if (event.getType() == ElementType.ALL && event.getPhase() == EventPriority.NORMAL && textList.size() != 0) {
			double s = (double) width / (double) height;
			RenderAPI.drawTexture(new DrawTexture.Builder().setTexture("loop:textures/text.png").setAlpha(0.4F).setXYAndSize(  90, 140, 245, 80).build());

			if (textList.size() > 0)
				nextText = true;
			else if (textList.size() == 1){
				textChange(textList.get(0), width, height);
			}
			if(nextText){
				textChange(textList.get(next), width, height);
			}

		}
	}

	private static int mouseCount = 0;
	@SubscribeEvent
	public void mouse(MouseEvent e) {
		if(e.getButton() == 0 && e.isButtonstate() && mouseCount == 0 && !end){
			TextEvent.getTextEvent().addNext();
			mouseCount = 10;
			TickRegister.register(new TickTask(10, false) {
				@Override
				public void run(Type type) {
					mouseCount = 0;
				}
			});
			return;
		}
		if(e.getButton() == 1 && e.isButtonstate()){
			if(textList.size() > 0 && nextText && next > 0){
				next --;
			}
		}
	}
	
	public static TextEvent getTextEvent(){
		return textevent;
	}
	public void addNext(){
		next++;
		if (next >= textList.size()) {
			next = 0;
			textList.clear();
			nextText = false;
			end = false;
		}

	}


	public String checkWidth(String text){
		if(text == null || text.equals("")){
			return "";
		}

		if(mc.fontRendererObj.getStringWidth(text) <= 240){
			return text;
		}
		for(String i = text;mc.fontRendererObj.getStringWidth(i) >= 230;i = i.substring(0, i.length()-1)){
			if(mc.fontRendererObj.getStringWidth(i) <= 240){
				return i;
			}
		}
		return "";
	}
	
	public void textChange(String stext, int width, int height){
		if(stext == null)
			return ;
   		stext = stext.replace("/playername/", WorldAPI.getPlayer().getName());
		String[] line = new String[]{"", "", "", "", "", "", "", "", "", ""};

   		int stringWidth = mc.fontRendererObj.getStringWidth(stext);

   		line[0] = checkWidth(stext);
   		line[1] = checkWidth(stext.replace(line[0], ""));
   		line[2] = checkWidth(stext.replace(line[0]+line[1], ""));
   		line[3] = checkWidth(stext.replace(line[0]+line[1]+line[2], ""));
   		line[4] = checkWidth(stext.replace(line[0]+line[1]+line[2]+line[3], ""));
   		line[5] = checkWidth(stext.replace(line[0]+line[1]+line[2]+line[3]+line[4], ""));
   		line[6] = checkWidth(stext.replace(line[0]+line[1]+line[2]+line[3]+line[4]+line[5], ""));
   		line[7] = checkWidth(stext.replace(line[0]+line[1]+line[2]+line[3]+line[4]+line[5]+line[6], ""));
   		line[8] = checkWidth(stext.replace(line[0]+line[1]+line[2]+line[3]+line[4]+line[5]+line[6]+line[7], ""));
   		line[9] = checkWidth(stext.replace(line[0]+line[1]+line[2]+line[3]+line[4]+line[5]+line[6]+line[7]+line[8], ""));
		int y = 142;
       	for(int i = 0; i < line.length;i++){
       		String ste = line[i];
       		if(ste == null)
       			continue;
       		stext = stext.replace(ste, "");
       		stext = stext.replace("/line/", "");

       		if(ste.indexOf("/quest:") != -1){
       			EntityPlayer player = WorldAPI.getPlayer();
       			String quest = ste.substring(ste.indexOf("/quest:"), ste.indexOf(":/"));
       			//if(!QuestHelper.get(player).isQuest( quest.replace("/quest:", "")))
       			//	QuestHelper.get(player).registerPlayer(QuestHelper.getDefaultQuest(quest.replace("/quest:", "")));
       			ste = ste.replace(quest+":/", "");
       		}
       		if(ste.indexOf("/drawtexture:") != -1)
       		{
       			String texture = ste.substring(ste.indexOf("/drawtexture:"), ste.indexOf(".png/"));
       			String[] s = texture.split(",");
				RenderAPI.drawTexture(new DrawTexture.Builder().setTexture(s[5]+".png").setAlpha(Float.parseFloat(s[0].replace("/drawtexture:", "")))
						.setXYAndSize(  Integer.parseInt(s[1]), Integer.parseInt(s[2]), Integer.parseInt(s[3]), Integer.parseInt(s[4])).build());
       			ste = ste.replace(texture+".png/", "").replace(".png", "");
       		}
       		if(ste.indexOf("/rotate:") != -1)
       		{
       			String texture = ste.substring(ste.indexOf("/rotate:"), ste.indexOf(":ro/"));
    			this.yaw = -10000;
    			this.pitch = -10000; 

       			if(texture.split(":")[1].equals("player")){
       				mob2.getLookHelper().setLookPositionWithEntity(WorldAPI.getPlayer(), (float)mob2.getHorizontalFaceSpeed(), (float)mob2.getVerticalFaceSpeed());
       				mob2.getLookHelper().onUpdateLook();
       			}else{
       				int rotate = Integer.parseInt(texture.split(":")[1].split(",")[0]);
       				int pitch = Integer.parseInt(texture.split(":")[1].split(",")[1]);
       				this.yaw = rotate;
       				this.pitch = pitch;
       				mob2.rotationYawHead = rotate;;
       				mob2.rotationPitch=pitch;
       			}
       			ste = ste.replace(texture, "").replace(":ro/", "");
       			look = true;
       		}
       		if(ste.indexOf("/chatset:") != -1){
       			String[] chatset = ste.substring(ste.indexOf("/chatset:"), ste.indexOf(":chat/")).split(",");
       			int line1 = Integer.parseInt(chatset[0]);
       			String type = chatset[1];
       			String text = null;
       			
       			if(chatset.length > 2)
       				text = chatset[2];
       			
       			if(type.equals("change")){
       				try {
						CommandChat.changeChatLine(line1, text);
					} catch (Exception e) {
						e.printStackTrace();
					}
       			}
     			if(type.equals("delete")){
       				try {
       					CommandChat.deleteChatLine(line1);
					} catch (Exception e) {
						e.printStackTrace();
					}
       			}
       			if(type.equals("add")){
       				try {
       					CommandChat.addChatLine(line1, text);
					} catch (Exception e) {
						e.printStackTrace();
					}
       			}
       		}
       		if(ste.indexOf("/move:") != -1 && ste.indexOf(":mo/") != -1)
       		{
       			String move = ste.substring(ste.indexOf("/move:"), ste.indexOf(":mo/"));
    			this.x = -10000;
    			this.y = -10000;
    			this.z = -10000;
    			
       			if(move.split(":")[1].equals("player")){
       			}else{
       				double px = Double.valueOf(move.split(":")[1].split(",")[0]);
       				double py = Double.valueOf(move.split(":")[1].split(",")[1]);
       				double pz = Double.valueOf(move.split(":")[1].split(",")[2]);
       				this.x = px;
       				this.y = py;
       				this.z = pz;
       			}
       			ste = ste.replace(move, "").replace(":mo/", "");
       			this.move = true;
       		}

			RenderAPI.drawString(ste, 95, y, 0xFFFFFF);
       		y+=10;
       	}
	}
	
	public static void addText(String text, EntityMob mob) {
		if (!end) {
			textList.add(text);
			mob2 = mob;
		}

	}

	
	public static void end() {
		end = true;
		
	}

	public static boolean isTextEnd() {
		return textList.size() == 0;
	}

}
