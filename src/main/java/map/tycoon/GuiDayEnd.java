package map.tycoon;

import oneline.api.WorldAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import map.tycoon.map.MapHelper;

import java.io.IOException;

public class GuiDayEnd extends GuiScreen{
	
	public GuiDayEnd() {
		this.mc = Minecraft.getMinecraft();
	}
	@Override
	public void initGui() {
		super.initGui();
		this.buttonList.add(new GuiButton(0, mc.displayWidth / 2 - 100, mc.displayHeight / 2 + 100, "쇼핑하러 가기"));
		this.buttonList.add(new GuiButton(1, mc.displayWidth / 2 - 100, mc.displayHeight / 2 + 100, "자기"));

	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		mc.fontRendererObj.drawString("오늘의 날짜:"+WorldAPI.getWorldDay(mc.theWorld), mc.displayWidth/2,mc.displayHeight/2, 0xffffff);
		mc.fontRendererObj.drawString("현재 갖고 있는 돈:"+TyconHelper.playermoney, mc.displayWidth/2,mc.displayHeight/2+20, 0xffffff);
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		this.mc.displayGuiScreen(null);
		WorldAPI.getWorld().setWorldTime(0);
		MapHelper.setTyconOpen(true);
	}
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
}
