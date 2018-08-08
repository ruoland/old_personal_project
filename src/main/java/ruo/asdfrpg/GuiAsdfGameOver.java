package ruo.asdfrpg;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketClientStatus;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ruo.minigame.api.WorldAPI;

@SideOnly(Side.CLIENT)
public class GuiAsdfGameOver extends GuiGameOver implements GuiYesNoCallback {

    public GuiAsdfGameOver() {
        super(new TextComponentString(""));

    }

    public void initGui() {
        super.initGui();
        buttonList.get(0).displayString = "여기서 부활(부활 아이템 소모)";
        buttonList.get(1).displayString = "마을에서 부활";
        System.out.println("아이템 체크"+mc.thePlayer.inventory.hasItemStack(new ItemStack(AsdfRPG.respawn)));

        if (mc.thePlayer.inventory.hasItemStack(new ItemStack(AsdfRPG.respawn))) {
            buttonList.get(0).enabled = true;
        }else
            buttonList.get(0).enabled = false;
        for(int i = 0; i < buttonList.size();i++){
            System.out.println(buttonList.get(i).displayString);
        }
    }

    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 0:
                BlockPos pos = mc.thePlayer.getPosition();
                this.mc.thePlayer.respawnPlayer();
                this.mc.displayGuiScreen((GuiScreen) null);
                WorldAPI.teleport(pos);
                break;
            case 1:
                BlockPos spawnPoint = mc.thePlayer.getPosition();
                this.mc.thePlayer.respawnPlayer();
                this.mc.displayGuiScreen((GuiScreen) null);
                WorldAPI.teleport(spawnPoint);
                break;

        }

    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawGradientRect(0, 0, this.width, this.height, 1615855616, -1602211792);
        GlStateManager.pushMatrix();
        GlStateManager.scale(2.0F, 2.0F, 2.0F);
        this.drawCenteredString(this.fontRendererObj, "게임 오버!", this.width / 2 / 2, 30, 16777215);
        GlStateManager.popMatrix();

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