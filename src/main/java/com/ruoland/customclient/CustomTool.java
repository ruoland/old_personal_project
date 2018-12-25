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
import ruo.minigame.api.RuoCode;

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

    public static CustomTool instance = new CustomTool();
    private final Minecraft mc = Minecraft.getMinecraft();
    private final ArrayList<GuiTextField> fieldList = new ArrayList<GuiTextField>();
    private final ArrayList<GuiTextField> textureField = new ArrayList<GuiTextField>();
    private GuiMainMenuRealNew mainmenu;
    private ArrayList<Integer> removeList = new ArrayList<Integer>();
    private ArrayList<GuiTexture> textureList = new ArrayList<GuiTexture>();


    private CustomTool() {
        editName = new GuiTextField(1, mc.fontRendererObj, 20, 10, 140, 20);
        texture = new GuiTextField(2, mc.fontRendererObj, 20, 40, 200, 20);
        panorama = new GuiTextField(5, mc.fontRendererObj, 20, 10, 140, 20);

        textureF = new GuiTextField(6, mc.fontRendererObj, 20, 10, 140, 20);
        onoff = new GuiCusButton(200, 20, 50, 50, 20, "true", false);
        fileFind = new GuiCusButton(10, 20, 120, 70, 20, "사진 선택", false);
        onoff.visible = false;
        fileFind.visible = false;
        texture.setMaxStringLength(5000);
        textureF.setMaxStringLength(5000);
        editName.setMaxStringLength(5000);
        panorama.setMaxStringLength(5000);
        this.fieldList.add(editName);
        this.fieldList.add(texture);
        this.fieldList.add(panorama);
        this.textureField.add(textureF);
    }

    public GuiMainMenuRealNew getMainmenu() {
        return mainmenu;
    }

    private boolean editMode = false;
    protected int selectTextureID = -1, selectButtonID = -1;
    private GuiTextField panorama, editName, texture;// 이름 바꾸는 텍스트 필드,
    // Width, Height
    private GuiTextField textureF;// 이름 바꾸는 텍스트 필드, Width, Height
    private GuiCusButton onoff, fileFind;

    public boolean splashVisible = true;
    int splashX, splashY;

    public void setEditMode() {
        if (!editMode) {
            loadSplashData();
        }
        editMode = !editMode;
        selectButtonID = -1;
        selectTextureID = -1;

        this.fieldAllEnable(false);
        this.textureEnable(false);
        configsave();
    }

    public boolean isEditMode() {
        return editMode;
    }

    public void initGui(GuiMainMenuRealNew mainmenu) {
        this.mainmenu = mainmenu;
        this.textureList.clear();

        mainmenu.getButton().add(onoff);
        mainmenu.getButton().add(fileFind);
        fieldAllEnable(false);
        textureEnable(false);

        buttonSetting();
        textureSetting();
        addTitle();

        this.backgroundImage = CustomClient.mainmenuCategory.get("Mainmenu").getString();
        panorama.setText(backgroundImage);

    }

    public void loadSplashData() {
        CustomClient.config.load();
        ConfigCategory splashConfig = CustomClient.config.getCategory("Splash");
        splashX = splashConfig.get("X").getInt();
        splashY = splashConfig.get("Y").getInt();
        splashVisible = splashConfig.get("SplashVisible").getBoolean();
    }

    /**
     * 다이나믹 텍스쳐로 사진 설정시 minecraft:dynamic/텍시쳐이름 이렇게 됨
     * 이렇게 되면 껐다키면 사진을 불러올 수 없음
     */
    public void setDynamicTextureField(ResourceLocation resourceLocation, String textureField) {
        this.textureF.setText(textureField);
        if (selectTextureID != -1) {
            getTextureByID(selectTextureID).resourceLocation = resourceLocation;
            getTextureByID(selectTextureID).dynamicLocation = resourceLocation;
        }
    }

    public void setDynamicButtonField(ResourceLocation resourceLocation, String buttonField) {
        this.texture.setText(buttonField);
        if (selectButtonID != -1) {
            getButtonByID(selectButtonID).buttonTextures = resourceLocation;
            getButtonByID(selectButtonID).dynamicLocation = resourceLocation;
        }
    }

    public void drawScreen(int width, int height) {
        for (GuiTexture g : textureList) {
            GL11.glPushMatrix();
            g.renderTexture();
            GL11.glPopMatrix();
        }
        if (editMode) {
            if (getButtonByID(selectButtonID) != null) {
                GuiCusButton button = getButtonByID(selectButtonID);
                if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
                    button.width--;
                }
                if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
                    button.width++;
                }
                if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
                    button.height--;
                }
                if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
                    button.height++;
                }
            }
            if (getTextureByID(selectTextureID) != null) {
                GuiTexture texture = getTextureByID(selectTextureID);
                if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
                    texture.width--;
                }
                if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
                    texture.width++;
                }
                if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
                    texture.height--;
                }
                if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
                    texture.height++;
                }
            }
            if (selectButtonID != -1)// 버튼 수정 업데이트
            {
                getButtonByID(selectButtonID).displayString = editName.getText();
            }

            mc.fontRendererObj.drawString("수정 모드", 0, 0, 0x000000);
        } else {// 에딧 모드가 해제되거나 버튼을 선택하지 않으면 이름 변경 칸도 숨김
            fieldAllEnable(false);
            textureEnable(false);
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
            GL11.glTranslatef((float) (splashX), (float) splashY, 0.0F);
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

    public boolean canRenderGradient() {
        return this.onoff.displayString.equals("true");
    }

    public String backgroundImage = "textures/gui/title/background/panorama_0.png";
    public String dynamicBackgroundImage;

    public void setDynamicBackgroundImage(String dynamic, String backgroundField) {
        backgroundImage = dynamic;
        dynamicBackgroundImage = dynamic;
        panorama.setText(backgroundField);
    }

    public void updateTexture() {
        if (selectTextureID != -1)
            getTextureByID(selectTextureID).resourceLocation = new ResourceLocation(textureF.getText());//텍스쳐 업데이트
        if (selectButtonID != -1)
            getButtonByID(selectButtonID).buttonTextures = new ResourceLocation(texture.getText());//버튼 텍스쳐 업데이트
    }

    public void selectButton(GuiCusButton guibutton) {
        this.selectTextureID = -1;
        this.selectButtonID = guibutton.id;
        fieldAllEnable(true);
        textureEnable(false);
        editName.setText(guibutton.displayString);
        texture.setText(String.valueOf(guibutton.buttonTextures));
        fileFind.visible = true;
        fileFind.enabled = true;
    }

    public void selectTexture(GuiTexture g) {
        this.selectTextureID = g.id;
        this.selectButtonID = -1;
        fieldAllEnable(false);
        textureEnable(true);
        textureF.setText(g.resourceLocation.toString());
        textureF.setVisible(true);
    }

    protected boolean isBackgroundEdit() {
        return onoff.visible;
    }

    public void mouseClicked(int mouseX, int mouseY, int clickedMouseButton) {
        if (editMode) {
            panorama.setEnabled(false);
            panorama.setVisible(false);
            if (selectButtonID != -1) {
                getButtonByID(selectButtonID).buttonTextures = new ResourceLocation(texture.getText());
            }
            updateTexture();

            for (GuiTextField f : fieldList) {
                f.mouseClicked(mouseX, mouseY, clickedMouseButton);
                if (f.getVisible() && f.isFocused())
                    return;
            }
            for (GuiTextField f : textureField) {
                f.mouseClicked(mouseX, mouseY, clickedMouseButton);
                if (f.getVisible() && f.isFocused())
                    return;
            }
            if (clickedMouseButton == 0) {
                for (int i = 0; i < getButtonList().size(); ++i) {//버튼 클릭 메서드
                    GuiCusButton guibutton = (GuiCusButton) getButtonList().get(getButtonList().size() - 1 - i);
                    if (guibutton.mousePressed(this.mc, mouseX, mouseY)) {
                        if (guibutton.canEdit)
                            selectButton(guibutton);
                        return;
                    }
                }
                for (int i = 0; i < textureList.size(); i++) {
                    GuiTexture g = textureList.get(textureList.size() - 1 - i);
                    if (g.mousePressed(mouseX, mouseY) && g.visible) {
                        selectTexture(g);
                        return;
                    }
                }
            }

            selectButtonID = -1;// 단순히 배경만 눌렀다면
            selectTextureID = -1;// 선택한 것들을 제거한다
            backgroundImage = panorama.getText();
            fieldAllEnable(false);
            textureEnable(false);
            panorama.setEnabled(true);
            panorama.setVisible(true);
            panorama.setText(backgroundImage);
            this.onoff.visible = true;
            this.onoff.enabled = true;
            this.fileFind.visible = true;
            this.fileFind.enabled = true;
        }
    }

    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        if (editMode) {
            if (selectButtonID != -1) {
                getButtonByID(selectButtonID).xPosition = mouseX;
                getButtonByID(selectButtonID).yPosition = mouseY;
            }
            if (selectTextureID != -1) {
                getTextureByID(selectTextureID).x = mouseX;
                getTextureByID(selectTextureID).y = mouseY;
            }
        }
    }

    protected void mouseReleased(int mouseX, int mouseY, int state) {
    }

    public void keyTyped(char typedChar, int keyCode) {
        if (!(mc.currentScreen instanceof GuiMainMenuRealNew)) {
            return;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_E) && Keyboard.isKeyDown(56)) {
            setEditMode();
        }
        if (isEditMode()) {
            for (GuiTextField f : fieldList) {
                f.textboxKeyTyped(typedChar, keyCode);
            }
            for (GuiTextField f : textureField) {
                f.textboxKeyTyped(typedChar, keyCode);
            }

            if (Keyboard.isKeyDown(Keyboard.KEY_DELETE)
                    && (Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))) {
                if (selectButtonID != -1) {
                    getButtonByID(selectButtonID).visible = false;
                    removeList.add(selectButtonID);
                }
                if (selectTextureID != -1) {
                    getTextureByID(selectTextureID).visible = false;
                }
                System.out.println("Delete");
            }
            if (Keyboard.isKeyDown(56) && Keyboard.isKeyDown(Keyboard.KEY_C)) {
                if (textureF.getVisible()) {
                    if (!textureF.getText().equals("")) {
                        this.addTexture();
                    }
                }
            }
            if (Keyboard.isKeyDown(56) && Keyboard.isKeyDown(Keyboard.KEY_X)) {
                textureEnable(true);
                fieldAllEnable(false);
                selectButtonID = -1;
                selectTextureID = -1;
            }

            if (Keyboard.isKeyDown(56) && Keyboard.isKeyDown(Keyboard.KEY_B)) {
                textureEnable(false);
                fieldAllEnable(false);
                id++;
                GuiCusButton newButton = new GuiCusButton(id, 0, 0, 100, 20, "버튼");
                mainmenu.getButton().add(newButton);
                selectButtonID = 255;
                selectTextureID = -1;
                selectButton(newButton);
            }
            if (Keyboard.isKeyDown(29) && Keyboard.isKeyDown(44)) {
                if (removeList.size() != 0) {
                    getButtonByID(removeList.get(removeList.size() - 1)).visible = true;
                    removeList.remove(removeList.size() - 1);
                }
            }
        }
    }

    int id = 255;

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
                    c.get("yPosition").setDefaultValue(bu.yPosition).setValue(bu.yPosition - i2);
                    c.get("Width").setDefaultValue(bu.width).setValue(bu.width);
                    c.get("Height").setDefaultValue(bu.height).setValue(bu.height);
                    c.get("CuWidth").setDefaultValue(mainmenu.width).setValue(mainmenu.width);
                    c.get("CuHeight").setDefaultValue(mainmenu.height).setValue(mainmenu.height);
                    c.get("Texture").setDefaultValue(bu.buttonTextures.toString())
                            .set(bu.buttonTextures.toString());
                    c.get("Visible").setDefaultValue(bu.visible).setValue(bu.visible);
                } else {
                    String id = String.valueOf(bu.id);
                    ConfigCategory c = CustomClient.config.getCategory(id);
                    CustomClient.config.addCustomCategoryComment(id, "버튼 정보");
                    get(id, "Button", string).set(string);
                    get(id, "xPosition", bu.xPosition).setValue(bu.xPosition - mainmenu.width / 2);
                    get(id, "yPosition", bu.yPosition).setValue(bu.yPosition - i2);
                    get(id, "Width", bu.width).setValue(bu.width).setValue(bu.width);
                    get(id, "Height", bu.height).setValue(bu.height);
                    get(id, "CuWidth", mainmenu.width).setValue(mainmenu.width);
                    get(id, "CuHeight", mainmenu.height).setValue(mainmenu.height).setValue(mainmenu.height);
                    get(id, "Texture", bu.buttonTextures.toString()).setValue(bu.buttonTextures.toString());
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
            String textureID = "T" + String.valueOf(bu.id);
            if (CustomClient.config.hasCategory(textureID)) {
                CustomClient.config.addCustomCategoryComment(textureID, "텍스쳐");
                CustomClient.config.get(textureID, "Texture", bu.resourceLocation.toString())
                        .set(bu.resourceLocation.toString());
                CustomClient.config.get(textureID, "xPosition", bu.x).set(bu.x);
                CustomClient.config.get(textureID, "yPosition", bu.y).set(bu.y);
                CustomClient.config.get(textureID, "Width", bu.width).set(bu.width);
                CustomClient.config.get(textureID, "Height", bu.height).set(bu.height);
                CustomClient.config.get(textureID, "Visible", bu.visible).set(bu.visible);
            } else {
                CustomClient.config.addCustomCategoryComment(textureID, "텍스쳐");
                CustomClient.config.get(textureID, "Texture", bu.resourceLocation.toString(), "기본값:" + bu.resourceLocation.toString())
                        .set(bu.resourceLocation.toString());
                CustomClient.config.get(textureID, "xPosition", bu.x, "기본값: " + bu.x).set(bu.x);
                CustomClient.config.get(textureID, "yPosition", bu.y, "기본값: " + bu.y).set(bu.y);
                CustomClient.config.get(textureID, "Width", bu.width, "기본값: " + bu.width).set(bu.width);
                CustomClient.config.get(textureID, "Height", bu.height, "기본값: " + bu.height).set(bu.height);
                CustomClient.config.get(textureID, "Visible", bu.visible).set(bu.visible);
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


    public GuiTexture getTextureByID(int id) {
        for (Object b : this.textureList) {
            GuiTexture button = (GuiTexture) b;
            if (button.id == id)
                return (GuiTexture) b;
        }
        return null;
    }

    public List<GuiButton> getButtonList() {
        return getMainmenu().getButton();
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


    public Property get(String category, String key, boolean defaults) {
        return CustomClient.config.get(category, key, defaults);
    }

    public Property get(String category, String key, int defaults) {
        return CustomClient.config.get(category, key, defaults);
    }

    public Property get(String category, String key, String defaults) {
        return CustomClient.config.get(category, key, defaults);
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

        for (int i = 255; i < 500; i++) {
            GuiCusButton b = new GuiCusButton(i, 0, 0, 0, 0, "");
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
                mainmenu.getButton().add(b);
            } else {
                break;
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
            this.textureList.add(new GuiTexture(this.textureList.size(), textureF.getText(), i, j,
                    100, 100));
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
                browser.resize(mc.displayWidth + 40, mc.displayHeight - scaleY(40));
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
