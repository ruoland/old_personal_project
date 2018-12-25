package com.ruoland.customclient;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.FileUtils;
import ruo.minigame.api.RenderAPI;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

public class GuiCustomBase extends GuiScreen {
    protected CustomTool customTool;
    public GuiCustomBase(String name){
        customTool = new CustomTool(name);
    }
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        String texture = customTool.menuData.backgroundImage;
        if (texture.startsWith("http") || texture.startsWith("Http") || texture.startsWith("www.youtube") || texture.startsWith("youtube.com")) {
            if (texture.indexOf("youtube") != -1) {
                customTool.drawBrowser("https://www.youtube.com/embed/"+getYoutubeID(texture), width, height);
            } else {
                customTool.menuData.backgroundImage = (texture);
                customTool.drawBrowser(texture, width, height);
            }
        } else {
            if(customTool.menuData.dynamicBackgroundImage != null)
                texture = customTool.menuData.dynamicBackgroundImage;
            if(!customTool.menuData.backgroundImage.equalsIgnoreCase("")) {
                RenderAPI.drawTextureZ(texture, 0, 0, -100, mc.displayWidth / 2, mc.displayHeight / 2);
                RenderAPI.drawTextureZ(texture, 0, 0, -100, mc.displayWidth / 2, mc.displayHeight / 2);
            }
        }
        customTool.drawScreen(width, height);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public String getYoutubeID(String url){
        String pattern = "(?<=watch\\?v=|/videos/|embed\\/|youtu.be\\/|\\/v\\/|\\/e\\/|watch\\?v%3D|watch\\?feature=player_embedded&v=|%2Fvideos%2F|embed%\u200C\u200B2F|youtu.be%2F|%2Fv%2F)[^#\\&\\?\\n]*";

        Pattern compiledPattern = Pattern.compile(pattern);
        java.util.regex.Matcher matcher = compiledPattern.matcher(url); //url is youtube url for which you want to extract the id.
        if (matcher.find()) {
            return matcher.group();
        }
        return "";
    }
    public List<GuiButton> getButton() {
        return this.buttonList;
    }
    /**
     * Called when the mouse is clicked.
     */
    protected void mouseClicked(int p_73864_1_, int p_73864_2_, int p_73864_3_) throws IOException {
        super.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
        customTool.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        customTool.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        customTool.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        customTool.keyTyped(typedChar, keyCode);
        System.out.println("키누름");
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);

        if (button.id == 200) {
            if (button.displayString.equals("false"))
                button.displayString = "true";
            else if (button.displayString.equals("true"))
                button.displayString = "false";
        }
        if (button.id == 10) {
            File file = (CustomTool.fileChooser());
            if (file != null) {
                FileUtils.copyFile(file.getAbsoluteFile(), new File("./resourcepacks/CustomClient/assets/customclient/textures/gui/" + file.getName()));
                ResourceLocation chooser = RenderAPI.getDynamicTexture(file.getName(), file);
                String name = "customclient:textures/gui/" + file.getName();
                if (customTool.selectButtonID != -1) {
                    customTool.setDynamicButtonField(chooser, name);
                } else if (customTool.isBackgroundEdit()) {
                    customTool.menuData.setDynamicBackgroundImage(chooser.toString(), name);
                } else {
                    customTool.setDynamicTextureField(RenderAPI.getDynamicTexture(file.getName(), file), name);
                }
            }
        }
        if(customTool.isEditMode()){
            return;
        }
        ButtonFunction buttonFunction = new ButtonFunction(this.customTool);
        buttonFunction.init();
        buttonFunction.run(button);
    }

    @Override
    public void onGuiClosed() {
        customTool.menuData.saveNBT(this);
        CustomTool.closeBrowser();
        super.onGuiClosed();
    }
}
