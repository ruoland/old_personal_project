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

public class GuiData {
    protected CustomTool customTool;
    protected ArrayList<Integer> removeList = new ArrayList<Integer>();
    protected ArrayList<GuiTexture> textureList = new ArrayList<GuiTexture>();
    private final NBTAPI nbtapi;
    Minecraft mc;

    public GuiData(CustomTool customTool, String fileName) {
        nbtapi = new NBTAPI("./" + fileName + ".dat");
        this.customTool = customTool;
        mc = Minecraft.getMinecraft();

    }

    public void saveNBT(GuiScreen mainmenu) {
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
        customTool.guiScreen.writeNBT(this, nbtapi.getNBT());
        nbtapi.saveNBT();

    }


    public void readBackground() {
        if (nbtapi.getNBT().hasKey("backgroundImage")) {
            backgroundImage = nbtapi.getNBT().getString("backgroundImage");
        }
        System.out.println(nbtapi.getNBT().hasKey("Gradient") + " - " + customTool.guiScreen);
        customTool.guiScreen.readNBT(this, nbtapi.getNBT());
    }

    public void readTexture() {
        GuiTexture texture;
        int size = nbtapi.getNBT().getInteger("Texture Size");
        System.out.println("텍스쳐 사이즈" + size);
        for (int i = 0; i < size; i++) {
            if (nbtapi.getNBT().hasKey(String.valueOf(i))) {
                NBTTagCompound tagCompound = nbtapi.getNBT().getCompoundTag(String.valueOf(i));
                texture = new GuiTexture(i);
                texture.deserializeNBT(tagCompound);
                this.textureList.add(texture);
            }

        }
    }

    public void clearTexture() {
        for (GuiTexture guiTexture : textureList) {
            guiTexture.visible = false;
        }
    }

    public void readButton() {
        for (int i = 0; i < customTool.getButtonList().size(); i++) {//기존에 있는 버튼을 설정함
            GuiCusButton b = (GuiCusButton) customTool.getButtonList().get(i);
            String buttonID = String.valueOf(b.id);
            b.deserializeNBT(nbtapi.getNBT().getCompoundTag(buttonID));
        }

        for (int i = 255; i < 300; i++) {
            GuiCusButton b = new GuiCusButton(i, 0, 0, 0, 0, "");
            String buttonID = String.valueOf(i);
            if (nbtapi.getNBT().hasKey(buttonID)) {
                b.deserializeNBT(nbtapi.getNBT().getCompoundTag(buttonID));
                customTool.getButtonList().add(b);
                customTool.getScreen().getButton().add(b);
            }
        }
    }

    public NBTAPI getNBTAPI() {
        return nbtapi;
    }

    public int findEmptyID() {
        int[] buttonIDList = new int[customTool.getButtonList().size()];
        int emptyID = 0;
        for (int i = 0; i < buttonIDList.length; i++) {
            buttonIDList[i] = customTool.getButtonList().get(i).id;
        }
        for (int j = 255; j < 300; j++) {
            for (GuiButton button : customTool.getButtonList()) {
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
        this.textureList.add(new GuiTexture(this.textureList.size(), customTool.textureF.getText(), i, j,
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
        customTool.panorama.setText(backgroundField);
    }


}
