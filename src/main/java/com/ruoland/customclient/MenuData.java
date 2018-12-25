package com.ruoland.customclient;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.ConfigCategory;
import org.lwjgl.input.Mouse;
import ruo.minigame.api.NBTAPI;

import java.util.ArrayList;

public class MenuData {
    protected CustomTool customTool;
    protected ArrayList<Integer> removeList = new ArrayList<Integer>();
    protected ArrayList<GuiTexture> textureList = new ArrayList<GuiTexture>();
    private final NBTAPI nbtapi;

    public MenuData(CustomTool customTool, String fileName){
        nbtapi = new NBTAPI("./"+fileName+".dat");
        this.customTool = customTool;

    }
    public void saveNBT(GuiScreen mainmenu){
        for (GuiButton bu : customTool.getButtonList()) {
            GuiCusButton button = (GuiCusButton) bu;
            nbtapi.getNBT().setTag(String.valueOf(bu.id), button.serializeNBT(mainmenu));
        }
        for (int i = 0; i < textureList.size(); i++) {
            GuiTexture texture = textureList.get(i);
            if (texture.resourceLocation.toString().equals("") || texture.resourceLocation.toString().equals("minecraft:")) {
                textureList.remove(i);
                i--;
                continue;
            }
            nbtapi.getNBT().setTag(String.valueOf(texture.id), texture.serializeNBT());
        }
        nbtapi.getNBT().setInteger("Texture Size", textureList.size());
        nbtapi.getNBT().setString("backgroundImage", backgroundImage);
        nbtapi.getNBT().setBoolean("Gradient", customTool.canRenderGradient());
        nbtapi.saveNBT();
    }


    public void readBackground(){
        if(nbtapi.getNBT().hasKey("backgroundImage")) {
            backgroundImage = nbtapi.getNBT().getString("backgroundImage");
            customTool.setRenderGradient(nbtapi.getNBT().getBoolean("Gradient"));
        }
        addTitle();
    }
    public void readTexture() {
        GuiTexture texture;
        int size = nbtapi.getNBT().getInteger("Texture Size");
        for (int i = 0; i < size; i++) {
            NBTTagCompound tagCompound = nbtapi.getNBT().getCompoundTag(String.valueOf(i));
            texture = new GuiTexture(i);
            texture.deserializeNBT(tagCompound);
            this.textureList.add(texture);
        }
    }

    public void addTitle() {
        // 1000, "customclient:textures/gui/title2.png", 77, 31, 257, 45) X Y Width Height
        // 위에건 기본 값임, 절대 지우지 말 것
        //customclient:textures/gui/title2.png

        int i = 1000;
        boolean v = nbtapi.getNBT().getBoolean("Visible");

        String texturec = nbtapi.getNBT().getString("Texture");
        int x = nbtapi.getNBT().getInteger("xPosition");
        int y = nbtapi.getNBT().getInteger("yPosition");
        int w = nbtapi.getNBT().getInteger("width");
        int h = nbtapi.getNBT().getInteger("height");
        GuiTexture texture = new GuiTexture(i, texturec, x, y, w, h);
        texture.visible = v;
        texture.x = x;
        texture.y = y;
        this.textureList.add(texture);
    }

    public void buttonSetting() {
        for (int i = 0; i < customTool.getButtonList().size(); i++) {//기존에 있는 버튼을 설정함
            GuiCusButton b = (GuiCusButton) customTool.getButtonList().get(i);
            String buttonID = String.valueOf(b.id);
            b.deserializeNBT(nbtapi.getNBT().getCompoundTag(buttonID));
        }

        for (int i = 255; i < 300; i++) {
            GuiCusButton b = new GuiCusButton(i, 0, 0, 0, 0, "");
            String buttonID = String.valueOf(i);
            if(nbtapi.getNBT().hasKey(buttonID)) {
                b.deserializeNBT(nbtapi.getNBT().getCompoundTag(buttonID));
                customTool.getButtonList().add(b);
                customTool.getScreen().getButton().add(b);
            }
        }
        customTool.onoff.displayString = CustomClient.config.get("M", "onoff", "true").getString();
    }

    public int findEmptyID(){
        int[] buttonIDList = new int[customTool.getButtonList().size()];
        int emptyID = 0;
        for (int i = 0;i < buttonIDList.length;i++) {
            buttonIDList[i] = customTool.getButtonList().get(i).id;
        }
        for(int j = 255; j < 300;j++) {
            for (GuiButton button : customTool.getButtonList()) {
                if(button.id == j) {
                    emptyID = 0;
                    break;
                }
                emptyID = j;
            }
            if(emptyID > 0)
                break;
        }

        return emptyID;
    }
    private void buttonData(ConfigCategory category, String buttonID, GuiCusButton b) {
        if(!b.canEdit)
            return;
        int CuWidth = category.get("CuWidth").getInt();
        int CuHeight = category.get("CuHeight").getInt();

        if (customTool.getScreen().width != CuWidth)
            CuWidth = customTool.getScreen().width;
        if (customTool.getScreen().height != CuHeight)
            CuHeight = customTool.getScreen().height;
        int i2 = CuHeight / 4 + 48;

        b.xPosition = CuWidth / 2 + category.get("xPosition").getInt();
        b.yPosition = i2 + category.get("yPosition").getInt();
        b.width = category.get("Width").getInt();
        b.height = category.get("Height").getInt();
        b.displayString = category.get("Button").getString();
        b.visible = category.get("Visible").getBoolean();
        b.buttonTextures = new ResourceLocation(category.get("Texture").getString());

    }

    public void addTexture() {
        Minecraft mc = Minecraft.getMinecraft();
        int i = Mouse.getEventX() * mc.currentScreen.width / mc.displayWidth;
        int j = mc.currentScreen.height - Mouse.getEventY() * mc.currentScreen.height / mc.displayHeight - 1;
        this.textureList.add(new GuiTexture(this.textureList.size(), customTool.textureF.getText(), i, j,
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
    public String backgroundImage = "textures/gui/title/background/panorama_0.png";
    public String dynamicBackgroundImage;

    public void setDynamicBackgroundImage(String dynamic, String backgroundField) {
        backgroundImage = dynamic;
        dynamicBackgroundImage = dynamic;
        customTool.panorama.setText(backgroundField);
    }


}
