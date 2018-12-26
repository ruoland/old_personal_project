package com.ruoland.customclient;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.fml.common.Loader;
import net.montoyo.mcef.api.API;
import net.montoyo.mcef.api.IBrowser;
import net.montoyo.mcef.api.MCEFApi;
import org.lwjgl.input.Keyboard;
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
import java.util.regex.Pattern;

public class CustomTool {

    private final Minecraft mc = Minecraft.getMinecraft();
    private final ArrayList<GuiTextField> fieldList = new ArrayList<GuiTextField>();
    private final ArrayList<GuiTextField> textureField = new ArrayList<GuiTextField>();
    protected GuiCustomBase guiScreen;
    protected GuiData guiData;

    public CustomTool(String name) {
        guiData = new GuiData(this, name);
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


    private boolean editMode = false;
    protected int selectTextureID = -1, selectButtonID = -1;
    protected GuiTextField panorama, editName, texture;// 이름 바꾸는 텍스트 필드,
    // Width, Height
    protected GuiTextField textureF;// 이름 바꾸는 텍스트 필드, Width, Height
    protected GuiCusButton onoff, fileFind;

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
        //configsave();
        guiData.saveNBT(guiScreen);
    }

    public void initGui(GuiCustomBase mainmenu) {
        this.guiScreen = mainmenu;
        guiData.textureList.clear();

        mainmenu.getButton().add(onoff);
        mainmenu.getButton().add(fileFind);
        fieldAllEnable(false);
        textureEnable(false);

        guiData.customTool = this;
        guiData.buttonSetting();
        guiData.readTexture();
        guiData.readBackground();
        panorama.setText(guiData.backgroundImage);

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
        if (canRenderGradient()) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(0, 0, -100);
            guiScreen.drawGradientRect(0, 0, width, height, -2130706433, 16777215);
            guiScreen.drawGradientRect(0, 0, width, height, 0, Integer.MIN_VALUE);
            GlStateManager.popMatrix();
        }
        for (GuiTexture g : guiData.textureList) {
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

        if (splashVisible && guiScreen instanceof GuiMainMenuRealNew) {
            GuiMainMenuRealNew mainMenuRealNew = (GuiMainMenuRealNew) guiScreen;
            GL11.glPushMatrix();
            GL11.glTranslatef((float) (splashX), (float) splashY, 0.0F);
            GL11.glRotatef(-20.0F, 0.0F, 0.0F, 1.0F);
            float f1 = 1.8F - MathHelper
                    .abs(MathHelper.sin((float) (Minecraft.getSystemTime() % 1000L) / 1000.0F * (float) Math.PI * 2.0F)
                            * 0.1F);
            f1 = f1 * 100.0F / (float) (mc.fontRendererObj.getStringWidth(mainMenuRealNew.getSplashText()) + 32);
            GL11.glScalef(f1, f1, f1);
            guiScreen.drawCenteredString(mc.fontRendererObj, mainMenuRealNew.getSplashText(), 0, -8, -256);
            GL11.glPopMatrix();
        }
    }

    public GuiCustomBase getScreen(){
        return guiScreen;
    }
    public void setRenderGradient(boolean var) {
        onoff.displayString = String.valueOf(var);
    }
    public boolean canRenderGradient() {
        return this.onoff.displayString.equals("true");
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
                for (int i = 0; i < guiData.textureList.size(); i++) {
                    GuiTexture g = guiData.textureList.get(guiData.textureList.size() - 1 - i);
                    if (g.mousePressed(mouseX, mouseY) && g.visible) {
                        selectTexture(g);
                        return;
                    }
                }
            }

            selectButtonID = -1;// 단순히 배경만 눌렀다면
            selectTextureID = -1;// 선택한 것들을 제거한다
            guiData.backgroundImage = panorama.getText();
            fieldAllEnable(false);
            textureEnable(false);
            panorama.setEnabled(true);
            panorama.setVisible(true);
            panorama.setText(guiData.backgroundImage);
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
        if (!(mc.currentScreen instanceof GuiCustomBase)) {
            return;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_E) && Keyboard.isKeyDown(56)) {
            setEditMode();
        }
        if (editMode) {
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
                    guiData.removeList.add(selectButtonID);
                }
                if (selectTextureID != -1) {
                    getTextureByID(selectTextureID).visible = false;
                }
                System.out.println("Delete");
            }
            if (Keyboard.isKeyDown(56) && Keyboard.isKeyDown(Keyboard.KEY_C)) {
                if (textureF.getVisible()) {
                    if (!textureF.getText().equals("")) {
                        guiData.addTexture();
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

                GuiCusButton newButton = new GuiCusButton(guiData.findEmptyID(), 0, 0, 100, 20, "버튼");
                guiScreen.getButton().add(newButton);
                selectButtonID = 255;
                selectTextureID = -1;
                selectButton(newButton);

            }
            if (Keyboard.isKeyDown(29) && Keyboard.isKeyDown(44)) {
                if (guiData.removeList.size() != 0) {
                    getButtonByID(guiData.removeList.get(guiData.removeList.size() - 1)).visible = true;
                    guiData.removeList.remove(guiData.removeList.size() - 1);
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


    public GuiTexture getTextureByID(int id) {
        for (Object b : guiData.textureList) {
            GuiTexture button = (GuiTexture) b;
            if (button.id == id)
                return (GuiTexture) b;
        }
        return null;
    }

    public List<GuiButton> getButtonList() {
        return getScreen().getButton();
    }

    public GuiCusButton getButtonByID(int id) {
        for (Object b : getButtonList()) {
            GuiCusButton button = (GuiCusButton) b;
            if (button.id == id)
                return (GuiCusButton) b;
        }
        return null;
    }

    static IBrowser browser = null;

    public static void drawBrowser(String url, int x, int y, int width, int height) {
        Minecraft mc = Minecraft.getMinecraft();
        if (Loader.isModLoaded("mcef")) {
            if (browser == null) {
                API api = MCEFApi.getAPI();

                browser = api.createBrowser(
                        "https://www.youtube.com/embed/"+getYoutubeID(url)+ "?autoplay=1&autohide=1&controls=0&showinfo=0&rel=0",
                        false);
                browser.resize(mc.displayWidth + 40, mc.displayHeight - scaleY(40));
            }
            if (browser != null) {
                GlStateManager.translate(x,y,0);
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                browser.draw(0, height, width, 0); // Don't forget to
                // flip Y axis.
                GL11.glEnable(GL11.GL_DEPTH_TEST);
            }
        } else {
            System.out.println("MCEF 미설치됨!");
            closeBrowser();
        }
    }
    public static void drawBrowser(String url, int width, int height) {
        drawBrowser(url, 0,0, width,height);
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
    public boolean isEditMode() {
        return editMode;
    }

    public static String getYoutubeID(String url){
        String pattern = "(?<=watch\\?v=|/videos/|embed\\/|youtu.be\\/|\\/v\\/|\\/e\\/|watch\\?v%3D|watch\\?feature=player_embedded&v=|%2Fvideos%2F|embed%\u200C\u200B2F|youtu.be%2F|%2Fv%2F)[^#\\&\\?\\n]*";

        Pattern compiledPattern = Pattern.compile(pattern);
        java.util.regex.Matcher matcher = compiledPattern.matcher(url); //url is youtube url for which you want to extract the id.
        if (matcher.find()) {
            return matcher.group();
        }
        return "";
    }
    public static int scaleY(int y) {
        Minecraft mc = Minecraft.getMinecraft();

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
