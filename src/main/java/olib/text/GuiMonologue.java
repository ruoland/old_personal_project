package olib.text;

import olib.api.RenderAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;

import java.io.IOException;

public class GuiMonologue extends GuiScreen {

	private Monologue mono;
	private boolean pause, dark;

	public GuiMonologue(Monologue monologue) {
		this.mc = Minecraft.getMinecraft();
		this.mono = monologue;
		this.pause = mono.pause;
		this.dark = mono.dark;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		int width = Minecraft.getMinecraft().displayWidth;
		int height = Minecraft.getMinecraft().displayHeight;
		if (!mono.isLastPage()) {
			if (!mono.getMonoText().isEmpty()) {
				if (dark) {
					RenderAPI.drawTexture("loop:textures/text.png", (float) 1.0, -5, 0, width, height);
				} else
					RenderAPI.drawTexture("loop:textures/text.png", (float) 0.4, 0, 0, width, height);
				GlStateManager.pushMatrix();
				GlStateManager.scale(1.4, 1.4, 1.4);
				mono.monologueChange(mono.getMonoText().line.get(mono.page).toString(), width, height);
			}
			GlStateManager.popMatrix();
		}
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		if (mouseButton == 0) {
			if(!mono.textRenderComplete) {
				mono.textRenderComplete = true;
			}
			if (mono.isLastPage()) {
				mono = null;
				mc.displayGuiScreen(null);
				this.mc.setIngameFocus();
				return;
			}
			if (!mono.isLastPage() && mono.hasNextPage()) {
				mono.next();
			}
		}

		if (mouseButton == 1) {
			if (!mono.isLastPage() && mono.hasNextPage() && mono.page > 0) {
				mono.page--;
			}
		}
	}

	@Override
	public boolean doesGuiPauseGame() {
		return pause;
	}

}
