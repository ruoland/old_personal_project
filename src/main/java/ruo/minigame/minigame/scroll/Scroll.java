package ruo.minigame.minigame.scroll;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;
import ruo.cmplus.CMManager;
import ruo.cmplus.camera.Camera;
import ruo.minigame.MiniGame;
import ruo.minigame.action.ActionEffect;
import ruo.minigame.api.PosHelper;
import ruo.minigame.api.WorldAPI;
import ruo.minigame.fakeplayer.FakePlayerHelper;
import ruo.minigame.minigame.AbstractMiniGame;

public class Scroll extends AbstractMiniGame {
    private Minecraft mc;
    private GameSettings s;
    public boolean x, z, xR, zR;
    public float yaw=-1, pitch=-1;//요는 좌우

    public Scroll() {
        mc = Minecraft.getMinecraft();
        s = mc.gameSettings;
    }

    @Override
    public boolean start(Object... obj) {
        KeyBinding.unPressAllKeys();
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
        ActionEffect.doubleJump(true);
        ActionEffect.crawl(true);
        return super.start();
    }

    @Override
    public boolean end(Object... obj) {
        boolean isCMReset = true;
        mc.thePlayer.motionX = 0;
        mc.thePlayer.motionY = 0;
        mc.thePlayer.motionZ = 0;
        if (obj.length > 0) {
            String str = (String) obj[0];
            if (str.equalsIgnoreCase("cm") || str.equalsIgnoreCase("cmreset"))
                isCMReset = false;
        }
        KeyBinding.unPressAllKeys();

        s.setOptionKeyBinding(s.keyBindForward, Keyboard.KEY_W);
        s.setOptionKeyBinding(s.keyBindBack, Keyboard.KEY_S);
        s.setOptionKeyBinding(s.keyBindRight, Keyboard.KEY_D);
        s.setOptionKeyBinding(s.keyBindLeft, Keyboard.KEY_A);
        KeyBinding.resetKeyBindingArrayAndHash();
        if (isCMReset)
            Camera.getCamera().reset();
        x = false;
        xR = false;
        zR = false;
        z = false;
        FakePlayerHelper.setFakeDead();
        CMManager.HOTBAR = true;
        CMManager.HAND = true;

        return super.end();
    }

    public double getForwardX(){
        if(x){
            return 0;
        }
        if(z)
            return -1;
        return 0;
    }
    public double getForwardZ(){
        if(x){
            return 1;
        }
        if(z)
            return 0;
        return 0;
    }


    public double getBackX(){
        if(x){
            return 0;
        }
        if(z)
            return -1;
        return 0;
    }
    public double getBackZ(){
        if(x){
            return 1;
        }
        if(z)
            return 0;
        return 0;
    }
    public double getRightX(){
        if(x){
            return -1;
        }
        if(z)
            return 0;
        return 0;
    }
    public double getRightZ(){
        if(x){
            return 0;
        }
        if(z)
            return 1;
        return 0;
    }

    public double getLeftX(){
        if(x){
            return 1;
        }
        if(z)
            return 0;
        return 0;
    }
    public double getLeftZ(){
        if(x){
            return 0;
        }
        if(z)
            return -1;
        return 0;
    }


    public void pos(int key) {
        boolean isSprintKeyDown = s.keyBindSprint.isKeyDown();

        if (Keyboard.KEY_D == key && s.keyBindForward.getKeyCode() != Keyboard.KEY_D) {// 두번째 조건은 카메라 설정을 한번만 하게 만들기 위해
            System.out.println(s.keyBindForward.getKeyCode() + " - 키 코드 - " + Keyboard.KEY_D);
            s.setOptionKeyBinding(s.keyBindForward, Keyboard.KEY_D);
            s.setOptionKeyBinding(s.keyBindBack, Keyboard.KEY_A);
            s.setOptionKeyBinding(s.keyBindRight, Keyboard.KEY_SLEEP);
            s.setOptionKeyBinding(s.keyBindLeft, Keyboard.KEY_SECTION);
            KeyBinding.resetKeyBindingArrayAndHash();
            KeyBinding.unPressAllKeys();
            KeyBinding.setKeyBindState(s.keyBindSprint.getKeyCode(), isSprintKeyDown);
            KeyBinding.setKeyBindState(s.keyBindForward.getKeyCode(), true);
            if (MiniGame.scroll.x) {
                if (!MiniGame.scroll.xR) {
                    Camera.getCamera().lockCamera(true, yaw == -1 ? 90: yaw, pitch == -1 ? 0 : pitch);
                    Camera.getCamera().moveCamera(0, 0.199, 6.7);
                    Camera.getCamera().rotateCamera(0, 180, 0);
                }
                if (MiniGame.scroll.xR) {
                    Camera.getCamera().lockCamera(true,  yaw == -1 ? -90: yaw, pitch == -1 ? 0 : pitch);
                    Camera.getCamera().moveCamera(0, 0.199, -6.7);
                    Camera.getCamera().rotateCamera(0, 0, 0);
                }
            }
            if (MiniGame.scroll.z) {
                if (!zR) {
                    Camera.getCamera().lockCamera(true,  yaw == -1 ? 0: yaw, pitch == -1 ? 0 : pitch);
                    Camera.getCamera().moveCamera(7, 0.199, -2);//원래 값은 x 6.7 y 0.199 z 0
                    Camera.getCamera().rotateCamera(0, 90, 0);
                }
                if (zR) {
                    Camera.getCamera().lockCamera(true,  yaw == -1 ? 180: yaw, pitch == -1 ? 0 : pitch);
                    Camera.getCamera().moveCamera(-6.7, 0.199, 0);
                    Camera.getCamera().rotateCamera(0, -90, 0);
                }
            }
        }

        if (Keyboard.KEY_A == key && s.keyBindForward.getKeyCode() != Keyboard.KEY_A) {// 두번째 조건은 카메라 설정을 한번만 하게 만들기 위해
            System.out.println(s.keyBindForward.getKeyCode() + " - 키 코드 - " + Keyboard.KEY_A);
            s.setOptionKeyBinding(s.keyBindForward, Keyboard.KEY_A);
            s.setOptionKeyBinding(s.keyBindBack, Keyboard.KEY_D);
            s.setOptionKeyBinding(s.keyBindRight, Keyboard.KEY_SLEEP);
            s.setOptionKeyBinding(s.keyBindLeft, Keyboard.KEY_SECTION);
            KeyBinding.resetKeyBindingArrayAndHash();
            KeyBinding.unPressAllKeys();
            KeyBinding.setKeyBindState(s.keyBindForward.getKeyCode(), true);
            KeyBinding.setKeyBindState(s.keyBindSprint.getKeyCode(), isSprintKeyDown);
            if (MiniGame.scroll.x) {
                if (!MiniGame.scroll.xR) {
                    Camera.getCamera().lockCamera(true,  yaw == -1 ? 270: yaw, pitch == -1 ? 0 : pitch);
                    Camera.getCamera().moveCamera(0, 0.199F, 6.7);
                    Camera.getCamera().rotateCamera(0, 180, 0);
                }
                if (MiniGame.scroll.xR) {
                    Camera.getCamera().lockCamera(true,  yaw == -1 ? 90: yaw, pitch == -1 ? 0 : pitch);
                    Camera.getCamera().moveCamera(0, 0.199, -6.7);
                    Camera.getCamera().rotateCamera(0, 0, 0);
                }
            }
            if (MiniGame.scroll.z) {
                if (!zR) {
                    Camera.getCamera().lockCamera(true,  yaw == -1 ? 180: yaw, pitch == -1 ? 0 : pitch);
                    Camera.getCamera().moveCamera(7, 0.199, -2);
                    Camera.getCamera().rotateCamera(0, 90, 0);
                }
                if (zR) {
                    Camera.getCamera().lockCamera(true,  yaw == -1 ? 0: yaw, pitch == -1 ? 0 : pitch);
                    Camera.getCamera().moveCamera(-6.7, 0.199, 0);
                    Camera.getCamera().rotateCamera(0, -90, 0);
                }
            }

        }
    }

}
