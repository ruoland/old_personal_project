package ruo.minigame.minigame.minerun;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.royawesome.jlibnoise.module.combiner.Min;
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
    public void playerTick(PlayerEvent.ItemPickupEvent e) {
        if (e.pickedUp.getEntityItem().getItem() == Items.NETHER_STAR) {
            pickupCount++;
        }
    }

    @SubscribeEvent
    public void playerTick(RenderGameOverlayEvent.Pre e) {
        if (MiniGame.minerun.isStart()) {
            if (e.getType() == RenderGameOverlayEvent.ElementType.ALL) {
                Minecraft mc = Minecraft.getMinecraft();
                int width = e.getResolution().getScaledWidth();
                int height = e.getResolution().getScaledHeight();
                RenderItem itemRender = Minecraft.getMinecraft().getRenderItem();
                itemRender.zLevel = 200.0F;
                if (stack != null) {
                    RenderHelper.enableGUIStandardItemLighting();
                    itemRender.renderItemAndEffectIntoGUI(stack, (width), (height));
                    RenderHelper.disableStandardItemLighting();
                }
                itemRender.zLevel = 0.0F;
            }
        }
    }

    @SubscribeEvent
    public void playerTick(PlayerTickEvent e) {
        if (!MiniGame.minerun.isStart())
            return;
        EntityFakePlayer fakePlayer = FakePlayerHelper.fakePlayer;
        if (MineRun.elytraMode() == 0) {

            e.player.motionX = MineRun.xCoord();//앞으로 나아가게 함 - 7월 14일
            e.player.motionZ = MineRun.zCoord();
        }
        if (MineRun.elytraMode() > 0) {
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
                MineRun.setPosition(posHelper.getXZ(SpawnDirection.LEFT, absLR(), false));
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
                if (lineLR == 1) {
                    MineRun.setPosition(posHelper.getXZ(SpawnDirection.FORWARD_LEFT, absLR(), false));
                }
                if (lineLR == -1) {
                    MineRun.setPosition(posHelper.getXZ(SpawnDirection.FORWARD_RIGHT, absLR(), false));
                }
            }
            if (lineFB > -1 && DebAPI.isKeyDown(Keyboard.KEY_S) && Keyboard.getEventKeyState()) {
                lineFB--;
                MineRun.setPosition(EntityAPI.backX(WorldAPI.getPlayer(), absFB(), false), 0, EntityAPI.backZ(WorldAPI.getPlayer(), absFB(), false));
                if (lineLR == 1) {
                    MineRun.setPosition(posHelper.getXZ(SpawnDirection.BACK_LEFT, absLR(), false));
                }
                if (lineLR == -1) {
                    MineRun.setPosition(posHelper.getXZ(SpawnDirection.BACK_RIGHT, absLR(), false));
                }
            }
            if (lineLR < 1 && DebAPI.isKeyDown(Keyboard.KEY_A) && Keyboard.getEventKeyState()) {
                lineLR++;
                MineRun.setPosition(posHelper.getXZ(SpawnDirection.LEFT, absLR(), false));
                if (lineFB == 1) {
                    MineRun.setPosition(posHelper.getXZ(SpawnDirection.FORWARD_LEFT, absLR(), false));
                }
                if (lineFB == -1) {
                    MineRun.setPosition(posHelper.getXZ(SpawnDirection.BACK_LEFT, absLR(), false));
                }
            }
            if (lineLR > -1 && DebAPI.isKeyDown(Keyboard.KEY_D) && Keyboard.getEventKeyState()) {
                lineLR--;
                MineRun.setPosition(posHelper.getXZ(SpawnDirection.RIGHT, absLR(), false));

                if (lineFB == 1) {
                    MineRun.setPosition(posHelper.getXZ(SpawnDirection.FORWARD_LEFT, absLR(), false));
                }
                if (lineFB == -1) {
                    MineRun.setPosition(posHelper.getXZ(SpawnDirection.FORWARD_RIGHT, absLR(), false));
                }
            }
            System.out.println(lineLR + " - " + lineFB);

        }
        if (MineRun.elytraMode() == 0) {
            if (lineLR < 1 && DebAPI.isKeyDown(Keyboard.KEY_A) && Keyboard.getEventKeyState()) {
                lineLR++;
                MineRun.setPosition(posHelper.getXZ(SpawnDirection.LEFT, absLR() * 1.5, false));
                System.out.println("LINELR " + lineLR * 1.5);
                System.out.println("LEFT " + posHelper.getXZ(SpawnDirection.LEFT, absLR() * 1.5, false));
            }
            if (lineLR > -1 && DebAPI.isKeyDown(Keyboard.KEY_D) && Keyboard.getEventKeyState()) {
                lineLR--;
                MineRun.setPosition(posHelper.getXZ(SpawnDirection.RIGHT, absLR() * 1.5, false));
                System.out.println("LINELR " + lineLR * 1.5);
                System.out.println("RIGHT " + posHelper.getXZ(SpawnDirection.RIGHT, absLR() * 1.5, false));
            }
        }
        if (FakePlayerHelper.fakePlayer != null && DebAPI.isKeyDown(Keyboard.KEY_SPACE) && Keyboard.getEventKeyState()) {
            FakePlayerHelper.fakePlayer.jump();
        }
        System.out.println(MineRun.elytraMode());
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
