
package oneline.text;

import oneline.api.RenderAPI;
import oneline.api.WorldAPI;
import oneline.effect.AbstractTick;
import oneline.effect.TickRegister;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.gameevent.TickEvent.Type;

import java.util.ArrayList;
import java.util.HashMap;

public class Monologue {
	private Minecraft mc = Minecraft.getMinecraft();
	protected HashMap<Integer, MonologueText> mText = new HashMap<>();
	protected boolean monoEnd = false;// �ٸ� ���Ǿ��� ��ȭ�ϴ� ��츦 �����ϱ� ����
	protected int page;// ���°
	
	protected void next(){
		page ++;
		tick();
	}

	public Monologue addText(int page, StringBuffer... s){
		for(StringBuffer text : s){
			addText(page, text.toString());
		}
		return this;
	}
	public Monologue addText(int page, String s){
		if(!monoEnd){
			if(!mText.containsKey(page))
				mText.put(page, new MonologueText());
			mText.get(page).textList.add(s);
			mText.get(page).line.add(new StringBuffer());
			mText.get(page).lineRenderList.add(false);
		}
		return this;
	}
	protected String checkMonologueWidth(String text){
		if(text == null || text.equals("")){
			return "";
		}
		if(mc.fontRendererObj.getStringWidth(text) <= 40){
			return text;
		}
		for(String i = text;mc.fontRendererObj.getStringWidth(i) >= 30;i = i.substring(0, i.length()-1)){
	
			if(mc.fontRendererObj.getStringWidth(i) <= 40){
				return i;
			}
		}
		return "";
	}
	protected void monologueChange(String stext, int width, int height){
		if(stext == null)
			return;
   		stext = stext.replace("/playername/", WorldAPI.getPlayer().getName());
   		int stringWidth = mc.fontRendererObj.getStringWidth(stext);
		int y = 22;

		for(int i = 0; i < getMonoText().lineRenderList.size();i++){
			boolean lineRender = getMonoText().lineRenderList.get(i);
			String string = getMonoText().line.get(i).toString();
			if(!lineRender)
				continue;
			y+=i*10;
			RenderAPI.drawString(string, 45, y, 0xFFFFFF);
		}

	}
	
	protected void tick(){
		TickRegister.register(new AbstractTick(Type.RENDER, 10, true) {
			private int monoLine;
			@Override
			public boolean stopCondition() {
				return !(mc.currentScreen instanceof GuiMonologue) || getMonoText() == null || getMonoText().textList.get(monoLine) == null;
			}
			@Override
			public void run(Type type) {
				StringBuffer buffer = getMonoText().line.get(monoLine);
				String monoStr = getMonoText().textList.get(monoLine);
				System.out.println("TICK "+buffer.toString()+monoStr);
				System.out.println("TICK2 "+getMonoText().textList.size());

				if(monoStr.length() > absRunCount){//단어를 한글자씩 추가함
					getMonoText().lineRenderList.set(monoLine, true);
					buffer.append(monoStr.substring(absRunCount, absRunCount+1));
					getMonoText().line.set(monoLine, buffer);
				}else {
					monoLine++;
					absRunCount = 0;
				}
				if(stopCondition()) {
					textRenderComplete = true;
				}
			}
		});
	}
	protected boolean pause = false, dark = true, textRenderComplete;
	public Monologue setPause(boolean on){
		pause = on;
		return this;
	}
	public Monologue setDark(boolean on){
		dark = on;
		return this;
	}
	public boolean hasNextPage() {
		return getTextPage().size() >= page;
		
	}
	public boolean isLastPage() {
		return getTextPage().size() <= page;
	}
	public HashMap<Integer, MonologueText> getTextPage(){
		return mText;
	}
	public MonologueText getMonoText(){
		return mText.get(page);
	}
	public void start(){
		Minecraft.getMinecraft().displayGuiScreen(new GuiMonologue(this));
		monoEnd = true;
		tick();
	}
	
	/**
	 * @author oprond
	 * 페이지에 표시할 내용을 담는 클래스로 보임
	 * textList에는 전체 내용을 담고
	 * line에는 표시할 내용을 담음
	 * lineRenderList에는 텍스트를 표시하게 만듬
	 * 
	 */
	public class MonologueText {
		protected ArrayList<String> textList = new ArrayList<>();//줄 전체 내용
		protected ArrayList<StringBuffer> line = new ArrayList<>();//현재 줄
		protected ArrayList<Boolean> lineRenderList = new ArrayList<>();//줄을 숨겨놨다가 true로 되면 표시함
		
		public ArrayList<String>  getAllText() {
			return textList;
		}
		
		public boolean isEmpty() {
			return textList.size() == 0;
		}
		public String getAllText(int line) {
			return textList.get(line);
		}

		public String getText(int line) {
			return textList.get(line);
		}
	}


}
