package com.ruoland.customclient;

import com.ruoland.customclient.component.GuiCusButton;
import com.ruoland.customclient.component.GuiString;
import com.ruoland.customclient.component.GuiTexture;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.nbt.NBTTagCompound;
import org.lwjgl.input.Mouse;
import ruo.minigame.api.NBTAPI;

import java.util.ArrayList;

public class GuiData {
    private GuiCustomBase customBase;
    protected ArrayList<GuiString> stringList = new ArrayList<GuiString>();

    protected ArrayList<GuiTexture> textureList = new ArrayList<GuiTexture>();
    private final NBTAPI nbtapi;
    Minecraft mc;

    public GuiData(GuiCustomBase customBase, String fileName) {
        nbtapi = new NBTAPI("./" + fileName + ".dat");
        mc = Minecraft.getMinecraft();
        this.customBase = customBase;

    }

    public ArrayList<GuiTexture> getTextureList() {
        return textureList;
    }

    public ArrayList<GuiString> getStringList() {
        return stringList;
    }

    public void saveNBT(GuiScreen mainmenu) {
        nbtapi.resetNBT();
        for (GuiButton bu : customBase.getButtonList()) {
            GuiCusButton button = (GuiCusButton) bu;
            String buttonID = new StringBuffer("Button ").append(bu.id).toString();
            nbtapi.getNBT().setTag(buttonID, button.serializeNBT(mainmenu));
        }
        for (GuiString bu : stringList) {
            nbtapi.getNBT().setTag("String "+bu.getID(), bu.serializeNBT(mainmenu));
        }

        for (int i = 0; i < textureList.size(); i++) {
            GuiTexture texture = textureList.get(i);
            if (texture.resourceLocation.toString().equals("") || texture.resourceLocation.toString().equals("minecraft:")) {
                textureList.remove(i);
                i--;
                continue;
            }
            if(texture.resourceLocation.toString().contains("dynamic")){
                System.out.println(texture.resourceLocation + " 가 다이나믹으로 저장됨"+texture.dynamicLocation);
            }
            String textureID = new StringBuffer("Texture ").append(texture.id).toString();
            nbtapi.getNBT().setTag(textureID, texture.serializeNBT());
        }
        nbtapi.getNBT().setInteger("Texture Size", textureList.size());
        nbtapi.getNBT().setString("backgroundImage", backgroundImage);
        customBase.writeNBT(this, nbtapi.getNBT());
        nbtapi.saveNBT();

    }


    public void clearTexture() {
        for (GuiTexture guiTexture : textureList) {
            guiTexture.visible = false;
        }
    }

    public void loadComponent() {
        readButton();
        readTexture();
        readBackground();
    }

    private void readButton() {
        for (GuiButton bu : customBase.getButtonList()) {//기존에 있는 버튼을 설정함
            GuiCusButton b = (GuiCusButton) bu;
            String buttonID = new StringBuffer("Button ").append(b.id).toString();
            b.deserializeNBT(nbtapi.getNBT().getCompoundTag(buttonID));
        }

        for (int i = 255; i < 300; i++) {
            GuiCusButton b = new GuiCusButton(i, 0, 0, 0, 0, "");
            String buttonID = new StringBuffer("Button ").append(i).toString();
            if (nbtapi.getNBT().hasKey(buttonID)) {
                b.deserializeNBT(nbtapi.getNBT().getCompoundTag(buttonID));
                customBase.getButtonList().add(b);
            }
        }
        for (int i = 0; i < 255; i++) {
            String buttonID = new StringBuffer("String ").append(i).toString();
            if(nbtapi.getNBT().hasKey(buttonID)) {
                GuiString guiString = new GuiString("",0,0,0);
                guiString.deserializeNBT(nbtapi.getNBT().getCompoundTag(buttonID));
            }else
                break;
        }
    }

    private void readBackground() {
        if (nbtapi.getNBT().hasKey("backgroundImage")) {
            backgroundImage = nbtapi.getNBT().getString("backgroundImage");
        }
        customBase.readNBT(this, nbtapi.getNBT());
    }

    private void readTexture() {
        GuiTexture texture;
        int size = nbtapi.getNBT().getInteger("Texture Size");
        System.out.println("텍스쳐 사이즈" + size);
        for (int i = 0; i < size; i++) {
            String buttonID = new StringBuffer("Texture ").append(i).toString();
            if (nbtapi.getNBT().hasKey(buttonID)) {
                NBTTagCompound tagCompound = nbtapi.getNBT().getCompoundTag(String.valueOf(buttonID));
                texture = new GuiTexture(i);
                texture.deserializeNBT(tagCompound);
                this.textureList.add(texture);
            }

        }
    }

    public NBTAPI getNBTAPI() {
        return nbtapi;
    }

    public int findEmptyID() {
        int[] buttonIDList = new int[customBase.getButtonList().size()];
        int emptyID = 0;
        for (int i = 0; i < buttonIDList.length; i++) {
            buttonIDList[i] = customBase.getButtonList().get(i).id;
        }
        for (int j = 255; j < 300; j++) {
            for (GuiButton button : customBase.getButtonList()) {
                if (button.id == j) {
                    emptyID = 0;
                    break;
                }
                emptyID = j;
            }
            if (emptyID > 0)
                break;
        }

        return emptyID;
    }


    public void addTexture() {
        int i = Mouse.getEventX() * mc.currentScreen.width / mc.displayWidth;
        int j = mc.currentScreen.height - Mouse.getEventY() * mc.currentScreen.height / mc.displayHeight - 1;
        this.textureList.add(new GuiTexture(this.textureList.size(), customBase.imagePathField.getText(), i, j,
                100, 100));
    }

    public void addTexture(int id, String texture, int mouseX, int mouseY, int width, int height) {
        this.textureList.add(new GuiTexture(id, texture, mouseX, mouseY,
                width, height));
    }

    public int mouseX() {
        int i = Mouse.getEventX() * mc.currentScreen.width / mc.displayWidth;
        return i;
    }

    public int mouseY() {
        int j = mc.currentScreen.height - Mouse.getEventY() * mc.currentScreen.height / mc.displayHeight - 1;
        return j;
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
        customBase.backgroundField.setText(backgroundField);
    }


}
