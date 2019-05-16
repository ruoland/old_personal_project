package cmplus.cm.v15;

import olib.api.WorldAPI;
import net.minecraft.client.gui.*;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;

@SideOnly(Side.CLIENT)
public class GuiCGameOver extends GuiGameOver implements GuiYesNoCallback
{
    private ITextComponent causeOfDeath;
    private String message;
    private boolean scoreVisible, titleVisible;
    public String respawnCommand;
    public int scoreValue;
    public GuiCGameOver(String messaage, ITextComponent p_i46598_1_, boolean score, boolean title)
    {
        super(p_i46598_1_);
        causeOfDeath = p_i46598_1_;
        this.message = messaage;
        this.scoreVisible = score;
        this.titleVisible = title;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    public void initGui()
    {
        super.initGui();

        for (GuiButton guibutton : this.buttonList)
        {
            if(guibutton.id == 1){
                if(!titleVisible) {
                    guibutton.visible = false;
                    guibutton.enabled = false;
                }
            }
        }
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    protected void actionPerformed(GuiButton button) throws IOException
    {
        switch (button.id)
        {
            case 0:
                if(respawnCommand != null)
                    WorldAPI.command(respawnCommand);
                else {
                    this.mc.thePlayer.respawnPlayer();
                    this.mc.displayGuiScreen((GuiScreen) null);
                }
                break;
            case 1:

                if (this.mc.theWorld.getWorldInfo().isHardcoreModeEnabled())
                {
                    this.mc.displayGuiScreen(new GuiMainMenu());
                }
                else
                {
                    GuiYesNo guiyesno = new GuiYesNo(this, I18n.format("deathScreen.quit.confirm", new Object[0]), "", I18n.format("deathScreen.titleScreen", new Object[0]), I18n.format("deathScreen.respawn", new Object[0]), 0);
                    this.mc.displayGuiScreen(guiyesno);
                    guiyesno.setButtonDelay(20);
                }
        }
    }

    public void confirmClicked(boolean result, int id)
    {
        if (result)
        {
            if (this.mc.theWorld != null)
            {
                this.mc.theWorld.sendQuittingDisconnectingPacket();
            }

            this.mc.loadWorld((WorldClient)null);
            this.mc.displayGuiScreen(new GuiMainMenu());
        }
        else
        {
            this.mc.thePlayer.respawnPlayer();
            this.mc.displayGuiScreen((GuiScreen)null);
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawGradientRect(0, 0, this.width, this.height, 1615855616, -1602211792);
        GlStateManager.pushMatrix();
        GlStateManager.scale(2.0F, 2.0F, 2.0F);
        this.drawCenteredString(this.fontRendererObj, I18n.format(message, new Object[0]), this.width / 2 / 2, 30, 16777215);
        GlStateManager.popMatrix();

        if (this.causeOfDeath != null)
        {
            this.drawCenteredString(this.fontRendererObj, this.causeOfDeath.getFormattedText(), this.width / 2, 85, 16777215);
        }

        if(scoreVisible)
            this.drawCenteredString(this.fontRendererObj, I18n.format("deathScreen.score", new Object[0]) + ": " + TextFormatting.YELLOW + (scoreValue != -12345 ? scoreValue : this.mc.thePlayer.getScore()), this.width / 2, 100, 16777215);

        for (int i = 0; i < this.buttonList.size(); ++i)
        {
            this.buttonList.get(i).drawButton(this.mc, mouseX, mouseY);
        }

        for (int j = 0; j < this.labelList.size(); ++j)
        {
            this.labelList.get(j).drawLabel(this.mc, mouseX, mouseY);
        }
    }

    /**
     * Returns true if this GUI should pause the game when it is displayed in single-player
     */
    public boolean doesGuiPauseGame()
    {
        return false;
    }
}