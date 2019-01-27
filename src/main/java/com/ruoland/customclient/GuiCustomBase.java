package com.ruoland.customclient;

import com.ruoland.customclient.button.ButtonFunction;
import com.ruoland.customclient.component.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.montoyo.mcef.api.API;
import net.montoyo.mcef.api.IBrowser;
import net.montoyo.mcef.api.MCEFApi;
import org.apache.commons.io.FileUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import ruo.minigame.api.RenderAPI;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class GuiCustomBase extends GuiScreen {
    protected GuiData guiData;//GUI의 데이터를 저장하고 관리하는 클래스
    protected final Minecraft mc = Minecraft.getMinecraft();
    public final ArrayList<GuiCusTextField> fieldList = new ArrayList<GuiCusTextField>();//텍스트 필드가 많다보니까 한번에 관리하기 위해서 넣음
    public final ArrayList<IGuiComponent> removeList = new ArrayList<IGuiComponent>();//삭제 리스트
    public final ArrayList<GuiButton> buttonRemoveList = new ArrayList<GuiButton>();//삭제 리스트

    private boolean editMode = false;
    protected GuiTextField backgroundField, nameField, textureField;
    protected GuiTextField imagePathField;// 텍스쳐 생성할 때
    protected GuiCusButton gradientOnoff, fileFind, splashOn, visibleDisplayString;
    private IGuiComponent selectComponent;

    public GuiCustomBase(String name) {
        guiData = new GuiData(this, name);
        nameField = new GuiCusTextField(this, 1, mc.fontRendererObj, 20, 10, 140, 20);
        textureField = new GuiCusTextField(this, 2, mc.fontRendererObj, 20, 40, 200, 20);
        backgroundField = new GuiCusTextField(this, 5, mc.fontRendererObj, 20, 10, 140, 20);
        imagePathField = new GuiCusTextField(this, 6, mc.fontRendererObj, 20, 10, 140, 20);
        gradientOnoff = new GuiCusButton(200, 20, 50, 50, 20, "흐리게", false);
        splashOn = new GuiCusButton(201, 20, 90, 80, 20, "스플래시 켜기", false);
        fileFind = new GuiCusButton(10, 20, 70, 70, 20, "사진 선택", false);
        visibleDisplayString = new GuiCusButton(11, 20, 100, 70, 20, "버튼 이름 숨기기", false);
    }

    public void setGuiData(GuiData data) {
        guiData = data;
        guiData.loadComponent();
    }

    @Override
    public void initGui() {
        super.initGui();
        guiData.textureList.clear();
        buttonList.add(splashOn);
        buttonList.add(gradientOnoff);
        buttonList.add(fileFind);
        buttonList.add(visibleDisplayString);
        fieldAllEnable(false);
        textureEnable(false);
        guiData.loadComponent();
        backgroundField.setText(guiData.backgroundImage);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        String texture = guiData.backgroundImage;

        if (texture.startsWith("http") || texture.startsWith("Http") || texture.startsWith("www.youtube") || texture.startsWith("youtube.com")) {
            if (texture.indexOf("youtube") != -1) {
                drawBrowser("https://www.youtube.com/embed/" + getYoutubeID(texture), width, height);
            } else {
                drawBrowser(texture, width, height);
            }
        } else {
            closeBrowser();
            if (guiData.dynamicBackgroundImage != null)
                texture = guiData.dynamicBackgroundImage;
            if (!guiData.backgroundImage.equalsIgnoreCase("")) {
                RenderAPI.drawTextureZ(texture, 0, 0, -100, mc.displayWidth / 2, mc.displayHeight / 2);
                RenderAPI.drawTextureZ(texture, 0, 0, -100, mc.displayWidth / 2, mc.displayHeight / 2);
            }
        }
        if (this instanceof GuiMainMenuRealNew) {
            if (((GuiMainMenuRealNew) this).canRenderGradient()) {
                GlStateManager.pushMatrix();
                GlStateManager.translate(0, 0, -100);
                drawGradientRect(0, 0, width, height, -2130706433, 16777215);
                drawGradientRect(0, 0, width, height, 0, Integer.MIN_VALUE);
                GlStateManager.popMatrix();
            }
        }

        drawScreen();
        sizeEdit();

        super.drawScreen(mouseX, mouseY, partialTicks);
    }


    public void setEditMode() {
        editMode = !editMode;
        selectComponent = null;
        this.fieldAllEnable(false);
        this.textureEnable(false);
        //configsave();
        guiData.saveNBT(this);
    }

    public List<GuiButton> getButtonList() {
        return this.buttonList;
    }

    public GuiData getGuiData() {
        return guiData;
    }

    public boolean isSelectButton() {
        return selectComponent instanceof GuiCusButton;
    }

    public boolean isSelectString() {
        return selectComponent instanceof GuiString;
    }

    public boolean isSelectTexture() {
        return selectComponent instanceof GuiTexture;
    }

    /**
     * Called when the mouse is clicked.
     */
    protected void mouseClicked(int mouseX, int mouseY, int clickedMouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, clickedMouseButton);
        if (editMode) {
            backgroundField.setVisible(false);
            if (isSelectButton()) {
                getSelButton().setTexture(new ResourceLocation(textureField.getText()));
            }

            for (GuiTextField f : fieldList) {
                f.mouseClicked(mouseX, mouseY, clickedMouseButton);
                if (f.getVisible() && f.isFocused())
                    return;
            }
            if (clickedMouseButton == 0) {
                for (int i = 0; i < buttonList.size(); ++i) {//버튼 클릭 메서드
                    GuiCusButton guibutton = (GuiCusButton) buttonList.get(buttonList.size() - 1 - i);
                    if (guibutton.mousePressed(this.mc, mouseX, mouseY)) {
                        if (guibutton.canEdit)
                            selectButton(guibutton);
                        return;
                    }
                }
                for (int i = 0; i < getGuiData().stringList.size(); ++i) {//문자 클릭 메서드
                    GuiString guibutton = (GuiString) getGuiData().stringList.get(getGuiData().stringList.size() - 1 - i);
                    if (guibutton.mousePressed(this.mc, mouseX, mouseY)) {
                        selectString(guibutton);
                        return;
                    }
                }
                GuiTexture topTexture = null;
                for (int i = 0; i < guiData.textureList.size(); i++) {
                    GuiTexture g = guiData.textureList.get(guiData.textureList.size() - 1 - i);
                    if (g.mousePressed(mouseX, mouseY) && g.visible) {
                        if (topTexture == null || topTexture.z < g.z) ;
                        topTexture = g;
                    }
                }
                if (topTexture != null) {
                    selectTexture(topTexture);
                    return;
                }
            }

            selectComponent = null;// 단순히 배경만 눌렀다면  모두 제거
            guiData.backgroundImage = backgroundField.getText();
            fieldAllEnable(false);
            textureEnable(false);
            enableBackgroundField();
        }
    }

    public void selectString(GuiString guibutton) {
        selectComponent = guibutton;
        fieldAllEnable(false);
        textureEnable(false);
        nameField.setVisible(true);
        nameField.setText(guibutton.text);
        fileFind.setVisible(false);
        visibleDisplayString.setVisible(false);
    }

    public void selectButton(GuiCusButton guibutton) {
        selectComponent = guibutton;
        fieldAllEnable(true);
        textureEnable(false);
        nameField.setText(guibutton.displayString);
        textureField.setText(String.valueOf(guibutton.buttonTextures));
        fileFind.setVisible(true);
        visibleDisplayString.setVisible(true);
    }

    public void selectTexture(GuiTexture g) {
        selectComponent = g;
        fieldAllEnable(false);
        textureEnable(true);
        System.out.println(g.resourceLocation);
        imagePathField.setText(g.resourceLocation.toString());
        imagePathField.setVisible(true);
    }

    public void enableBackgroundField() {
        backgroundField.setEnabled(true);
        backgroundField.setVisible(true);
        backgroundField.setText(guiData.backgroundImage);
        this.gradientOnoff.setVisible(true);
        this.fileFind.setVisible(true);
        this.splashOn.setVisible(true);
    }

    public GuiCusButton getSelButton() {
        for (Object b : getButtonList()) {
            GuiCusButton button = (GuiCusButton) b;
            if (button.id == selectComponent.getID())
                return (GuiCusButton) b;
        }
        return null;
    }

    public GuiString getSelString() {
        return (GuiString) selectComponent;
    }

    public GuiTexture getSelTexture() {
        return (GuiTexture) selectComponent;
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        if (editMode) {
            if (isSelectButton()) {
                System.out.println(getSelButton().displayString + getSelButton().xPosition + " - " + getSelButton().yPosition);
                getSelButton().xPosition = mouseX;
                getSelButton().yPosition = mouseY;
            }
            if (isSelectTexture()) {
                getSelTexture().x = mouseX;
                getSelTexture().y = mouseY;
            }
            if (isSelectString()) {
                getSelString().x = mouseX;
                getSelString().y = mouseY;
            }
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        if (Keyboard.isKeyDown(Keyboard.KEY_E) && Keyboard.isKeyDown(56)) {//알트 + E
            setEditMode();
        }
        if (editMode) {
            for (GuiTextField f : fieldList) {
                f.textboxKeyTyped(typedChar, keyCode);
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_DELETE)
                    && (Keyboard.isKeyDown(Keyboard.KEY_RSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))) {
                if (isSelectButton()) {
                    getSelButton().setVisible(false);
                    buttonRemoveList.add(getSelButton());
                }
                if (isSelectTexture()) {
                    getSelTexture().setVisible(false);
                    removeList.add(getSelTexture());
                }
            }
            if (Keyboard.isKeyDown(56) && Keyboard.isKeyDown(Keyboard.KEY_C)) {
                if (imagePathField.getVisible()) {
                    if (!imagePathField.getText().equals("")) {
                        guiData.addTexture();
                    }
                }
            }
            if (Keyboard.isKeyDown(56) && Keyboard.isKeyDown(Keyboard.KEY_S)) {
                fieldAllEnable(false);
                nameField.setVisible(true);
            }
            if (Keyboard.isKeyDown(56) && Keyboard.isKeyDown(Keyboard.KEY_D)) {
                if (nameField.getVisible()) {
                    int i = Mouse.getEventX() * mc.currentScreen.width / mc.displayWidth;
                    int j = mc.currentScreen.height - Mouse.getEventY() * mc.currentScreen.height / mc.displayHeight - 1;
                    getGuiData().stringList.add(new GuiString(nameField.getText(), i, j,  getGuiData().stringList.size()));
                }
            }
            if (Keyboard.isKeyDown(56) && Keyboard.isKeyDown(Keyboard.KEY_X)) {
                fieldAllEnable(false);
                textureEnable(true);
                selectComponent = null;
            }

            if (Keyboard.isKeyDown(56) && Keyboard.isKeyDown(Keyboard.KEY_B)) {
                textureEnable(false);
                fieldAllEnable(false);

                GuiCusButton newButton = new GuiCusButton(guiData.findEmptyID(), 0, 0, 100, 20, "버튼");
                buttonList.add(newButton);
                selectComponent = (newButton);

            }
        }
    }

    public void writeNBT(GuiData guiData, NBTTagCompound tagCompound) {

    }

    public void readNBT(GuiData guiData, NBTTagCompound tagCompound) {

    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        if (button.id == 10) {
            File file = fileChooser();
            if (file != null) {
                FileUtils.copyFile(file.getAbsoluteFile(), new File("./resourcepacks/CustomClient/assets/customclient/textures/gui/" + file.getName()));
                ResourceLocation chooser = RenderAPI.getDynamicTexture(file.getName(), file);
                String name = "customclient:textures/gui/" + file.getName();
                if (isSelectButton()) {
                    setDynamicButtonField(chooser, name);
                } else if (isBackgroundEdit()) {
                    guiData.setDynamicBackgroundImage(chooser.toString(), name);
                } else {
                    setDynamicTextureField(RenderAPI.getDynamicTexture(file.getName(), file), name);
                }
            }
        }
        if (button.id == 11) {
            getSelButton().displayStringVisible = !getSelButton().displayStringVisible;
        }
        if (isEditMode()) {
            return;
        }
        ButtonFunction buttonFunction = new ButtonFunction(this);
        buttonFunction.init();
        buttonFunction.run(button);
    }

    protected boolean isBackgroundEdit() {
        return gradientOnoff.visible;
    }

    @Override
    public void onGuiClosed() {
        getGuiData().getTextureList().removeAll(removeList);
        for (GuiButton button : buttonRemoveList) {
            if (findButton(button.id) != null) {
                buttonList.remove(findButton(button.id));
                System.out.println(buttonList.size());
            }
        }
        buttonList.removeAll(buttonRemoveList);
        guiData.saveNBT(this);
        closeBrowser();

        super.onGuiClosed();
    }

    public GuiButton findButton(int id) {
        for (GuiButton button : buttonList) {
            if (button.id == id)
                return button;
        }
        return null;
    }

    public GuiCusButton findButton(String displayString) {
        for (GuiButton button : buttonList) {
            if (button.displayString.equalsIgnoreCase(displayString))
                return (GuiCusButton) button;
        }
        return null;
    }

    private void sizeEdit() {
        if (editMode) {
            if (isSelectString()) {
                GuiString button = getSelString();
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
            if (isSelectButton()) {
                GuiCusButton button = getSelButton();
                if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
                    button.width--;
                }
                if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
                    button.width++;
                }
                if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                    if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
                        button.alpha += 0.03;
                    }
                    if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
                        button.alpha -= 0.03;
                    }
                    System.out.println(button.displayString + " - " + button.alpha);

                } else {
                    if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
                        button.height--;
                    }
                    if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
                        button.height++;
                    }
                }
            }
            if (isSelectTexture()) {
                GuiTexture texture = (GuiTexture) selectComponent;
                if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
                    texture.width--;
                }
                if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
                    texture.width++;
                }
                if (Keyboard.isKeyDown(Keyboard.KEY_Z)) {
                    if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
                        texture.z += 1;
                    }
                    if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
                        texture.z -= 1;
                    }
                    System.out.println(texture.resourceLocation + " - " + texture.z);
                } else if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                    if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
                        texture.alpha += 0.03;
                    }
                    if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
                        texture.alpha -= 0.03;
                    }
                    System.out.println(texture.resourceLocation + " - " + texture.alpha);
                } else {
                    if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
                        texture.height--;
                    }
                    if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
                        texture.height++;
                    }
                }
            }
            if (isSelectButton())// 버튼 수정 업데이트
            {
                getSelButton().displayString = nameField.getText();
            }
        }
    }

    private void drawScreen() {
        for (GuiTexture g : guiData.textureList) {
            GL11.glPushMatrix();
            g.renderTexture();
            GL11.glPopMatrix();
        }
        if (editMode) {
            mc.fontRendererObj.drawString("수정 모드", 0, 0, 0x000000);
            if (selectComponent != null) {
                mc.fontRendererObj.drawString("선택한 컴포넌트 좌표 " + selectComponent.getX() + " - " + selectComponent.getY(), 0, 20, 0xFFFFFF);
                mc.fontRendererObj.drawString("선택한 컴포넌트 사이즈" + selectComponent.getWidth() + " - " + selectComponent.getHeight(), 0, 40, 0xFFFFFF);
            }

        }
        for (GuiString f : getGuiData().stringList) {
            GL11.glPushMatrix();
            GL11.glTranslatef(0, 0, 2);
            f.renderString();

            GL11.glPopMatrix();
        }
        for (GuiTextField f : fieldList) {
            GL11.glPushMatrix();
            GL11.glTranslatef(0, 0, 2);
            f.drawTextBox();
            GL11.glPopMatrix();
        }
    }

    private static IBrowser browser = null;

    public static void drawBrowser(String url, int x, int y, int width, int height) {
        Minecraft mc = Minecraft.getMinecraft();
        if (Loader.isModLoaded("mcef")) {
            if (browser == null) {
                API api = MCEFApi.getAPI();
                browser = api.createBrowser(
                        "https://www.youtube.com/embed/" + getYoutubeID(url) + "?autoplay=1&autohide=1&controls=0&showinfo=0&rel=0",
                        false);
                browser.resize(mc.displayWidth + 40, mc.displayHeight - scaleY(40));
            }
            if (browser != null) {
                GlStateManager.translate(x, y, 0);
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
        drawBrowser(url, 0, 0, width, height);
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

    public void fieldAllEnable(boolean enable) {
        for (GuiCusTextField f : fieldList) {
            f.setEnabled(enable);
            f.setVisible(enable);
        }
        backgroundField.setVisible(false);
        this.gradientOnoff.setVisible(false);
        this.splashOn.setVisible(false);
        this.visibleDisplayString.setVisible(false);
    }

    public void textureEnable(boolean enable) {
        imagePathField.setVisible(enable);
        fileFind.setVisible(enable);
        System.out.println("사진" + imagePathField.getVisible());
    }

    /**
     * 다이나믹 텍스쳐로 사진 설정시 minecraft:dynamic/텍시쳐이름 이렇게 됨
     * 이렇게 되면 껐다키면 사진을 불러올 수 없음
     * 그래서 뭐하는 코드인데
     */
    public void setDynamicTextureField(ResourceLocation resourceLocation, String textureField) {
        this.imagePathField.setText(textureField);
        if (isSelectTexture()) {
            getSelTexture().resourceLocation = new ResourceLocation(textureField);
            getSelTexture().dynamicLocation = resourceLocation;
            System.out.println(getSelTexture().resourceLocation + " - " + getSelTexture().dynamicLocation);
        }
    }

    public void setDynamicButtonField(ResourceLocation resourceLocation, String buttonField) {
        this.textureField.setText(buttonField);
        if (isSelectButton()) {
            getSelButton().buttonTextures = resourceLocation;
            getSelButton().dynamicLocation = resourceLocation;
        }
    }

    public static String getYoutubeID(String url) {
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
