package ruo.minigame.minigame.minerun;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import org.lwjgl.input.Keyboard;
import ruo.cmplus.deb.DebAPI;
import ruo.minigame.MiniGame;
import ruo.minigame.action.ActionEffect;
import ruo.minigame.api.*;
import ruo.minigame.fakeplayer.EntityFakePlayer;
import ruo.minigame.fakeplayer.FakePlayerHelper;

public class MineRunEvent {
    //엘리트라 모드1용 - 7월 14일
    public int lineLR = 0, lineFB = 0;

    //엘리트라 모드2용 - 7월 14일
    public int lineUD = 0;

    //line은 왼쪽라인 오른쪽라인으로 갈 수 있는 값을 담고 있음, FB는 Forward Back 앞뒤 값 말함 - 7월 14일
    protected double lineX, lineZ, lineFBX, lineFBZ;

    private ItemStack stack = new ItemStack(Items.NETHER_STAR);
    private int pickupCount;

    @SubscribeEvent
    public void itemPickup(EntityItemPickupEvent e) {
        if (e.getItem().getEntityItem().getItem() == Items.NETHER_STAR) {
            pickupCount++;
            e.setCanceled(true);
            EntityItem item = e.getItem();
            item.setPosition(item.posX, item.posY + 2, item.posZ);
        }
        if (e.getItem().getEntityItem().getItem() == Items.POTIONITEM) {
            e.setCanceled(true);
            EntityItem item = e.getItem();
            item.setPosition(item.posX, item.posY + 2, item.posZ);
            e.getEntityPlayer().heal(3);
        }
    }

    @SubscribeEvent
    public void renderBlockOverlay(RenderBlockOverlayEvent e) {
        e.setCanceled(MiniGame.minerun.isStart());
    }

    @SubscribeEvent
    public void playerTick(PlayerTickEvent e) {
        if (!MiniGame.minerun.isStart())
            return;
        EntityFakePlayer fakePlayer = FakePlayerHelper.fakePlayer;
        if (MineRun.elytraMode() == 0) {
            PosHelper posHelper = new PosHelper(e.player);
            Vec3d vec3i = posHelper.getXZ(Direction.BACK, 3, false);
            BlockPos pos = e.player.getPosition().add(vec3i.xCoord, vec3i.yCoord + 4, vec3i.zCoord);
            if (!e.player.isInLava() && !e.player.isInWater()) {
                e.player.motionX = MineRun.xCoord();//앞으로 나아가게 함 - 7월 14일
                e.player.motionZ = MineRun.zCoord();
            }
        }
        if (MineRun.elytraMode() > 0 && FakePlayerHelper.fakePlayer != null) {
            if (MineRun.elytraMode() == 2) {
                fakePlayer.motionY = 0;
                MineRun.setFakePositionUpdate();
                fakePlayer.motionX = MineRun.xCoord();//걷는 모션을 주기 위해 있음 - 7월 14일
                fakePlayer.motionZ = MineRun.zCoord();
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
        if (MineRun.elytraMode() > 0 && FakePlayerHelper.fakePlayer != null) {
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
                    MineRun.setPosition(posHelper.getXZ(Direction.LEFT, absLR(), false));
                }
                if (lineLR > -1 && DebAPI.isKeyDown(Keyboard.KEY_D) && Keyboard.getEventKeyState()) {
                    lineLR--;
                    MineRun.setPosition(posHelper.getXZ(Direction.RIGHT, absLR(), false));
                }
            }
            if (MineRun.elytraMode() == 1) {
                if (lineFB < 1 && DebAPI.isKeyDown(Keyboard.KEY_W) && Keyboard.getEventKeyState()) {
                    lineFB++;
                    //abs는 앞으로만 이동하게 하기 위해서 함
                    MineRun.setPosition(EntityAPI.forwardX(WorldAPI.getPlayer(), absFB(), false), 0, EntityAPI.forwardZ(WorldAPI.getPlayer(), absFB(), false));
                    if (lineLR == 1) {
                        MineRun.setPosition(posHelper.getXZ(Direction.FORWARD_LEFT, absLR(), false));
                    }
                    if (lineLR == -1) {
                        MineRun.setPosition(posHelper.getXZ(Direction.FORWARD_RIGHT, absLR(), false));
                    }
                }
                if (lineFB > -1 && DebAPI.isKeyDown(Keyboard.KEY_S) && Keyboard.getEventKeyState()) {
                    lineFB--;
                    MineRun.setPosition(EntityAPI.backX(WorldAPI.getPlayer(), absFB(), false), 0, EntityAPI.backZ(WorldAPI.getPlayer(), absFB(), false));
                    if (lineLR == 1) {
                        MineRun.setPosition(posHelper.getXZ(Direction.BACK_LEFT, absLR(), false));
                    }
                    if (lineLR == -1) {
                        MineRun.setPosition(posHelper.getXZ(Direction.BACK_RIGHT, absLR(), false));
                    }
                }
                if (lineLR < 1 && DebAPI.isKeyDown(Keyboard.KEY_A) && Keyboard.getEventKeyState()) {
                    lineLR++;
                    MineRun.setPosition(posHelper.getXZ(Direction.LEFT, absLR(), false));
                    if (lineFB == 1) {
                        MineRun.setPosition(posHelper.getXZ(Direction.FORWARD_LEFT, absLR(), false));
                    }
                    if (lineFB == -1) {
                        MineRun.setPosition(posHelper.getXZ(Direction.BACK_LEFT, absLR(), false));
                    }
                }
                if (lineLR > -1 && DebAPI.isKeyDown(Keyboard.KEY_D) && Keyboard.getEventKeyState()) {
                    lineLR--;
                    MineRun.setPosition(posHelper.getXZ(Direction.RIGHT, absLR(), false));

                    if (lineFB == 1) {
                        MineRun.setPosition(posHelper.getXZ(Direction.FORWARD_LEFT, absLR(), false));
                    }
                    if (lineFB == -1) {
                        MineRun.setPosition(posHelper.getXZ(Direction.FORWARD_RIGHT, absLR(), false));
                    }
                }
                System.out.println(lineLR + " - " + lineFB);

            }
        }
        if (MineRun.elytraMode() == 0) {
            System.out.println(lineLR + " - " + DebAPI.isKeyDown(Keyboard.KEY_A) + " - " + DebAPI.isKeyDown(Keyboard.KEY_D));
            if (lineLR < 1 && DebAPI.isKeyDown(Keyboard.KEY_A)) {
                lineLR++;
                boolean isLR = false;
                if (lineLR == 0) {
                    lineLR++;
                    isLR = true;
                }
                MineRun.setPosition(posHelper.getXZ(Direction.LEFT, absLR() * 2, false));
                if (isLR)
                    lineLR--;
                System.out.println("LINELR " + lineLR * 2);
                System.out.println("LEFT " + posHelper.getXZ(Direction.LEFT, absLR() * 2, false));
            }

            if (lineLR > -1 && DebAPI.isKeyDown(Keyboard.KEY_D)) {
                lineLR--;
                boolean isLR = false;
                if (lineLR == 0) {//가운데로 보내기 위해서 1 깎음
                    lineLR--;
                    isLR = true;
                }
                MineRun.setPosition(posHelper.getXZ(Direction.RIGHT, absLR() * 2, false));
                if (isLR)
                    lineLR++;
                System.out.println("LINELR " + lineLR * 2);
                System.out.println("RIGHT " + posHelper.getXZ(Direction.RIGHT, absLR() * 2, false));
            }

        }

    }

    public int absFB() {
        return Math.abs(lineFB);
    }

    public int absLR() {
        return Math.abs(lineLR);
    }

    @SubscribeEvent
    public void logout(PlayerEvent.PlayerLoggedOutEvent e) {
        MiniGame.minerun.end();
    }
}
