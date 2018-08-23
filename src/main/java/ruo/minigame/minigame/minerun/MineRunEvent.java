package ruo.minigame.minigame.minerun;

import net.minecraft.init.Items;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
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
import ruo.minigame.api.PosHelper;
import ruo.minigame.api.SpawnDirection;
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
    public void playerTick(PlayerEvent.ItemPickupEvent e) {

    }
    @SubscribeEvent
    public void playerTick(RenderGameOverlayEvent.Pre e) {
        if(e.getType() == RenderGameOverlayEvent.ElementType.ALL){

        }
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
                    e.player.motionY = ((fakePlayer.posY + 3 - (fakePlayer.isJumping() ? fakePlayer.motionY : 0)) - e.player.posY) * 0.3;
                    e.player.motionZ = MineRun.zCoord();
                    MineRun.setFakePositionUpdate();
                    fakePlayer.motionX = MineRun.xCoord();//걷는 모션을 주기 위해 있음 - 7월 14일
                    fakePlayer.motionZ = MineRun.zCoord();
                }
            }
            if (MineRun.elytraMode() == 1) {
                //엘리트라 모드1은 아래에서 위를 봄
                //플레이어를 페이크 아래에 배치함
                e.player.motionY = 0.1;
                MineRun.setFakePositionUpdate();

                fakePlayer.fallDistance = 0;
            }
            if (fakePlayer.fallDistance > 1242345 && e.player.getBedLocation(0) != null) {//추락시 스폰지점으로 - 7월 14일
                fakePlayer.fallDistance = 0;
                ActionEffect.teleportSpawnPoint(WorldAPI.getPlayer());
                MineRun.setFakePositionUpdate();
            }
        }
    }

    @SubscribeEvent
    public void keyInput(KeyInputEvent e) {
        if (!MiniGame.minerun.isStart())
            return;
        EntityFakePlayer fakePlayer = FakePlayerHelper.fakePlayer;
        PosHelper posHelper = new PosHelper(WorldAPI.getPlayer());
        if (DebAPI.isKeyDown(Keyboard.KEY_V)) {//엘리트라 모드로 변경함
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
                MineRun.setPosition(posHelper.getXZ(SpawnDirection.LEFT, absLR() , false));
            }
            if (lineLR > -1 && DebAPI.isKeyDown(Keyboard.KEY_D) && Keyboard.getEventKeyState()) {
                lineLR--;
                MineRun.setPosition(posHelper.getXZ(SpawnDirection.RIGHT, absLR(), false));
            }
        }
        if (MineRun.elytraMode() == 1) {
            if (lineFB < 1 && DebAPI.isKeyDown(Keyboard.KEY_W) && Keyboard.getEventKeyState()) {
                lineFB++;
                //abs는 앞으로만 이동하게 하기 위해서 함
                MineRun.setPosition(EntityAPI.forwardX(WorldAPI.getPlayer(), absFB(), false), 0, EntityAPI.forwardZ(WorldAPI.getPlayer(), absFB(), false));
                if(lineLR == 1){
                    MineRun.setPosition(posHelper.getXZ(SpawnDirection.FORWARD_LEFT, absLR(), false));
                }
                if(lineLR == -1){
                    MineRun.setPosition(posHelper.getXZ(SpawnDirection.FORWARD_RIGHT, absLR(), false));
                }
            }
            if (lineFB > -1 && DebAPI.isKeyDown(Keyboard.KEY_S) && Keyboard.getEventKeyState()) {
                lineFB--;
                MineRun.setPosition(EntityAPI.backX(WorldAPI.getPlayer(), absFB(), false), 0, EntityAPI.backZ(WorldAPI.getPlayer(), absFB(), false));
                if(lineLR == 1){
                    MineRun.setPosition(posHelper.getXZ(SpawnDirection.BACK_LEFT, absLR(), false));
                }
                if(lineLR == -1){
                    MineRun.setPosition(posHelper.getXZ(SpawnDirection.BACK_RIGHT, absLR(), false));
                }
            }
            if (lineLR < 1 && DebAPI.isKeyDown(Keyboard.KEY_A) && Keyboard.getEventKeyState()) {
                lineLR++;
                MineRun.setPosition(posHelper.getXZ(SpawnDirection.LEFT, absLR(), false));
                if(lineFB == 1){
                    MineRun.setPosition(posHelper.getXZ(SpawnDirection.FORWARD_LEFT, absLR(), false));
                }
                if(lineFB == -1){
                    MineRun.setPosition(posHelper.getXZ(SpawnDirection.BACK_LEFT, absLR(), false));
                }
            }
            if (lineLR > -1 && DebAPI.isKeyDown(Keyboard.KEY_D) && Keyboard.getEventKeyState()) {
                lineLR--;
                MineRun.setPosition(posHelper.getXZ(SpawnDirection.RIGHT, absLR(), false));

                if(lineFB == 1){
                    MineRun.setPosition(posHelper.getXZ(SpawnDirection.FORWARD_LEFT, absLR(), false));
                }
                if(lineFB == -1){
                    MineRun.setPosition(posHelper.getXZ(SpawnDirection.FORWARD_RIGHT, absLR(), false));
                }
            }
            System.out.println(lineLR+" - "+lineFB);

        }
        if (MineRun.elytraMode() == 0) {
            if (lineLR < 1 && DebAPI.isKeyDown(Keyboard.KEY_A) && Keyboard.getEventKeyState()) {
                lineLR++;
                MineRun.setPosition(posHelper.getXZ(SpawnDirection.LEFT, absLR() * 1.5, false));
            }
            if (lineLR > -1 && DebAPI.isKeyDown(Keyboard.KEY_D) && Keyboard.getEventKeyState()) {
                lineLR--;
                MineRun.setPosition(posHelper.getXZ(SpawnDirection.RIGHT, absLR() * 1.5, false));
            }
            System.out.println("LINELR "+lineLR * 1.5);
            System.out.println("LEFT "+posHelper.getXZ(SpawnDirection.LEFT, absLR() * 1.5, false));
            System.out.println("RIGHT "+posHelper.getXZ(SpawnDirection.RIGHT, absLR() * 1.5, false));
        }
        if (DebAPI.isKeyDown(Keyboard.KEY_SPACE) && Keyboard.getEventKeyState()) {
            fakePlayer.jump();
        }
    }

    public int absFB(){
        return Math.abs(lineFB);
    }
    public int absLR(){
        return Math.abs(lineLR);
    }
    @SubscribeEvent
    public void logout(PlayerEvent.PlayerLoggedOutEvent e) {
        MiniGame.minerun.end();
    }
}
