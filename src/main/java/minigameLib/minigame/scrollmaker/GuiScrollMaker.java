package minigameLib.minigame.scrollmaker;

import net.minecraft.client.gui.GuiScreen;

public class GuiScrollMaker extends GuiScreen{

	public GuiScrollMaker() {
		
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	public boolean doesGuiPauseGame() {
		return !super.doesGuiPauseGame();
	}
}
