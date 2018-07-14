package ruo.minigame.minigame.minerun;

import net.minecraft.init.Items;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import org.lwjgl.input.Keyboard;
import ruo.cmplus.deb.DebAPI;
import ruo.minigame.MiniGame;
import ruo.minigame.action.ActionEffect;
import ruo.minigame.api.EntityAPI;
import ruo.minigame.api.WorldAPI;
import ruo.minigame.fakeplayer.EntityFakePlayer;
import ruo.minigame.fakeplayer.FakePlayerHelper;

public class MineRunEvent {
    //엘리트라 모드1용 - 7월 14일
    public int lineLR = 0, lineFB = 0;

    //엘리트라 모드2용 - 7월 14일
    public int lineUD = 0;

    //line은 왼쪽라인 오른쪽라인으로 갈 수 있는 값을 담고 있음, FB는 Forward Back 앞뒤 값 말함 - 7월 14일
    protected double lineX, lineZ, lineFBX, lineFBZ;

    @SubscribeEvent
    public void playerTick(LivingEvent.LivingUpdateEvent e) {

    }

    @SubscribeEvent
    public void playerTick(PlayerTickEvent e) {
        if (!MiniGame.minerun.isStart())
            return;
        if (e.phase == Phase.END) {
            EntityFakePlayer fakePlayer = FakePlayerHelper.fakePlayer;
            if (MineRun.elytraMode() == 0 || MineRun.elytraMode() == 2) {//플레이어를 페이크 위에 갖다 놓음 - 7월 14일
                if (MineRun.elytraMode() == 2) {
                    fakePlayer.motionY = 0;//모드2 는 앞을 향해 날라가기 때문에 공중에 띄워줘야 함 - 7월 14일
                    //엘리트라 모드 2에서 위 아래로 움직이면 카메라도 같이 움직여서 고쳐야하는데 아직 안고침 - 7월 14일
                    //e.player.motionY = (fakePlayer.posY + 3 - MiniGame.minerun.curY) - e.player.posY; - 이거 안됨 움직일 때 어긋남 - 7월 14일
                }
                if (fakePlayer.isNotColliding() && !fakePlayer.isCollidedHorizontally) {//페이크 플레이어가 어딘가에 막힌 상태가 아닌 경우에만 - 7월 14일
                    e.player.motionX = MineRun.xCoord();//앞으로 나아가게 함 - 7월 14일
                    e.player.motionY = (fakePlayer.posY + 3) - e.player.posY;
                    e.player.motionZ = MineRun.zCoord();
                    MineRun.setPosition();
                    if (WorldAPI.equalsHeldItem(Items.APPLE)) {
                        System.out.println("Y  " + (fakePlayer.posY) + " - " + MineRun.curY);
                        //System.out.println("X  " + (e.player.posX + MineRun.curX + EntityAPI.lookX(e.player, 3)) + " - " + MineRun.curX + " - " + EntityAPI.lookX(e.player, 3));
                        //System.out.println("Z " + (e.player.posZ + MineRun.curZ + EntityAPI.lookZ(e.player, 3)) + " - " + MineRun.curZ + " - " + EntityAPI.lookZ(e.player, 3));
                    }
                    fakePlayer.motionX = MineRun.xCoord();//걷는 모션을 주기 위해 있음 - 7월 14일
                    fakePlayer.motionZ = MineRun.zCoord();
                }
            }
            if (MineRun.elytraMode() == 1) {
                //엘리트라 모드1은 아래에서 위를 봄 따라서 플레이어를 페이크 아래에 배치함
                e.player.motionY = 0.2;
                fakePlayer.setPosition(e.player.posX + EntityAPI.lookX(e.player, 3), e.player.posY + 3, e.player.posZ + EntityAPI.lookZ(e.player, 3));
                fakePlayer.fallDistance = 0;
            }
            if (fakePlayer.fallDistance > 1242345 && e.player.getBedLocation(0) != null) {//추락시 스폰지점으로 - 7월 14일
                fakePlayer.fallDistance = 0;
                ActionEffect.teleportSpawnPoint(WorldAPI.getPlayer());
                MineRun.setPosition();
            }
        }
    }

    @SubscribeEvent
    public void keyInput(KeyInputEvent e) {
        if (!MiniGame.minerun.isStart())
            return;
        EntityFakePlayer fakePlayer = FakePlayerHelper.fakePlayer;
        if (DebAPI.isKeyDown(Keyboard.KEY_V)) {
            if (MineRun.elytraMode() == 2) {
                MineRun.setElytra(1);
                System.out.println(MineRun.elytraMode());
                return;
            }
            if (MineRun.elytraMode() == 1) {
                MineRun.setElytra(0);
                System.out.println(MineRun.elytraMode());
                return;
            }
            if (MineRun.elytraMode() == 0) {
                MineRun.setElytra(2);
                System.out.println(MineRun.elytraMode());
                return;
            }
        }
        if (MineRun.elytraMode() == 2) {
            if (lineUD < 1 && DebAPI.isKeyDown(Keyboard.KEY_W) && Keyboard.getEventKeyState()) {
                MineRun.setPosition(MineRun.curX, 1, MineRun.curZ);
                lineUD++;
                System.out.println(lineUD);
            }
            if (lineUD > -1 && DebAPI.isKeyDown(Keyboard.KEY_S) && Keyboard.getEventKeyState()) {
                MineRun.setPosition(MineRun.curX, -1, MineRun.curZ);
                lineUD--;
                System.out.println(lineUD);
            }
            if (lineUD == 0) {
                if (DebAPI.isKeyDown(Keyboard.KEY_W))
                    MineRun.setPosition(MineRun.curX, 1, MineRun.curZ);
                if (DebAPI.isKeyDown(Keyboard.KEY_S))
                    MineRun.setPosition(MineRun.curX, -1, MineRun.curZ);
            }
            if (lineLR < 1 && DebAPI.isKeyDown(Keyboard.KEY_A) && Keyboard.getEventKeyState()) {
                lineLR++;
                MineRun.setPosition(EntityAPI.forwardLeftX(WorldAPI.getPlayer(), lineLR, false), 0, EntityAPI.forwardLeftZ(WorldAPI.getPlayer(), lineLR, false));
            }
            if (lineLR > -1 && DebAPI.isKeyDown(Keyboard.KEY_D) && Keyboard.getEventKeyState()) {
                lineLR--;
                MineRun.setPosition(EntityAPI.forwardRightX(WorldAPI.getPlayer(), Math.abs(lineLR), false), 0, EntityAPI.forwardRightZ(WorldAPI.getPlayer(), Math.abs(lineLR), false));
            }
        }
        if (MineRun.elytraMode() == 1) {
            if (lineFB < 1 && DebAPI.isKeyDown(Keyboard.KEY_W) && Keyboard.getEventKeyState()) {
                lineFB++;
                MineRun.setPosition(EntityAPI.forwardX(WorldAPI.getPlayer(), Math.abs(lineFB), false), 0, EntityAPI.forwardZ(WorldAPI.getPlayer(), Math.abs(lineFB), false));
            }
            if (lineFB > -1 && DebAPI.isKeyDown(Keyboard.KEY_S) && Keyboard.getEventKeyState()) {
                lineFB--;
                MineRun.setPosition(EntityAPI.backX(WorldAPI.getPlayer(), Math.abs(lineFB), false), 0, EntityAPI.backZ(WorldAPI.getPlayer(), Math.abs(lineFB), false));
            }
            if (lineLR < 1 && DebAPI.isKeyDown(Keyboard.KEY_A) && Keyboard.getEventKeyState()) {
                lineLR++;
                MineRun.setPosition(EntityAPI.forwardLeftX(WorldAPI.getPlayer(), lineLR, false), 0, EntityAPI.forwardLeftZ(WorldAPI.getPlayer(), lineLR, false));
            }
            if (lineLR > -1 && DebAPI.isKeyDown(Keyboard.KEY_D) && Keyboard.getEventKeyState()) {
                lineLR--;
                MineRun.setPosition(EntityAPI.forwardRightX(WorldAPI.getPlayer(), Math.abs(lineLR), false), 0, EntityAPI.forwardRightZ(WorldAPI.getPlayer(), Math.abs(lineLR), false));
            }
        }
        if (MineRun.elytraMode() == 0) {
            if (lineLR < 1 && DebAPI.isKeyDown(Keyboard.KEY_A) && Keyboard.getEventKeyState()) {
                lineLR++;
                MineRun.setPosition(EntityAPI.forwardLeftX(WorldAPI.getPlayer(), lineLR * 3, false), 0, EntityAPI.forwardLeftZ(WorldAPI.getPlayer(), lineLR * 3, false));
                System.out.println(EntityAPI.forwardLeftX(WorldAPI.getPlayer(), lineLR * 3, false) + " - " + EntityAPI.forwardLeftZ(WorldAPI.getPlayer(), lineLR * 3, false));

            }
            if (lineLR > -1 && DebAPI.isKeyDown(Keyboard.KEY_D) && Keyboard.getEventKeyState()) {
                lineLR--;
                MineRun.setPosition(EntityAPI.forwardRightX(WorldAPI.getPlayer(), Math.abs(lineLR) * 3, false), 0, EntityAPI.forwardRightZ(WorldAPI.getPlayer(), Math.abs(lineLR) * 3, false));
                System.out.println(EntityAPI.forwardRightX(WorldAPI.getPlayer(), Math.abs(lineLR) * 3, false) + " - " + EntityAPI.forwardRightZ(WorldAPI.getPlayer(), Math.abs(lineLR) * 3, false));

            }
            System.out.println(lineLR);
        }
        if (DebAPI.isKeyDown(Keyboard.KEY_SPACE) && Keyboard.getEventKeyState()) {
            fakePlayer.jump();
        }
    }

    @SubscribeEvent
    public void logout(PlayerEvent.PlayerLoggedOutEvent e) {
        MiniGame.minerun.end();
    }
}
