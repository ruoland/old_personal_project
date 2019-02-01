package cmplus.cm.v18.customgui;

import cmplus.cm.v18.customgui.GuiCustom.CustomTextField;
import cmplus.cm.v18.customgui.GuiCustom.GuiCusButton;
import cmplus.cm.v18.function.VAR;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CustomGUITool {
	private Minecraft mc = Minecraft.getMinecraft();
	private boolean editMode = false;
	public static CustomGUITool instance;
	private ResourceLocation texture;
	private String name;
	private Configuration config;
	public CustomGUITool(String name) {
		this.instance = this;
		this.name = name;
		config = CustomClient.addConfig(name);
		nameField = new CustomTextField(20, 10, 140, 20).but();
		textureField = new CustomTextField(20, 40, 200, 20).but();
		widthField = new CustomTextField(20, 70, 40, 20).but();
		xyField = new CustomTextField(20, 100, 40, 20).but();
		functionField = new CustomTextField(20, 130, 40, 20).but();
		texWidthF = new CustomTextField(20, 40, 40, 20).tex();
		texXyF = new CustomTextField(20, 70, 80, 20).tex();
		string = new CustomTextField(20, 10, 140, 20).str();
		strXy = new CustomTextField(20, 40, 140, 20).str();
		texWidthF.setText("100,100");
	}
	public ArrayList<GuiString> stringList = new ArrayList<GuiString>();
	public ArrayList<GuiTexture> textureList = new ArrayList<GuiTexture>();
	private int selectButton = -1, selectTexture = -1, selectString = -1;
	private GuiCusButton textureButton = new GuiCusButton(0, 20, 10, "이미지 파일 선택");
	private GuiTextField nameField, widthField, xyField, functionField, textureField;// 이름 바꾸는 텍스트 필드,
	private GuiTextField texWidthF, texXyF;// 텍스쳐 설정 필드 - 이름 바꾸는 텍스트 필드, Width, Height
	private GuiTextField string, strXy;

	public void setEditMode() {
		if(!getButtonList().contains(textureButton)){
			this.getButtonList().add(textureButton);
		}
		textureButton.visible = false;
		if (!editMode) {
			getConfig(name).load();
		}
		isOnField = true;
		editMode = !editMode;
		selectButton = -1;
		selectTexture = -1;
		selectString = -1;
		this.fieldAllEnable(false);
		this.textureEnable(false);
		this.stringEnable(false);
		configsave();
	}
	
	public void initGui(int width, int height) {
		this.textureList.clear();
		fieldAllEnable(false);
		textureEnable(false);
		stringEnable(false);
		// button.visible = false;
		buttonSetting();
		textureSetting();

	}
	private GuiCusButton selBut(){
		return getButtonID(selectButton);
	}
	
	/**
	 * 현재 선택된 버튼의 넓이와 높이를 가져옵니다
	 */
	public String getSelBut(){
		return ""+selBut().width+","+selBut().height;
	}
	
	/**
	 * 인자에 있는 버튼의 넓이와 높이를 가져옵니다
	 */
	public String getButWidHei(GuiCusButton b){
		return ""+b.width+","+b.height;
	}
	public void drawScreen(int width, int height) {
		if (isButton())
		{
			updateButton();
		}
		if (isTexture())// 텍스쳐 수정 모드
		{
			getTextureID(selectTexture).width = ""+selTexW();
			getTextureID(selectTexture).height = ""+selTexH();
			getTextureID(selectTexture).x = ""+selTexX();
			getTextureID(selectTexture).y =""+ selTexY();
		}
		if(isString()){
			getStringID(selectString).str = ""+selStr();
			getStringID(selectString).x = ""+selStrX();
			getStringID(selectString).y = ""+selStrY();
		}
		if (editMode) // 에딧 모드가 해제되거나 버튼을 선택하지 않으면 이름 변경 칸도 숨김
		{
			String select = null;
			String selectType= null;
			if(selectButton != -1){
				select = ""+selectButton;
				selectType = "버튼";
			}
			if(selectString != -1){
				select = ""+selectString;
				selectType = "문자";

			}
			if(selectTexture != -1){
				select = ""+selectTexture;
				selectType = "이미지";
			}
			
			mc.fontRendererObj.drawString("EditMode"+"선택된 "+selectType+" ID:"+select, 0, 0, 0xFF0000);

		}

		for (GuiTextField f : CustomTextField.fieldList) {
			f.drawTextBox();
		}
		for (GuiTextField f : CustomTextField.textureFieldList) {
			f.drawTextBox();
		}
		for (GuiTextField f : CustomTextField.strFieldList) {
			f.drawTextBox();
		}
		for (GuiTexture g : textureList) {
			if (g.visible){
				g.renderTexture();
			}
		}
		for (GuiString g : stringList) {
			if (g.visible)
				g.renderString();
		}
	}

	/**
	 * 버튼을 계속 갱신하는 메서드
	 */
	public void updateButton(){
		selBut().displayString = nameField.getText();
		selBut().width = selButWidth();
		selBut().height = selButHeight();
		selBut().command = functionField.getText();
		selBut().xPosition = selButX();
		selBut().yPosition = selButY();
	}

	public void actionPerformed(GuiCusButton button){
		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && button.id == this.textureButton.id){
			JFileChooser file = new JFileChooser();
			file.setCurrentDirectory(new File(System.getProperty("user.home") + "//" + "Desktop"));
			file.setFileFilter(new FileNameExtensionFilter("PNG 파일", "png"));
			file.showOpenDialog(null);
			if(file.getSelectedFile() == null)
				return;
			this.texture = CMResourcePack.copyFile(file.getSelectedFile());
		}
	}
	public void mouseClicked(int p_73864_1_, int p_73864_2_, int p_73864_3_) {
		if(CustomTextField.mouse(p_73864_1_, p_73864_2_, p_73864_3_))
			return;
		
		if (isTexture())
			getTextureID(selectTexture).resourceLocation = texture;
		if (isButton())
			selBut().buttonTextures = new ResourceLocation(textureField.getText());
	
		if (editMode) {
			if (p_73864_3_ == 0 && isOnField) {
				for (int l = 0; l < getButtonList().size(); ++l) {
					GuiCusButton guibutton = (GuiCusButton) getButtonList().get(getButtonList().size() - 1 - l);
					if (guibutton.mousePressed(this.mc, p_73864_1_, p_73864_2_)) {
						fieldAllEnable(true);
						textureEnable(false);
						stringEnable(false);
						setSelectButton(guibutton.id);
						nameField.setText(guibutton.displayString);
						widthField.setText(getButWidHei(guibutton));
						textureField.setText(String.valueOf(guibutton.buttonTextures));
						return;
					}
				}
				for (int i = 0; i < textureList.size(); i++) {
					GuiTexture g = textureList.get(textureList.size() - 1 - i);
					if (g.mousePressed(mc, p_73864_1_, p_73864_2_) && g.visible) {
						fieldAllEnable(false);
						textureEnable(true);
						stringEnable(false);
						texWidthF.setText(g.width+","+g.height);
						setSelectTexture(g.id);

						return;
					}
				}
				for (int i = 0; i < stringList.size(); i++) {
					GuiString g = stringList.get(stringList.size() - 1 - i);
					if (g.mousePressed(mc, p_73864_1_, p_73864_2_) && g.visible) {
						fieldAllEnable(false);
						textureEnable(false);
						stringEnable(true);
						string.setText(g.str);
						strXy.setText(g.x+","+g.y);
						setSelectString(g.id);

						return;
					}
				}
				if(nameField.getVisible() && selectButton != -1) {
					selBut().width = selButWidth();
					selBut().height = selButHeight();
					selBut().buttonTextures = new ResourceLocation(textureField.getText());
					return;
				}
				if (!nameField.getVisible() && !texXyF.getVisible()) {
					selectButton = -1;// 단순히 배경만 눌렀다면
					selectTexture = -1;// 선택한 것들을 제거한다
					fieldAllEnable(false);
					textureEnable(false);
				}
			}
		}
	
	}

	protected void mouseClickMove(int p_146273_1_, int p_146273_2_, int p_146273_3_, long p_146273_4_) {
		if (editMode){
			if(selectButton != -1) {
				selBut().xPosition = p_146273_1_;
				selBut().yPosition = p_146273_2_;
				xyField.setText(p_146273_1_+","+p_146273_2_);
				widthField.setText(getSelBut());
			}
			if (selectTexture != -1) {
				getTextureID(selectTexture).x = ""+p_146273_1_;
				getTextureID(selectTexture).y = ""+p_146273_2_;
				texXyF.setText(p_146273_1_+","+p_146273_2_);
			}
			if (selectString != -1) {
				strXy.setText(p_146273_1_+","+p_146273_2_);
			}
		}
	}
	
	public void fieldAllEnable(boolean enable) {
		for (GuiTextField f : CustomTextField.fieldList) {
			f.setEnabled(enable);
			f.setVisible(enable);
		}
	}
	public void stringEnable(boolean enable) {
		for (GuiTextField f : CustomTextField.strFieldList) {
			f.setEnabled(enable);
			f.setVisible(enable);
		}
	}
	public void textureEnable(boolean enable) {
		for (GuiTextField f : CustomTextField.textureFieldList) {
			f.setEnabled(enable);
			f.setVisible(enable);
		}
		this.textureButton.visible = enable;
		
	}
	boolean isOnField = true;//쉬프트 키를 누르면 텍스트필드가 안나오게 만듬
	public void keyTyped(char p_73869_1_, int p_73869_2_) {
		if(Keyboard.isKeyDown(56) && Keyboard.isKeyDown(Keyboard.KEY_E))
			setEditMode();
		
		if (Keyboard.isKeyDown(56) && editMode) {
			if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
				isOnField = !isOnField;
				fieldAllEnable(isOnField);
				textureEnable(isOnField);
				stringEnable(isOnField);
				System.out.println(isOnField);
			}
	
			if(Keyboard.isKeyDown(Keyboard.KEY_C) && texXyF.getVisible())
				this.addTexture();
		
			if(Keyboard.isKeyDown(Keyboard.KEY_X)){
				textureEnable(true);
				fieldAllEnable(false);
				stringEnable(false);
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_S)){
				stringEnable(true);
				textureEnable(false);
				fieldAllEnable(false);
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_D)){
				String i = ""+(Mouse.getEventX() * mc.currentScreen.width / this.mc.displayWidth);
				String j = ""+(mc.currentScreen.height - Mouse.getEventY() * mc.currentScreen.height / this.mc.displayHeight - 1);
				this.stringList.add(new GuiString(this.stringList.size(), string.getText(), i, j));
			}
			
			if(Keyboard.isKeyDown(Keyboard.KEY_B)){
				int i = Mouse.getEventX() * mc.currentScreen.width / this.mc.displayWidth;
				int j = mc.currentScreen.height - Mouse.getEventY() * mc.currentScreen.height / this.mc.displayHeight - 1;
				GuiCusButton b = new GuiCusButton(this.getButtonList().size(), i, j, "이름없음");
				this.getButtonList().add(b);
			}
		}
		CustomTextField.key(p_73869_1_, p_73869_2_);

	}

	public void buttonSetting() {
		GuiCustom m = (GuiCustom) mc.currentScreen;
		int size = config.get("Button", "size", 0).getInt();
		for (int i = 0; i < size; i++) {
			if(config.hasCategory("b"+i)){
				int bid = get("b"+i, "ButtonID", 0).getInt();
				String id = "b"+i;
				int CuWidth = get(id, "CuWidth", m.width).getInt();
				int CuHeight = get(id, "CuHeight", m.height).getInt();
				if (m.width != CuWidth)
					CuWidth = m.width;
				if (m.height != CuHeight)
					CuHeight = m.height;
				int i2 = CuHeight / 4 + 48;
				int xPosition = CuWidth / 2 + get(id, "xPosition", 0).getInt();
				int yPosition = i2 + get(id, "yPosition",0).getInt();
				int width = get(id, "Width", 0).getInt();
				int height = get(id, "Height", 0).getInt();
				String displayString = get(id, "Button", "").getString();
				String command = get(id, "Command", "").getString();
				boolean visible = get(id, "Visible", true).getBoolean();
				ResourceLocation buttonTextures = new ResourceLocation(get(id, "Texture", "").getString());
				GuiCusButton b = new GuiCusButton(bid, xPosition, yPosition, width, height, displayString, command);
				b.visible = visible;
				b.buttonTextures = buttonTextures;
				this.getButtonList().add(b);
			}else continue;
		}
	}

	public void addTexture() {
		String i = ""+(Mouse.getEventX() * mc.currentScreen.width / this.mc.displayWidth);
		String j = ""+(mc.currentScreen.height - Mouse.getEventY() * mc.currentScreen.height / this.mc.displayHeight - 1);
		this.textureList.add(new GuiTexture(this.textureList.size(), texture, i, j,
				""+texWidthF.getText().split(",")[0], ""+texWidthF.getText().split(",")[1]));
	}
	public GuiString getStringID(int id) {
		for (Object b : this.stringList) {
			GuiString button = (GuiString) b;
			if (button.id == id)
				return (GuiString) b;
		}
		return null;
	}
	public GuiTexture getTextureID(int id) {
		for (Object b : this.textureList) {
			GuiTexture button = (GuiTexture) b;
			if (button.id == id)
				return (GuiTexture) b;
		}
		return null;
	}

	public List<GuiButton> getButtonList() {
		return ((GuiCustom) mc.currentScreen).getButton();
	}

	public GuiCusButton getButtonID(int id) {
		for (Object b : getButtonList()) {
			GuiCusButton button = (GuiCusButton) b;
			if (button.id == id)
				return (GuiCusButton) b;
		}
		return null;
	}

	public Property get(int id, String w, boolean f) {
		return getConfig(name).get(String.valueOf(id), w, f);
	}

	public Property get(int id, String w, String f) {
		return getConfig(name).get(String.valueOf(id), w, f);
	}

	public Property get(int id, String w, int f) {
		return getConfig(name).get(String.valueOf(id), w, f);
	}
	public boolean isEditMode() {
		return editMode;
	}
	public static Configuration getConfig(String key){
		return CustomClient.getConfig(key);
	}
	public String selStr() {
		if (!string.getText().equals(""))
			return ""+ VAR.getStr(string.getText());
		else
			return string.getText();
	}

	public int selStrX() {
		String[] text = strXy.getText().split(",");
		if (text.length > 0 && !text[0].equals("") )
			return VAR.getInt(text[0]);
		else
			return Integer.valueOf(getStringID(selectString).x);
	}

	public int selStrY() {
		String[] text = strXy.getText().split(",");
		if (text.length > 1 && !text[1].equals(""))
			return VAR.getInt(text[1]);
		else
			return Integer.valueOf(getStringID(selectString).y);
	}
	public int selTexX() {
		String[] text = texXyF.getText().split(",");
		if (text.length > 0 && !text[0].equals("") )
			return VAR.getInt(text[0]);
		else
			return Integer.valueOf(getTextureID(selectTexture).x);
	}

	public int selTexY() {
		String[] text = texXyF.getText().split(",");
		if (text.length > 1 && !text[1].equals(""))
			return VAR.getInt(text[1]);
		else
			return Integer.valueOf(getTextureID(selectTexture).y);
	}
	

	public int selTexW() {
		String[] text = texWidthF.getText().split(",");
		if (text.length > 0 && !text[0].equals(""))
			return VAR.getInt(text[0]);
		else
			return Integer.valueOf(getTextureID(selectTexture).width);
	}

	public int selTexH() {
		String[] text = texWidthF.getText().split(",");
		if (text.length > 1 && !text[1].equals(""))
			return VAR.getInt(text[1]);
		else
			return Integer.valueOf(getTextureID(selectTexture).height);
	}
	
	public int selButX() {
		String[] text = xyField.getText().split(",");
		if (text.length > 0 && !text[0].equals("") )
			return VAR.getInt(text[0]);
		else
			return selBut().xPosition;
	}

	public int selButY() {
		String[] text = xyField.getText().split(",");
		if (text.length > 1 && !text[1].equals(""))
			return VAR.getInt(text[1]);
		else
			return selBut().yPosition;
	}
	public int selButWidth() {
		String[] text = widthField.getText().split(",");
		if (text.length > 0 && !text[0].equals(""))
			return VAR.getInt(text[0]);
		else
			return selBut().width;
	}

	public int selButHeight() {
		String[] text = widthField.getText().split(",");
		if (text.length > 1 && !text[1].equals(""))
			return VAR.getInt(text[1]);
		else
			return selBut().height;
	}
	public void setSelectButton(int a) {
		this.selectTexture = -1;
		this.selectButton = a;
		this.selectString = -1;
	}

	public void setSelectTexture(int a) {
		this.selectTexture = a;
		this.selectButton = -1;
		this.selectString = -1;

	}
	public void setSelectString(int a) {
		this.selectTexture = -1;
		this.selectButton = -1;
		this.selectString = a;
	}

	public boolean isTexture(){
		return editMode && selectTexture != -1;
	}

	public boolean isString(){
		return editMode && selectString != -1;
	}
	
	public boolean isButton(){
		return editMode && selectButton != -1;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public void configsave() {
		GuiCustom m = (GuiCustom) mc.currentScreen;
		int i2 = m.height / 4 + 48;
		for (Object b : getButtonList()) {
			GuiCusButton bu = (GuiCusButton) b;
			String string = bu.displayString;
			if (bu.visible) {
				string = string.replace("...", "");
				String id = "b"+String.valueOf(bu.id);
				config.get("Button", "size", this.getButtonList().size()).set(getButtonList().size());;
				if (getConfig(name).hasCategory(id)) {
					getConfig(name).addCustomCategoryComment(id, "버튼 정보");
					ConfigCategory c = getConfig(name).getCategory(id);
					c.get("ButtonID").setValue(bu.id);

					c.get("Button").setValue(string);
					c.get("xPosition").setValue(bu.xPosition - m.width / 2);
					c.get("yPosition").setDefaultValue( bu.yPosition).setValue(bu.yPosition - i2);
					c.get("Width").setDefaultValue(bu.width).setValue(bu.width);
					c.get("Height").setDefaultValue(bu.height).setValue(bu.height);
					try{
						c.get("Command").setDefaultValue("").setValue(bu.command == null ? "" : bu.command);
					}catch (Exception e) {
						get(id, "Command", "").setValue(bu.command == null ? "" : bu.command);
						e.printStackTrace();
					}
					c.get("CuWidth").setDefaultValue(m.width).setValue(m.width);
					c.get("CuHeight").setDefaultValue(m.height).setValue(m.height);
					c.get("Texture").setDefaultValue(bu.buttonTextures.toString())
							.set(bu.buttonTextures.toString());
					c.get("Visible").setDefaultValue(bu.visible).setValue(bu.visible);
				}else{
					ConfigCategory c = getConfig(name).getCategory(id);
					getConfig(name).addCustomCategoryComment(id, "버튼 정보");
					get(id, "ButtonID", bu.id).setValue(bu.id);

					get(id,"Button",string).set(string);
					get(id,"xPosition",bu.xPosition).setValue(bu.xPosition - m.width / 2);
					get(id,"yPosition",bu.yPosition).setValue(bu.yPosition - i2);
					get(id,"Width",bu.width).setValue(bu.width).setValue(bu.width);
					get(id,"Height",bu.height).setValue(bu.height);
					get(id, "Command", "").setValue(bu.command == null ? "" : bu.command);
					get(id,"CuWidth",m.width).setValue(m.width);
					get(id,"CuHeight",m.height).setValue(m.height).setValue(m.height);
					get(id,"Texture",bu.buttonTextures.toString()).setValue(bu.buttonTextures.toString());
					get(id, "Visible", bu.visible).set(bu.visible);
				}
				getConfig(name).save();

			}
		}

		for (int i = 0; i < textureList.size(); i++) {
			GuiTexture bu = (GuiTexture) textureList.get(i);
			if (bu.resourceLocation.toString().equals("") || bu.resourceLocation.toString().equals("minecraft:")) {
				textureList.remove(i);
				i--;
				continue;
			}
			if (getConfig(name).hasCategory("T" + String.valueOf(bu.id))) {
				getConfig(name).addCustomCategoryComment("T" + String.valueOf(bu.id), "텍스쳐");
				getConfig(name).get("T" + String.valueOf(bu.id), "Texture", bu.resourceLocation.toString())
					.set(bu.resourceLocation.toString());
				getConfig(name).get("T" + String.valueOf(bu.id), "xPosition", bu.x).set(bu.x);
				getConfig(name).get("T" + String.valueOf(bu.id), "yPosition", bu.y).set(bu.y);
				getConfig(name).get("T" + String.valueOf(bu.id), "Width", bu.width).set(bu.width);
				getConfig(name).get("T" + String.valueOf(bu.id), "Height", bu.height).set(bu.height);
				getConfig(name).get("T" + String.valueOf(bu.id), "Visible", bu.visible).set(bu.visible);
			}
			else {
				getConfig(name).addCustomCategoryComment("T" + String.valueOf(bu.id), "텍스쳐");
				getConfig(name).get("T" + String.valueOf(bu.id), "Texture", bu.resourceLocation.toString(), "기본값:"+ bu.resourceLocation.toString())
					.set(bu.resourceLocation.toString());
				getConfig(name).get("T" + String.valueOf(bu.id), "xPosition", bu.x, "기본값: "+bu.x).set(bu.x);
				getConfig(name).get("T" + String.valueOf(bu.id), "yPosition", bu.y, "기본값: "+bu.y).set(bu.y);
				getConfig(name).get("T" + String.valueOf(bu.id), "Width", bu.width, "기본값: "+bu.width).set(bu.width);
				getConfig(name).get("T" + String.valueOf(bu.id), "Height", bu.height, "기본값: "+bu.height).set(bu.height);
				getConfig(name).get("T" + String.valueOf(bu.id), "Visible", bu.visible).set(bu.visible);
			}
			// getConfig(name).save();
		}
		getConfig(name).addCustomCategoryComment("T", "텍스쳐 관련 설정");

		getConfig(name).get("T", "Size", textureList.size()).set(textureList.size());

		//getConfig(name).save();
	}
	public Property get(String category, String key, boolean defaults){
		return getConfig(name).get(category, key, defaults);
	}
	public Property get(String category, String key, int defaults){
		return getConfig(name).get(category, key, defaults);
	}
	public Property get(String category, String key, String defaults){
		return getConfig(name).get(category, key, defaults);
	}


	public void textureSetting() {
		Configuration config = getConfig(name);
		GuiTexture texture;
		int size = config.get("T", "Size", 0).getInt();
		for (int i = 0; i < size; i++) {
			String texturec = config.get("T" + i, "Texture", "").getString();
			String x = config.get("T" + i, "xPosition", 0).getDefault();
			String y = config.get("T" + i, "yPosition", 0).getDefault();
			String w = config.get("T" + i, "Width", 0).getDefault();
			String h = config.get("T" + i, "Height", 0).getDefault();
			boolean v = config.get("T" + i, "Visible", false).getBoolean();
			texture = new GuiTexture(i, texturec, x, y, w, h);
			texture.visible = v;
			this.textureList.add(texture);
		}
	}


	public String getName() {
		
		return name;
	}


}
