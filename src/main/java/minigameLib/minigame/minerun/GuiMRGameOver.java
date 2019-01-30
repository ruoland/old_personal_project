package minigameLib.minigame.minerun;
import minigameLib.api.WorldAPI;
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

@SideOnly(Side.CLIENT)
public class GuiMRGameOver extends GuiGameOver implements GuiYesNoCallback {
    public final int scoreValue;

    public GuiMRGameOver(int scoreValue) {
        super(new TextComponentString(""));
        this.scoreValue = scoreValue;

    }

    public void initGui() {
        super.initGui();
        buttonList.get(0).displayString = "세이브 포인트에서 이어하기";
        buttonList.get(1).visible = !buttonList.get(1).visible;
        buttonList.get(1).enabled = false;
    }

    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 0:
                WorldAPI.command("/minerun respawn");
                this.mc.displayGuiScreen((GuiScreen) null);
                break;
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawGradientRect(0, 0, this.width, this.height, 1615855616, -1602211792);
        GlStateManager.pushMatrix();
        GlStateManager.scale(2.0F, 2.0F, 2.0F);
        this.drawCenteredString(this.fontRendererObj, "게임 오버!", this.width / 2 / 2, 30, 16777215);
        GlStateManager.popMatrix();

        this.drawCenteredString(this.fontRendererObj, I18n.format("점수", new Object[0]) + ": " + TextFormatting.YELLOW + scoreValue, this.width / 2, 100, 16777215);

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