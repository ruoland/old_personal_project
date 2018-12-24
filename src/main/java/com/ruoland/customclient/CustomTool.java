package com.ruoland.customclient;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.Loader;
import net.montoyo.mcef.api.API;
import net.montoyo.mcef.api.IBrowser;
import net.montoyo.mcef.api.MCEFApi;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class CustomTool {

	public static CustomTool instance;

	private CustomTool() {
		instance = this;

		editName = new GuiTextField(1,mc.fontRendererObj, 20, 10, 140,20);
		texture = new GuiTextField(2,mc.fontRendererObj, 20, 40, 200,20);
		w = new GuiTextField(3,mc.fontRendererObj, 20, 70, 40,20);
		h = new GuiTextField(4,mc.fontRendererObj, 20, 100, 40,20);
		panorama = new GuiTextField(5,mc.fontRendererObj,  20, 10, 140,20);

		textureF = new GuiTextField(6,mc.fontRendererObj, 20, 10, 140, 20);
		wF = new GuiTextField(7,mc.fontRendererObj, 20, 40, 40,20);
		hF = new GuiTextField(8,mc.fontRendererObj, 20, 60, 40,20);
		textureF.setMaxStringLength(5000);

		texture.setMaxStringLength(5000);
		editName.setMaxStringLength(5000);
		panorama.setMaxStringLength(5000);
		this.fieldList.add(editName);
		this.fieldList.add(w);
		this.fieldList.add(h);
		this.fieldList.add(texture);
		this.fieldList.add(panorama);

		this.textureField.add(textureF);
		this.textureField.add(wF);
		this.textureField.add(hF);
	}

	private static final Minecraft mc = Minecraft.getMinecraft();
	private final ArrayList<GuiTextField> fieldList = new ArrayList<GuiTextField>();
	private final ArrayList<GuiTextField> textureField = new ArrayList<GuiTextField>();
	private GuiMainMenuRealNew mainmenu = (GuiMainMenuRealNew) mc.currentScreen;

	private boolean editMode = false;
	private ArrayList<Integer> removeList = new ArrayList<Integer>();
	public ArrayList<GuiTexture> textureList = new ArrayList<GuiTexture>();
	protected int selectTexture = -1, selectButton = -1;

	private GuiTextField panorama, editName, w, h, texture;// 이름 바꾸는 텍스트 필드,
	// Width, Height
	private GuiTextField textureF, wF, hF;// 이름 바꾸는 텍스트 필드, Width, Height
	private GuiCusButton onoff, fileFind;

	public void setEditMode() {
		if (!editMode) {
			CustomClient.config.load();
			splashVisible = CustomClient.config.getBoolean("Splash", "SplashVisible", true, "");
			x = CustomClient.config.get("Splash", "X", mainmenu.width / 2 + 90, "기본값: 323").getInt();
			y = CustomClient.config.get("Splash", "Y", 70, "기본값: 70").getInt();
		}
		editMode = !editMode;
		selectButton = -1;
		selectTexture = -1;

		this.fieldAllEnable(false);
		this.textureEnable(false);
		configsave();
	}

	public boolean isEditMode() {
		return editMode;
	}

	public void initGui() {
		this.textureList.clear();
		onoff = new GuiCusButton(200, 20, 50, 50, 20, "true");
		onoff.visible = false;
		fileFind = new GuiCusButton(10, 20, 120, 70, 20, "사진 선택");
		fileFind.visible = false;
		onoff.noEdit = true;
		fileFind.noEdit = true;
		mainmenu.getButton().add(onoff);
		mainmenu.getButton().add(fileFind);
		fieldAllEnable(false);
		textureEnable(false);
		// button.visible = false;

		buttonSetting();
		textureSetting();
		addTitle();

		this.backgroundImage = CustomClient.config
				.get("T", "Mainmenu", this.backgroundImage, "기본값: textures/gui/title/background/panorama_0.png")
				.getString();
		panorama.setText(backgroundImage);

	}

	public boolean splashVisible = true;
	int x, y;

	public void setSelectTextureField(ResourceLocation resourceLocation){
		this.textureF.setText(resourceLocation.toString());
		getTextureByID(selectTexture).resourceLocation = resourceLocation;
	}

	/**
	 * 다이나믹 텍스쳐로 사진 설정시 minecraft:dynamic/텍시쳐이름 이렇게 됨
	 * 이렇게 되면 껐다키면 사진을 불러올 수 없음
	 */
	public void setDynamicTextureField(ResourceLocation resourceLocation, String textureField){
		this.textureF.setText(textureField);
		if(selectTexture != -1) {
			getTextureByID(selectTexture).resourceLocation = resourceLocation;
			getTextureByID(selectTexture).dynamicLocation = resourceLocation;
		}
	}

	public void setDynamicButtonField(ResourceLocation resourceLocation, String buttonField){
		this.texture.setText(buttonField);
		if(selectButton != -1) {
			getButtonByID(selectButton).buttonTextures = resourceLocation;
			getButtonByID(selectButton).dynamicLocation = resourceLocation;
		}
	}
	public void drawScreen(int width, int height) {
		if (editMode && selectButton != -1 && editName.getText() != null)// 버튼 수정 업데이트
		{
			getButtonByID(selectButton).displayString = editName.getText();
			if (w.getText().equals(""))
				w.setText(String.valueOf(getButtonByID(selectButton).width));
			if (h.getText().equals(""))
				h.setText(String.valueOf(getButtonByID(selectButton).height));
			getButtonByID(selectButton).width = Integer.valueOf(w.getText());
			getButtonByID(selectButton).height = Integer.valueOf(h.getText());
		}

		if (editMode && selectTexture != -1 && editName.getText() != null)// 텍스쳐 수정 업데이트
		{
			if (wF.getText().equals(""))
				wF.setText(String.valueOf(getTextureByID(selectTexture).width));
			if (hF.getText().equals(""))
				hF.setText(String.valueOf(getTextureByID(selectTexture).height));
			getTextureByID(selectTexture).width = Integer.valueOf(wF.getText());
			getTextureByID(selectTexture).height = Integer.valueOf(hF.getText());
			//텍스쳐를 변경하는 코드는 mouseClicked에 있음
			//drawScreen에서 새로운 객체를 생성하면 부담이 큼
		}
		for (GuiTexture g : textureList) {
			if (g.visible)
				g.renderTexture();
		}
		if (!editMode) {// 에딧 모드가 해제되거나 버튼을 선택하지 않으면 이름 변경 칸도 숨김
			fieldAllEnable(false);
			textureEnable(false);
		}
		if (editMode) {
			mc.fontRendererObj.drawString("수정 모드", 0, 0, 0x000000);
		}

		for (GuiTextField f : fieldList) {
			GL11.glPushMatrix();
			GL11.glTranslatef(0, 0, 2);
			f.drawTextBox();
			GL11.glPopMatrix();
		}
		for (GuiTextField f : textureField) {
			GL11.glPushMatrix();
			GL11.glTranslatef(0, 0, 2);
			f.drawTextBox();
			GL11.glPopMatrix();

		}

		if (splashVisible) {
			GL11.glPushMatrix();
			GL11.glTranslatef((float) (x), (float) y, 0.0F);
			GL11.glRotatef(-20.0F, 0.0F, 0.0F, 1.0F);
			float f1 = 1.8F - MathHelper
					.abs(MathHelper.sin((float) (Minecraft.getSystemTime() % 1000L) / 1000.0F * (float) Math.PI * 2.0F)
							* 0.1F);
			f1 = f1 * 100.0F / (float) (mc.fontRendererObj.getStringWidth(mainmenu.getSplashText()) + 32);
			GL11.glScalef(f1, f1, f1);
			mainmenu.drawCenteredString(mc.fontRendererObj, mainmenu.getSplashText(), 0, -8, -256);
			GL11.glPopMatrix();
		}
	}

	public boolean isGradient() {
		return this.onoff.displayString.equals("true");
	}

	public String backgroundImage = "textures/gui/title/background/panorama_0.png";
	public String dynamicBackgroundImage = "textures/gui/title/background/panorama_0.png";

	public void setDynamicBackgroundImage(String dynamic, String backgroundField){
		backgroundImage = dynamic;
		dynamicBackgroundImage = dynamic;
		panorama.setText(backgroundField);
	}
	private void setSelectB(int a) {
		this.selectTexture = -1;
		this.selectButton = a;
	}

	private void setSelectT(int a) {
		this.selectTexture = a;
		this.selectButton = -1;
	}

	public void button(GuiButton button, boolean on){
		button.visible = on;
		button.enabled = on;
	}

	public void mouseClicked(int p_73864_1_, int p_73864_2_, int p_73864_3_) {
		if (isEditMode() && selectTexture != -1)
			getTextureByID(selectTexture).resourceLocation = new ResourceLocation(textureF.getText());//텍스쳐 업데이트
		if (isEditMode() && selectButton != -1)
			getButtonByID(selectButton).buttonTextures = new ResourceLocation(texture.getText());//버튼 텍스쳐 업데이트
		panorama.setEnabled(false);
		panorama.setVisible(false);
		for (GuiTextField f : fieldList) {
			f.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
		}
		for (GuiTextField f : textureField) {
			f.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
			boolean flag = p_73864_1_ >= f.xPosition && p_73864_1_ < f.xPosition + f.width && p_73864_2_ >= f.yPosition
					&& p_73864_2_ < f.yPosition + f.height;
			if (f.getVisible() && flag)
				return;
		}
		if (editMode && p_73864_3_ == 0) {
			for (int l = 0; l < getButtonList().size(); ++l) {//버튼 클릭 메서드
				GuiCusButton guibutton = (GuiCusButton) getButtonList().get(getButtonList().size() - 1 - l);
				if (guibutton.noEdit)
					continue;
				if (guibutton.mousePressed(this.mc, p_73864_1_, p_73864_2_)) {
					setSelectB(guibutton.id);
					fieldAllEnable(true);
					textureEnable(false);
					editName.setText(guibutton.displayString);
					w.setText(String.valueOf(guibutton.width));
					h.setText(String.valueOf(guibutton.height));
					texture.setText(String.valueOf(guibutton.buttonTextures));
					fileFind.visible = true;
					fileFind.enabled = true;
					return;
				}
			}
			for (int i = 0; i < textureList.size(); i++) {
				GuiTexture g = textureList.get(textureList.size() - 1 - i);
				if (g.mousePressed(p_73864_1_, p_73864_2_) && g.visible) {

					setSelectT(g.id);
					fieldAllEnable(false);
					textureEnable(true);
					textureF.setText(g.resourceLocation.toString());
					wF.setText(String.valueOf(g.width));
					hF.setText(String.valueOf(g.height));
					textureF.setVisible(true);
					return;
				}
			}
		}
		if (editMode && editName.getVisible() && selectButton != -1) {
			getButtonByID(selectButton).width = Integer.parseInt(w.getText());
			getButtonByID(selectButton).height = Integer.parseInt(h.getText());
			getButtonByID(selectButton).buttonTextures = new ResourceLocation(texture.getText());
			return;
		}
		if (editMode && !editName.getVisible() && !textureF.getVisible()) {
			selectButton = -1;// 단순히 배경만 눌렀다면
			selectTexture = -1;// 선택한 것들을 제거한다
			backgroundImage = panorama.getText();

			System.out.println("배경 선택함"+textureF);
			fieldAllEnable(false);
			textureEnable(false);
			panorama.setEnabled(true);
			panorama.setVisible(true);
			panorama.setText(backgroundImage);
			this.onoff.visible = true;
			this.onoff.enabled = true;
			this.fileFind.visible  = true;
			this.fileFind.enabled = true;

		}
	}

	protected boolean isBackgroundEdit(){
		return onoff.visible;
	}

	protected void mouseClickMove(int p_146273_1_, int p_146273_2_, int p_146273_3_, long p_146273_4_) {
		if (editMode && selectButton != -1) {
			getButtonByID(selectButton).xPosition = p_146273_1_;
			getButtonByID(selectButton).yPosition = p_146273_2_;
			w.setText(String.valueOf(getButtonByID(selectButton).width));
			h.setText(String.valueOf(getButtonByID(selectButton).height));
		}
		if (editMode && selectTexture != -1) {
			getTextureByID(selectTexture).x = p_146273_1_;
			getTextureByID(selectTexture).y = p_146273_2_;
		}

	}

	public void keyTyped(char p_73869_1_, int p_73869_2_) {
		if (!(mc.currentScreen instanceof GuiMainMenuRealNew)) {
			return;
		}
		for (GuiTextField f : fieldList) {
			f.textboxKeyTyped(p_73869_1_, p_73869_2_);
		}
		for (GuiTextField f : textureField) {
			f.textboxKeyTyped(p_73869_1_, p_73869_2_);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_E) && Keyboard.isKeyDown(56)) {
			setEditMode();
		}
		if (isEditMode() && Keyboard.isKeyDown(Keyboard.KEY_DELETE)
				&& (Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))) {
			if (selectButton != -1) {
				getButtonByID(selectButton).visible = false;
				removeList.add(selectButton);
			}
			if (selectTexture != -1) {
				getTextureByID(selectTexture).visible = false;
			}
			System.out.println("Delete");
		}
		if (isEditMode() && (Keyboard.isKeyDown(56) && Keyboard.isKeyDown(Keyboard.KEY_C))) {
			if (textureF.getVisible()) {
				if (!textureF.getText().equals("")) {
					this.addTexture();
				}
			}
		}
		if (isEditMode() && (Keyboard.isKeyDown(56) && Keyboard.isKeyDown(Keyboard.KEY_X))) {
			textureEnable(true);
			fieldAllEnable(false);
			setSelectT(-1);
			setSelectB(-1);
		}
		if (isEditMode() && (Keyboard.isKeyDown(29) && Keyboard.isKeyDown(44))) {
			if (removeList.size() != 0) {
				getButtonByID(removeList.get(removeList.size() - 1)).visible = true;
				removeList.remove(removeList.size() - 1);
			}
		}
	}

	public void fieldAllEnable(boolean enable) {
		for (GuiTextField f : fieldList) {
			f.setEnabled(enable);
			f.setVisible(enable);
		}
		panorama.setEnabled(false);
		panorama.setVisible(false);
		this.onoff.visible = false;
		this.onoff.enabled = false;
	}

	public void textureEnable(boolean enable) {
		for (GuiTextField f : textureField) {
			f.setEnabled(enable);
			f.setVisible(enable);
		}
		fileFind.visible = enable;
		fileFind.enabled = enable;
	}

	public void configsave() {
		int i2 = mainmenu.height / 4 + 48;
		for (Object b : getButtonList()) {
			GuiCusButton bu = (GuiCusButton) b;
			String string = null;
			if (bu.displayString.equals("") || bu.displayString == null) {
				string = "Language";
			} else
				string = bu.displayString;
			if (bu.visible) {
				string = string.replace("...", "");
				if (CustomClient.config.hasCategory(String.valueOf(bu.id))) {
					CustomClient.config.addCustomCategoryComment(String.valueOf(bu.id), "버튼 정보");
					ConfigCategory c = CustomClient.config.getCategory(String.valueOf(bu.id));
					c.get("Button").setValue(string);

					c.get("xPosition").setValue(bu.xPosition - mainmenu.width / 2);
					c.get("yPosition").setDefaultValue( bu.yPosition).setValue(bu.yPosition - i2);
					c.get("Width").setDefaultValue(bu.width).setValue(bu.width);
					c.get("Height").setDefaultValue(bu.height).setValue(bu.height);
					c.get("CuWidth").setDefaultValue(mainmenu.width).setValue(mainmenu.width);
					c.get("CuHeight").setDefaultValue(mainmenu.height).setValue(mainmenu.height);
					c.get("Texture").setDefaultValue(bu.buttonTextures.toString())
							.set(bu.buttonTextures.toString());
					c.get("Visible").setDefaultValue(bu.visible).setValue(bu.visible);
				}else{
					String id = String.valueOf(bu.id);
					ConfigCategory c = CustomClient.config.getCategory(id);
					CustomClient.config.addCustomCategoryComment(id, "버튼 정보");
					get(id,"Button",string).set(string);
					get(id,"xPosition",bu.xPosition).setValue(bu.xPosition - mainmenu.width / 2);
					get(id,"yPosition",bu.yPosition).setValue(bu.yPosition - i2);
					get(id,"Width",bu.width).setValue(bu.width).setValue(bu.width);
					get(id,"Height",bu.height).setValue(bu.height);
					get(id,"CuWidth",mainmenu.width).setValue(mainmenu.width);
					get(id,"CuHeight",mainmenu.height).setValue(mainmenu.height).setValue(mainmenu.height);
					get(id,"Texture",bu.buttonTextures.toString()).setValue(bu.buttonTextures.toString());
					get(id, "Visible", bu.visible).set(bu.visible);
				}
				CustomClient.config.save();

			}
		}

		for (int i = 0; i < textureList.size(); i++) {
			GuiTexture bu = (GuiTexture) textureList.get(i);
			if (bu.resourceLocation.toString().equals("") || bu.resourceLocation.toString().equals("minecraft:")) {
				textureList.remove(i);
				i--;
				continue;
			}
			if (CustomClient.config.hasCategory("T" + String.valueOf(bu.id))) {
				CustomClient.config.addCustomCategoryComment("T" + String.valueOf(bu.id), "텍스쳐");
				CustomClient.config.get("T" + String.valueOf(bu.id), "Texture", bu.resourceLocation.toString())
						.set(bu.resourceLocation.toString());
				CustomClient.config.get("T" + String.valueOf(bu.id), "xPosition", bu.x).set(bu.x);
				CustomClient.config.get("T" + String.valueOf(bu.id), "yPosition", bu.y).set(bu.y);
				CustomClient.config.get("T" + String.valueOf(bu.id), "Width", bu.width).set(bu.width);
				CustomClient.config.get("T" + String.valueOf(bu.id), "Height", bu.height).set(bu.height);
				CustomClient.config.get("T" + String.valueOf(bu.id), "Visible", bu.visible).set(bu.visible);
			}
			else {
				CustomClient.config.addCustomCategoryComment("T" + String.valueOf(bu.id), "텍스쳐");
				CustomClient.config.get("T" + String.valueOf(bu.id), "Texture", bu.resourceLocation.toString(), "기본값:"+ bu.resourceLocation.toString())
						.set(bu.resourceLocation.toString());
				CustomClient.config.get("T" + String.valueOf(bu.id), "xPosition", bu.x, "기본값: "+bu.x).set(bu.x);
				CustomClient.config.get("T" + String.valueOf(bu.id), "yPosition", bu.y, "기본값: "+bu.y).set(bu.y);
				CustomClient.config.get("T" + String.valueOf(bu.id), "Width", bu.width, "기본값: "+bu.width).set(bu.width);
				CustomClient.config.get("T" + String.valueOf(bu.id), "Height", bu.height, "기본값: "+bu.height).set(bu.height);
				CustomClient.config.get("T" + String.valueOf(bu.id), "Visible", bu.visible).set(bu.visible);
			}
			CustomClient.config.save();
			// CustomClient.config.save();
		}
		CustomClient.config.addCustomCategoryComment("T", "텍스쳐 관련 설정");

		CustomClient.config.get("T", "Size", textureList.size()).set(textureList.size());
		CustomClient.config.get("T", "Mainmenu", this.backgroundImage).set(this.backgroundImage);
		CustomClient.config.addCustomCategoryComment("M", "메인메뉴 설정");
		CustomClient.config.get("M", "onoff", this.onoff.displayString).set(this.onoff.displayString);

		CustomClient.config.save();
	}
	public Property get(String category, String key, boolean defaults){
		return CustomClient.config.get(category, key, defaults);
	}
	public Property get(String category, String key, int defaults){
		return CustomClient.config.get(category, key, defaults);
	}
	public Property get(String category, String key, String defaults){
		return CustomClient.config.get(category, key, defaults);
	}
	public GuiTexture getTextureByID(int id) {
		for (Object b : this.textureList) {
			GuiTexture button = (GuiTexture) b;
			if (button.id == id)
				return (GuiTexture) b;
		}
		return null;
	}

	public List<GuiButton> getButtonList() {
		GuiMainMenuRealNew mainMenuNew = (GuiMainMenuRealNew) mc.currentScreen;
		return mainMenuNew.getButton();
	}

	public GuiCusButton getButtonByID(int id) {
		for (Object b : getButtonList()) {
			GuiCusButton button = (GuiCusButton) b;
			if (button.id == id)
				return (GuiCusButton) b;
		}
		return null;
	}

	public Property get(int id, String w, boolean f) {
		return CustomClient.config.get(String.valueOf(id), w, f);
	}

	public Property get(int id, String w, String f) {
		return CustomClient.config.get(String.valueOf(id), w, f);
	}

	public Property get(int id, String w, int f) {
		return CustomClient.config.get(String.valueOf(id), w, f);
	}

	public void textureSetting() {
		Configuration config = CustomClient.config;
		GuiTexture texture;
		int size = config.get("T", "Size", 0).getInt();
		for (int i = 0; i < size; i++) {
			String texturec = config.get("T" + i, "Texture", "").getString();
			int x = config.get("T" + i, "xPosition", 0).getInt();
			int y = config.get("T" + i, "yPosition", 0).getInt();
			int w = config.get("T" + i, "Width", 0).getInt();
			int h = config.get("T" + i, "Height", 0).getInt();
			boolean v = config.get("T" + i, "Visible", false).getBoolean();
			texture = new GuiTexture(i, texturec, x, y, w, h);
			texture.visible = v;
			this.textureList.add(texture);
		}
	}

	public void addTitle() {

		Configuration config = CustomClient.config;
		// 1000, "customclient:textures/gui/title2.png", 77, 31, 257, 45)
		// 위에건 기본 값임, 절대 지우지 말 것

		int i = 1000;
		boolean v = config.get("T" + i, "Visible", true).getBoolean();
		String texturec = config.get("T" + i, "Texture", "customclient:textures/gui/title2.png",
				"기본값: customclient:textures/gui/title2.png").getString();
		int x = config.get("T" + i, "xPosition", 77, "기본값: 77").getInt();
		int y = config.get("T" + i, "yPosition", 31, "기본값: 31").getInt();
		int w = config.get("T" + i, "Width", 257, "기본값: 257").getInt();
		int h = config.get("T" + i, "Height", 45, "기본값: 45").getInt();
		GuiTexture texture = new GuiTexture(i, texturec, x, y, w, h);
		texture.visible = v;
		texture.x = x;
		texture.y = y;
		this.textureList.add(texture);
	}

	public void buttonSetting() {

		for (int i = 0; i < getButtonList().size(); i++) {
			GuiCusButton b = (GuiCusButton) getButtonList().get(i);
			if (CustomClient.config.hasCategory(String.valueOf(b.id))) {
				int CuWidth = get(b.id, "CuWidth", mainmenu.width).getInt();
				int CuHeight = get(b.id, "CuHeight", mainmenu.height).getInt();

				if (mainmenu.width != CuWidth)
					CuWidth = mainmenu.width;
				if (mainmenu.height != CuHeight)
					CuHeight = mainmenu.height;
				int i2 = CuHeight / 4 + 48;

				b.xPosition = CuWidth / 2 + get(b.id, "xPosition", b.xPosition).getInt();
				b.yPosition = i2 + get(b.id, "yPosition", b.yPosition).getInt();
			} else {
				continue;
			}
			b.width = get(b.id, "Width", b.width).getInt();
			b.height = get(b.id, "Height", b.height).getInt();
			b.displayString = get(b.id, "Button", b.displayString).getString();
			b.visible = get(b.id, "Visible", b.visible).getBoolean();
			b.buttonTextures = new ResourceLocation(get(b.id, "Texture", b.buttonTextures.toString()).getString());
		}
		this.onoff.displayString = CustomClient.config.get("M", "onoff", "true").getString();
	}

	public void addTexture() {
		int i = Mouse.getEventX() * mc.currentScreen.width / this.mc.displayWidth;
		int j = mc.currentScreen.height - Mouse.getEventY() * mc.currentScreen.height / this.mc.displayHeight - 1;
		if (check(wF) && check(hF))
			this.textureList.add(new GuiTexture(this.textureList.size(), textureF.getText(), i, j,
					Integer.parseInt(wF.getText()), Integer.parseInt(hF.getText())));
	}

	public boolean check(GuiTextField f) {
		try {
			Integer.parseInt(f.getText());
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	static IBrowser browser = null;

	public void drawBrowser(String url, int width, int height) {
		if (Loader.isModLoaded("mcef")) {
			if (browser == null) {
				API api = MCEFApi.getAPI();
				browser = api.createBrowser(
						url.replace("watch?v=", "embed/").replace("https", "http").replace("&feature=youtu.be", "")
								+ "?autoplay=1&autohide=1&controls=0&showinfo=0&rel=0",
						false);
				browser.resize(mc.displayWidth+40, mc.displayHeight - scaleY(40));
			}
			if (browser != null) {
				GL11.glDisable(GL11.GL_DEPTH_TEST);
				browser.draw(0, height, width, -15); // Don't forget to
				// flip Y axis.
				GL11.glEnable(GL11.GL_DEPTH_TEST);
			}
		} else {
			System.out.println("MCEF 미설치됨!");
			closeBrowser();
		}
	}

	public static void closeBrowser() {
		if (Loader.isModLoaded("mcef")) {
			if (browser != null) {
				browser.close();
				browser = null;
			}
		}
	}

	public static boolean isBrowser() {
		if (Loader.isModLoaded("mcef")) {
			return browser != null;
		} else
			return false;
	}


	public int scaleY(int y) {
		if (mc.currentScreen != null) {
			double sy = ((double) y) / ((double) mc.currentScreen.height) * ((double) mc.displayHeight);
			return (int) sy;
		} else {
			double sy = ((double) y) / ((double) mc.displayHeight) * ((double) mc.displayHeight);
			return (int) sy;
		}
	}


	public static File fileChooser() {
		File mcmeta = new File("resourcepacks/customclient/pack.mcmeta");

		if (!mcmeta.isFile()) {
			try {
				mcmeta.createNewFile();
				BufferedWriter writer = new BufferedWriter(new FileWriter(mcmeta));
				writer.write("{");
				writer.newLine();
				writer.write("  \"pack\": {");
				writer.newLine();
				writer.write("    \"pack_format\": 2,");
				writer.newLine();
				writer.write("    \"description\": \"CustomClient\"");
				writer.newLine();
				writer.write("  }");
				writer.newLine();
				writer.write("}");
				writer.flush();
				writer.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		JFileChooser chooser = new JFileChooser();
		{
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
			}

			chooser.setCurrentDirectory(new File(System.getProperty("user.home") + "/Desktop"));
			chooser.setFileFilter(new FileNameExtensionFilter("png 파일", "png"));
			chooser.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (JFileChooser.CANCEL_SELECTION.equals(e.getActionCommand())) {
						SwingUtilities.windowForComponent((JFileChooser) e.getSource()).dispose();
					} else if (JFileChooser.APPROVE_SELECTION.equals(e.getActionCommand())) {
						SwingUtilities.windowForComponent((JFileChooser) e.getSource()).dispose();
					}
				}
			});
			JDialog dialog = new JDialog();
			dialog.setAlwaysOnTop(true);
			dialog.setTitle("이미지 선택하기");
			dialog.setModal(true);
			dialog.add(chooser);
			dialog.pack();
			dialog.setLocationRelativeTo(null);
			dialog.setVisible(true);
		}
		return chooser.getSelectedFile();
	}
}
