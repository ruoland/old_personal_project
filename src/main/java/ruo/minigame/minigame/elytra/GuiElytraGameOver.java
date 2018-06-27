package ruo.minigame.minigame.elytra;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ruo.minigame.api.WorldAPI;

@SideOnly(Side.CLIENT)
public class GuiElytraGameOver extends GuiGameOver implements GuiYesNoCallback {
    public final int scoreValue;

    public GuiElytraGameOver(int scoreValue) {
        super(new TextComponentString(""));
        this.scoreValue = scoreValue;

    }
    public void initGui() {
        super.initGui();
        buttonList.get(0).displayString = "이어서 하기";
        buttonList.get(1).displayString = "그만두기";
    }

    protected void actionPerformed(GuiButton button)  {
        switch (button.id) {
            case 0:
                WorldAPI.command("/elytra respawn");
                this.mc.displayGuiScreen((GuiScreen) null);
                break;
            case 1:
                WorldAPI.command("/elytra s");
                WorldAPI.addMessage("다시 이 미니게임을 하고 싶으실 때는 /elytra start 명령어를 입력하세요");
                this.mc.displayGuiScreen((GuiScreen) null);

        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawGradientRect(0, 0, this.width, this.height, 1615855616, -1602211792);
        GlStateManager.pushMatrix();
        GlStateManager.scale(2.0F, 2.0F, 2.0F);
        this.drawCenteredString(this.fontRendererObj, "게임 오버!", this.width / 2 / 2, 30, 16777215);
        GlStateManager.popMatrix();

        this.drawCenteredString(this.fontRendererObj, I18n.format("죽인 몬스터 수", new Object[0]) + ": " + TextFormatting.YELLOW + scoreValue, this.width / 2, 100, 16777215);

        for (int i = 0; i < this.buttonList.size(); ++i) {
            this.buttonList.get(i).drawButton(this.mc, mouseX, mouseY);
        }

        for (int j = 0; j < this.labelList.size(); ++j) {
            this.labelList.get(j).drawLabel(this.mc, mouseX, mouseY);
        }
    }

    public boolean doesGuiPauseGame() {
        return true;
    }
}