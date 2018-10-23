package ruo.minigame.minigame.scroll;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;
import ruo.cmplus.CMManager;
import ruo.cmplus.camera.Camera;
import ruo.minigame.MiniGame;
import ruo.minigame.api.WorldAPI;
import ruo.minigame.fakeplayer.FakePlayerHelper;
import ruo.minigame.minigame.AbstractMiniGame;

public class Scroll extends AbstractMiniGame {
	private Minecraft mc;
	private GameSettings s;
	public boolean x, z, xR, zR;

	public Scroll() {
		mc = Minecraft.getMinecraft();
		s = mc.gameSettings;
	}

	@Override
	public boolean start(Object... obj) {
		KeyBinding.unPressAllKeys();
		WorldAPI.getPlayer().motionX = 0;
		WorldAPI.getPlayer().motionY = 0;
		WorldAPI.getPlayer().motionZ = 0;
		mc.thePlayer.motionX = 0;
		mc.thePlayer.motionY = 0;
		mc.thePlayer.motionZ = 0;
		Camera.getCamera().reset();
		Camera.getCamera().setYP(true);
		Camera.getCamera().lockCamera(true, 90, 0);
		Camera.getCamera().playerCamera(true);
		s.setOptionKeyBinding(s.keyBindForward, Keyboard.KEY_D);
		pos(Keyboard.KEY_A);//카메라 초기화
		//MiniGame.spawnFakePlayer(false);
		return super.start();
	}

	@Override
	public boolean end(Object... obj) {
		boolean isCMReset = true;
		WorldAPI.getPlayer().motionX = 0;
		WorldAPI.getPlayer().motionY = 0;
		WorldAPI.getPlayer().motionZ = 0;
		mc.thePlayer.motionX = 0;
		mc.thePlayer.motionY = 0;
		mc.thePlayer.motionZ = 0;
		if(obj.length > 0) {
			String str = (String) obj[0];
			if(str.equalsIgnoreCase("cm") || str.equalsIgnoreCase("cmreset"))
				isCMReset  = false;
		}
		KeyBinding.unPressAllKeys();

		s.setOptionKeyBinding(s.keyBindForward, Keyboard.KEY_W);
		s.setOptionKeyBinding(s.keyBindBack, Keyboard.KEY_S);
		s.setOptionKeyBinding(s.keyBindRight, Keyboard.KEY_D);
		s.setOptionKeyBinding(s.keyBindLeft, Keyboard.KEY_A);
		KeyBinding.resetKeyBindingArrayAndHash();
		if(isCMReset)
			Camera.getCamera().reset();
		x = false;
		xR = false;
		zR = false;
		z = false;
		FakePlayerHelper.setFakeDead();
		CMManager.HOTBAR =true;
		CMManager.HAND = true;

		return super.end();
	}


	public void pos(int key) {

		if (Keyboard.KEY_D == key && s.keyBindForward.getKeyCode() != Keyboard.KEY_D) {// 두번째 조건은 카메라 설정을 한번만 하게 만들기 위해
			System.out.println(s.keyBindForward.getKeyCode()+" - 키 코드 - "+Keyboard.KEY_D);
			s.setOptionKeyBinding(s.keyBindForward, Keyboard.KEY_D);
			s.setOptionKeyBinding(s.keyBindBack, Keyboard.KEY_A);
			s.setOptionKeyBinding(s.keyBindRight, Keyboard.KEY_SLEEP);
			s.setOptionKeyBinding(s.keyBindLeft, Keyboard.KEY_SECTION);
			KeyBinding.resetKeyBindingArrayAndHash();
			KeyBinding.unPressAllKeys();
			WorldAPI.getPlayer().motionX = 0;
			WorldAPI.getPlayer().motionY = 0;
			WorldAPI.getPlayer().motionZ = 0;
			mc.thePlayer.motionX = 0;
			mc.thePlayer.motionY = 0;
			mc.thePlayer.motionZ = 0;
			mc.thePlayer.moveEntity(0,0,0);
			WorldAPI.getPlayer().moveEntity(0,0,0);
			WorldAPI.getPlayer().moveForward = 0;
			WorldAPI.getPlayer().moveStrafing = 0;

			if (MiniGame.scroll.x) {
				if (!MiniGame.scroll.xR) {

					Camera.getCamera().lockCamera(true, 90, 0);
					Camera.getCamera().moveCamera(0, 0.199, 6.7);
					Camera.getCamera().rotateCamera(0, 180, 0);
				}
				if (MiniGame.scroll.xR) {
					Camera.getCamera().lockCamera(true, -90, 0);
					Camera.getCamera().moveCamera(0, 0.199, -6.7);
					Camera.getCamera().rotateCamera(0, 0, 0);
				}
			}
			if (MiniGame.scroll.z) {
				if (!zR) {
					Camera.getCamera().lockCamera(true, 0, 0);
					Camera.getCamera().moveCamera(6.7, 0.199, 0);
					Camera.getCamera().rotateCamera(0, 90, 0);
				}
				if (zR) {
					Camera.getCamera().lockCamera(true, 180, 0);
					Camera.getCamera().moveCamera(-6.7, 0.199, 0);
					Camera.getCamera().rotateCamera(0, -90, 0);
				}
			}
			Minecraft.getMinecraft().thePlayer.moveEntity(0,0,0);
			WorldAPI.getPlayer().moveEntity(0,0,0);
			WorldAPI.getPlayer().moveForward = 0;
			WorldAPI.getPlayer().moveStrafing = 0;
			WorldAPI.getPlayer().motionX = 0;
			WorldAPI.getPlayer().motionY = 0;
			WorldAPI.getPlayer().motionZ = 0;
			Minecraft.getMinecraft().thePlayer.motionX = 0;
			Minecraft.getMinecraft().thePlayer.motionY = 0;
			Minecraft.getMinecraft().thePlayer.motionZ = 0;
		}

		if (Keyboard.KEY_A == key && s.keyBindForward.getKeyCode() != Keyboard.KEY_A) {// 두번째 조건은 카메라 설정을 한번만 하게 만들기 위해
			System.out.println(s.keyBindForward.getKeyCode()+" - 키 코드 - "+Keyboard.KEY_A);
			s.setOptionKeyBinding(s.keyBindForward, Keyboard.KEY_A);
			s.setOptionKeyBinding(s.keyBindBack, Keyboard.KEY_D);
			s.setOptionKeyBinding(s.keyBindRight, Keyboard.KEY_SLEEP);
			s.setOptionKeyBinding(s.keyBindLeft, Keyboard.KEY_SECTION);
			KeyBinding.resetKeyBindingArrayAndHash();
			KeyBinding.unPressAllKeys();
			WorldAPI.getPlayer().motionX = 0;
			WorldAPI.getPlayer().motionY = 0;
			WorldAPI.getPlayer().motionZ = 0;
			Minecraft.getMinecraft().thePlayer.motionX = 0;
			Minecraft.getMinecraft().thePlayer.motionY = 0;
			Minecraft.getMinecraft().thePlayer.motionZ = 0;
			Minecraft.getMinecraft().thePlayer.moveEntity(0,0,0);
			WorldAPI.getPlayer().moveEntity(0,0,0);
			WorldAPI.getPlayer().moveForward = 0;
			WorldAPI.getPlayer().moveStrafing = 0;

			if (MiniGame.scroll.x) {
				if (!MiniGame.scroll.xR) {
					Camera.getCamera().lockCamera(true, 270, 0);
					Camera.getCamera().moveCamera(0, 0.199F, 6.7);
					Camera.getCamera().rotateCamera(0, 180, 0);
				}
				if (MiniGame.scroll.xR) {
					Camera.getCamera().lockCamera(true, 90, 0);
					Camera.getCamera().moveCamera(0, 0.199, -6.7);
					Camera.getCamera().rotateCamera(0, 0, 0);
				}
			}
			if (MiniGame.scroll.z) {
				if (!zR) {
					Camera.getCamera().lockCamera(true, 180, 0);
					Camera.getCamera().moveCamera(6.7, 0.199, 0);
					Camera.getCamera().rotateCamera(0, 90, 0);
				}
				if (zR) {
					Camera.getCamera().lockCamera(true, 0, 0);
					Camera.getCamera().moveCamera(-6.7, 0.199, 0);
					Camera.getCamera().rotateCamera(0, -90, 0);
				}
			}
			Minecraft.getMinecraft().thePlayer.moveEntity(0,0,0);
			WorldAPI.getPlayer().moveEntity(0,0,0);
			WorldAPI.getPlayer().moveForward = 0;
			WorldAPI.getPlayer().moveStrafing = 0;

			WorldAPI.getPlayer().motionX = 0;
			WorldAPI.getPlayer().motionY = 0;
			WorldAPI.getPlayer().motionZ = 0;
			Minecraft.getMinecraft().thePlayer.motionX = 0;
			Minecraft.getMinecraft().thePlayer.motionY = 0;
			Minecraft.getMinecraft().thePlayer.motionZ = 0;
		}
	}

}
