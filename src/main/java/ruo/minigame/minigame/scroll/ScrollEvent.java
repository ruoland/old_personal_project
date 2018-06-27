package ruo.minigame.minigame.scroll;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import org.lwjgl.input.Keyboard;
import ruo.minigame.MiniGame;

public class ScrollEvent {
	public ScrollEvent() {
	}
	private Minecraft mc = Minecraft.getMinecraft();
	private GameSettings s = mc.gameSettings;
	@SubscribeEvent
	public void login(WorldEvent.Unload event) {
		MiniGame.scroll.end();
	}
	@SubscribeEvent
	public void login(ClientTickEvent event) {
		if(mc.currentScreen != null || !MiniGame.scroll.isStart())
			return;
		 if (Keyboard.isKeyDown(Keyboard.KEY_A)){
			 MiniGame.scroll.pos(Keyboard.KEY_A);
		 } 
		 if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			 MiniGame.scroll.pos(Keyboard.KEY_D);
		 }
	}
}
