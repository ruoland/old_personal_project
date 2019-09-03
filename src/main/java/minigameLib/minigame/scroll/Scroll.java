package minigameLib.minigame.scroll;

import cmplus.CMManager;
import cmplus.camera.Camera;
import minigameLib.MiniGame;
import olib.fakeplayer.FakePlayerHelper;
import minigameLib.minigame.AbstractMiniGame;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

public class Scroll extends AbstractMiniGame {
    private Minecraft mc;
    private GameSettings s;
    public boolean x, z, xR, zR;
    public float moveX, moveZ;//카메라
    public float yaw = -1, pitch = -1;//요는 좌우
    public double startX, startZ;

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
        Camera.reset();
        Camera.setYP(true);
        Camera.lockCamera(true, 90, 0);
        Camera.playerCamera(true);
        s.setOptionKeyBinding(s.keyBindForward, Keyboard.KEY_D);
        pos(Keyboard.KEY_A);//카메라 초기화
        KeyBinding.setKeyBindState(s.keyBindForward.getKeyCode(), false);
        startX = mc.thePlayer.posX;
        startZ = mc.thePlayer.posZ;
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
            Camera.reset();
        x = false;
        xR = false;
        zR = false;
        z = false;
        FakePlayerHelper.setFakeDead();
        CMManager.HOTBAR = true;
        CMManager.HAND = true;

        return super.end();
    }

    public double getForwardXZ() {
        if (x) {
            if (mc.thePlayer.rotationYaw == 90)
                return -1;
            else if (mc.thePlayer.rotationYaw == -90) {
                return 1;
            }
        }
        if (z) {

            if (mc.thePlayer.rotationYaw == 0)
                return 1;
            else if (mc.thePlayer.rotationYaw == 180) {
                return -1;
            }
        }
        return 0;
    }


    public double getBackX() {
        if (x) {
            return 0;
        }
        if (z)
            return -1;
        return 0;
    }

    public double getBackZ() {
        if (x) {
            return 1;
        }
        if (z)
            return 0;
        return 0;
    }

    public double getLookX() {
        if (x) {
            return -1;
        }
        if (z)
            return 0;
        return 0;
    }

    public double getRightZ() {
        if (x) {
            return 0;
        }
        if (z)
            return 1;
        return 0;
    }

    public double getLeftX() {
        if (x) {
            return 1;
        }
        if (z)
            return 0;
        return 0;
    }

    public double getLeftZ() {
        if (x) {
            return 0;
        }
        if (z)
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
                    Camera.lockCamera(true, yaw == -1 ? 90 : yaw, pitch == -1 ? 0 : pitch);
                    Camera.moveCamera(moveX, 0.199, moveZ);
                    Camera.rotateCamera(0, 180, 0);
                }
                if (MiniGame.scroll.xR) {
                    Camera.lockCamera(true, yaw == -1 ? -90 : yaw, pitch == -1 ? 0 : pitch);
                    Camera.moveCamera(moveX, 0.199, moveZ);
                    Camera.rotateCamera(0, 0, 0);
                }
            }
            if (MiniGame.scroll.z) {
                if (!zR) {
                    Camera.lockCamera(true, yaw == -1 ? 0 : yaw, pitch == -1 ? 0 : pitch);
                    Camera.moveCamera(moveX, 0.199, moveZ);//원래 값은 x 6.7 y 0.199 z 0
                    Camera.rotateCamera(0, 90, 0);
                }
                if (zR) {
                    Camera.lockCamera(true, yaw == -1 ? 180 : yaw, pitch == -1 ? 0 : pitch);
                    Camera.moveCamera(moveX, 0.199, moveZ);
                    Camera.rotateCamera(0, -90, 0);
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
                    Camera.lockCamera(true, yaw == -1 ? 270 : yaw, pitch == -1 ? 0 : pitch);
                    Camera.moveCamera(moveX, 0.199F, moveZ);
                    Camera.rotateCamera(0, 180, 0);
                }
                if (MiniGame.scroll.xR) {
                    Camera.lockCamera(true, yaw == -1 ? 90 : yaw, pitch == -1 ? 0 : pitch);
                    Camera.moveCamera(moveX, 0.199, moveZ);
                    Camera.rotateCamera(0, 0, 0);
                }
            }
            if (MiniGame.scroll.z) {
                if (!zR) {
                    Camera.lockCamera(true, yaw == -1 ? 180 : yaw, pitch == -1 ? 0 : pitch);
                    Camera.moveCamera(moveX, 0.199, moveZ);
                    Camera.rotateCamera(0, 90, 0);
                }
                if (zR) {
                    Camera.lockCamera(true, yaw == -1 ? 0 : yaw, pitch == -1 ? 0 : pitch);
                    Camera.moveCamera(moveX, 0.199, moveZ);
                    Camera.rotateCamera(0, -90, 0);
                }
            }
        }
    }

}
