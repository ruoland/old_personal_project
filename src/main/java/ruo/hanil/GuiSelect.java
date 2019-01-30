package ruo.hanil;

import customclient.beta.GuiCustom;
import customclient.component.GuiCusButton;
import customclient.component.GuiString;
import customclient.component.GuiTexture;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.math.Vec3d;

import java.io.IOException;
import java.util.HashMap;

public class GuiSelect extends GuiCustom {
    GuiTextField playerNameField;
    private boolean clickButton = false, moveStart = false;
    private int delay = 0, phase;//phase = 0은 이름 선택, 1은 대륙, 2는 과거 직업
    private HashMap<Integer, Float> hashMap = new HashMap<>();
    private String playerName;

    public GuiSelect() {
        super("customgui/han");
        playerNameField = new GuiTextField(124, mc.fontRendererObj, 175, 80, 80, 20);
    }

    @Override
    public void initGui() {
        super.initGui();
        for (GuiTexture guiTexture : guiData.getTextureList()) {
            hashMap.put(guiTexture.id, guiTexture.alpha);
        }
        for (GuiButton button : buttonList) {
            GuiCusButton cusButton = (GuiCusButton) button;
            hashMap.put(button.id, cusButton.alpha);
        }
        findButton("결정하기").height = 20;
        updatePhase();
    }

    public void updatePhase() {
        if (phase == 0) {
            playerNameField.setVisible(true);

            for (GuiButton guiButton : buttonList) {
                GuiCusButton button2 = (GuiCusButton) guiButton;
                if (button2.canEdit)
                    button2.setVisible(true);
            }
            for (GuiButton guiButton : buttonList) {
                GuiCusButton button = (GuiCusButton) guiButton;
                System.out.println(button.getTexture());
                if (button.getTexture().toString().contains("your") || button.getTexture().toString().contains("back")) {
                    button.visible = false;
                }
            }
        }
        if (phase == 1) {
            playerNameField.setVisible(false);
            for (GuiButton guiButton : buttonList) {
                GuiCusButton button2 = (GuiCusButton) guiButton;
                if (button2.canEdit && button2.buttonTextures.toString().contains("your"))//대륙 선택
                    button2.setVisible(true);
            }
        }
        if (phase == 2) {
            for (GuiButton guiButton : buttonList) {
                GuiCusButton button2 = (GuiCusButton) guiButton;
                if (button2.canEdit && button2.buttonTextures.toString().contains("your"))//대륙 선택
                    button2.setVisible(false);
                if (button2.canEdit && button2.buttonTextures.toString().contains("back"))
                    button2.setVisible(true);
            }
            for (GuiString string : getGuiData().getStringList()) {
                string.setVisible(true);
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        playerNameField.drawTextBox();
        if (playerName != null)
            fontRendererObj.drawString(playerName, 195, 100, 0xFFFFFF);

        if (clickButton) {
            for (GuiTexture guiTexture : guiData.getTextureList()) {
                guiTexture.alpha -= 0.01;

            }
            for (GuiButton guibutton : buttonList) {
                GuiCusButton button = (GuiCusButton) guibutton;
                button.alpha -= 0.03;
                if (button.alpha < -0.3)
                    moveStart = true;
            }
            if (moveStart) {
                Vec3d vec3d = mc.thePlayer.getLookVec();
                mc.thePlayer.setVelocity(vec3d.xCoord / 5, 0, vec3d.zCoord / 5);
                delay++;
                if (delay > 40) {
                    moveEnd();
                    delay = 0;
                }
            }
        } else {
            for (GuiTexture guiTexture : guiData.getTextureList()) {
                if (guiTexture.alpha < hashMap.get(guiTexture.id))
                    guiTexture.alpha += 0.05;
            }
            for (GuiButton guiButton : buttonList) {
                GuiCusButton cusButton = (GuiCusButton) guiButton;
                if (!isDefaultButton(cusButton)) {
                    if (cusButton.alpha < hashMap.get(cusButton.id))
                        cusButton.alpha += 0.05;
                }
            }
        }
    }

    public boolean isDefaultButton(GuiButton button) {
        return button.displayString.equalsIgnoreCase("결정하기") || button.displayString.equalsIgnoreCase("뒤로 가기");
    }

    public void moveEnd() {
        moveStart = false;
        clickButton = false;
        findButton("결정하기").displayStringVisible = true;
        findButton("뒤로 가기").displayStringVisible = true;
        updatePhase();
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        if (button.displayString.equalsIgnoreCase("결정하기")) {
            if (phase == 0) {
                if (playerNameField.getText().equalsIgnoreCase("")) {
                    return;
                }
                playerName = playerNameField.getText();
            }
            clickButton = true;
            for (GuiTexture guiTexture : guiData.getTextureList()) {
                guiTexture.alpha = 0.5F;
            }
            findButton("결정하기").displayStringVisible = false;
            findButton("뒤로 가기").displayStringVisible = false;

            phase++;

        }
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();

    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        playerNameField.textboxKeyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int clickedMouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, clickedMouseButton);
        playerNameField.mouseClicked(mouseX, mouseY, clickedMouseButton);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

}
