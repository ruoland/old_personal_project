package ruo.minigame.minigame.minerun;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBasePressurePlate;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import org.lwjgl.input.Keyboard;
import ruo.cmplus.cm.CommandChat;
import ruo.cmplus.deb.DebAPI;
import ruo.minigame.MiniGame;
import ruo.minigame.action.ActionEffect;
import ruo.minigame.api.BlockAPI;
import ruo.minigame.api.EntityAPI;
import ruo.minigame.api.WorldAPI;
import ruo.minigame.fakeplayer.EntityFakePlayer;
import ruo.minigame.fakeplayer.FakePlayerHelper;

public class MineRunEvent {
    //elytra 1
    public int lineLR = 0, lineFB = 0;

    //elytra 2
    public int lineUD = 0;

    //line은 왼쪽라인 오른쪽라인으로 갈 수 있는 값을 담고 있음, FB는 Forward Back 앞뒤 값 말함
    public double lineX, lineZ, lineFBX, lineFBZ;
    public double spawnX, spawnY, spawnZ;

    @SubscribeEvent
    public void playerTick(LivingEvent.LivingUpdateEvent e) {

    }

    @SubscribeEvent
    public void playerTick(PlayerTickEvent e) {
        if (!MiniGame.minerun.isStart() || FakePlayerHelper.fakePlayer == null)
            return;
        if (e.phase == Phase.END) {
            EntityFakePlayer fakePlayer = FakePlayerHelper.fakePlayer;
            if (MineRun.elytraMode() == 0 || MineRun.elytraMode() == 2) {//플레이어를 페이크 위에 갖다 놓음
                if (MineRun.elytraMode() == 2)
                    fakePlayer.motionY = 0;
                if (e.player.getDistanceToEntity(FakePlayerHelper.fakePlayer) > 6) {
                    //WorldAPI.teleport(fakePlayer.posX + EntityAPI.lookX(fakePlayer, -4), fakePlayer.posY + 1, fakePlayer.posZ + EntityAPI.lookZ(fakePlayer, -4), e.player.getHorizontalFacing().getHorizontalAngle(), 70);
                }
                if (fakePlayer.isNotColliding() && !FakePlayerHelper.fakePlayer.isCollidedHorizontally) {
                    e.player.motionX = MiniGame.minerun.xCoord();
                    e.player.motionY = (fakePlayer.posY + 3) - e.player.posY;
                    e.player.motionZ = MiniGame.minerun.zCoord();
                }
                fakePlayer.setPosition(e.player.posX + MiniGame.minerun.curX + EntityAPI.lookX(e.player, 3), fakePlayer.posY + MiniGame.minerun.curY, e.player.posZ + MiniGame.minerun.curZ + EntityAPI.lookZ(e.player, 3));
                if (MiniGame.minerun.curY != 0) {
                    fakePlayer.setPosition(e.player.posX + MiniGame.minerun.curX + EntityAPI.lookX(e.player, 3), fakePlayer.posY + MiniGame.minerun.curY, e.player.posZ + MiniGame.minerun.curZ + EntityAPI.lookZ(e.player, 3));
                    MiniGame.minerun.curY = 0;
                }
                if (WorldAPI.equalsHeldItem(Items.APPLE)) {
                    System.out.println("Y  " + (fakePlayer.posY) + " - " + MiniGame.minerun.curY);
                    //System.out.println("X  " + (e.player.posX + MiniGame.minerun.curX + EntityAPI.lookX(e.player, 3)) + " - " + MiniGame.minerun.curX + " - " + EntityAPI.lookX(e.player, 3));
                    //System.out.println("Z " + (e.player.posZ + MiniGame.minerun.curZ + EntityAPI.lookZ(e.player, 3)) + " - " + MiniGame.minerun.curZ + " - " + EntityAPI.lookZ(e.player, 3));
                }
                fakePlayer.motionX = MiniGame.minerun.xCoord() * speed;
                fakePlayer.motionZ = MiniGame.minerun.zCoord() * speed;
            }
            if (MineRun.elytraMode() == 1) {
                e.player.motionY = 0.2;
                e.player.rotationPitch = 70;
                fakePlayer.setPosition(e.player.posX + EntityAPI.lookX(fakePlayer, -4), e.player.posY + 3, e.player.posZ + EntityAPI.lookZ(fakePlayer, -4));
                FakePlayerHelper.fakePlayer.fallDistance = 0;
            }
            if (FakePlayerHelper.fakePlayer.fallDistance > 1242345 && e.player.getBedLocation() != null) {//추락시 스폰지점으로
                fakePlayer.fallDistance = 0;
                ActionEffect.teleportSpawnPoint(WorldAPI.getPlayer());
                fakePlayer.setPosition(e.player.posX + MiniGame.minerun.curX + EntityAPI.lookX(e.player, 3), fakePlayer.posY + MiniGame.minerun.curY, e.player.posZ + MiniGame.minerun.curZ + EntityAPI.lookZ(e.player, 3));
            }
        }
    }

    private double speed = 0.15, pspeed = 0.3;

    @SubscribeEvent
    public void keyInput(KeyInputEvent e) {
        if (!MiniGame.minerun.isStart() || FakePlayerHelper.fakePlayer == null)
            return;
        EntityFakePlayer fakePlayer = FakePlayerHelper.fakePlayer;
        if (DebAPI.isKeyDown(Keyboard.KEY_V)) {
            if (MineRun.elytraMode() == 2) {
                MiniGame.minerun.setElytra(1);
                System.out.println(MineRun.elytraMode());
                return;
            }
            if (MineRun.elytraMode() == 1) {
                MiniGame.minerun.setElytra(0);
                System.out.println(MineRun.elytraMode());
                return;
            }
            if (MineRun.elytraMode() == 0) {
                MiniGame.minerun.setElytra(2);
                System.out.println(MineRun.elytraMode());
                return;
            }
        }
        if (DebAPI.isKeyDown(Keyboard.KEY_O)) {
            speed += 0.01;
            System.out.println(speed);
        }
        if (DebAPI.isKeyDown(Keyboard.KEY_P)) {
            speed -= 0.01;
            System.out.println(speed);
        }
        if (DebAPI.isKeyDown(Keyboard.KEY_K)) {
            pspeed += 0.01;
            System.out.println(pspeed);
        }
        if (DebAPI.isKeyDown(Keyboard.KEY_L)) {
            pspeed -= 0.01;
            System.out.println(pspeed);
        }
        if (DebAPI.isKeyDown(Keyboard.KEY_N)) {
            pspeed = Double.valueOf(CommandChat.getLastChat());
        }
        if (DebAPI.isKeyDown(Keyboard.KEY_M)) {
            speed = Double.valueOf(CommandChat.getLastChat());
        }
        if (MineRun.elytraMode() == 2) {

            if (lineUD < 1 && DebAPI.isKeyDown(Keyboard.KEY_W) && Keyboard.getEventKeyState()) {
                MiniGame.minerun.setPosition(0, 1, 0);
                lineUD++;
                System.out.println(lineUD);
            }
            if (lineUD > -1 && DebAPI.isKeyDown(Keyboard.KEY_S) && Keyboard.getEventKeyState()) {
                MiniGame.minerun.setPosition(0, -1, 0);
                lineUD--;
                System.out.println(lineUD);
            }
            if (lineUD == 0) {
                if (DebAPI.isKeyDown(Keyboard.KEY_W))
                    MiniGame.minerun.setPosition(0, 1, 0);
                if(DebAPI.isKeyDown(Keyboard.KEY_S))
                    MiniGame.minerun.setPosition(0, -1, 0);
                return;
            }
            if (lineLR < 1 && DebAPI.isKeyDown(Keyboard.KEY_A) && Keyboard.getEventKeyState()) {
                lineLR++;
                MiniGame.minerun.setPosition(EntityAPI.forwardLeftX(WorldAPI.getPlayer(), lineLR, false), 0, EntityAPI.forwardLeftZ(WorldAPI.getPlayer(), lineLR, false));
            }
            if (lineLR > -1 && DebAPI.isKeyDown(Keyboard.KEY_D) && Keyboard.getEventKeyState()) {
                lineLR--;
                MiniGame.minerun.setPosition(EntityAPI.forwardRightX(WorldAPI.getPlayer(), Math.abs(lineLR), false), 0, EntityAPI.forwardRightZ(WorldAPI.getPlayer(), Math.abs(lineLR), false));
            }
        }
        if (MineRun.elytraMode() == 1) {
            if (lineFB < 1 && DebAPI.isKeyDown(Keyboard.KEY_W) && Keyboard.getEventKeyState()) {
                lineFB++;
                MiniGame.minerun.setPosition(EntityAPI.forwardRightX(WorldAPI.getPlayer(), Math.abs(lineFB), false), 0, EntityAPI.forwardRightZ(WorldAPI.getPlayer(), Math.abs(lineFB), false));
            }
            if (lineFB > -1 && DebAPI.isKeyDown(Keyboard.KEY_S) && Keyboard.getEventKeyState()) {
                lineFB--;
                MiniGame.minerun.setPosition(EntityAPI.forwardRightX(WorldAPI.getPlayer(), Math.abs(lineFB), false), 0, EntityAPI.forwardRightZ(WorldAPI.getPlayer(), Math.abs(lineFB), false));
            }
            if (lineLR < 1 && DebAPI.isKeyDown(Keyboard.KEY_A) && Keyboard.getEventKeyState()) {
                lineLR++;
                MiniGame.minerun.setPosition(EntityAPI.forwardLeftX(WorldAPI.getPlayer(), lineLR, false), 0, EntityAPI.forwardLeftZ(WorldAPI.getPlayer(), lineLR, false));
            }
            if (lineLR > -1 && DebAPI.isKeyDown(Keyboard.KEY_D) && Keyboard.getEventKeyState()) {
                lineLR--;
                MiniGame.minerun.setPosition(EntityAPI.forwardRightX(WorldAPI.getPlayer(), Math.abs(lineLR), false), 0, EntityAPI.forwardRightZ(WorldAPI.getPlayer(), Math.abs(lineLR), false));
            }
        }
        if (MineRun.elytraMode() == 0) {
            if (lineLR < 1 && DebAPI.isKeyDown(Keyboard.KEY_A) && Keyboard.getEventKeyState()) {
                lineLR++;
                MiniGame.minerun.setPosition(EntityAPI.forwardLeftX(WorldAPI.getPlayer(), lineLR * 3, false), 0, EntityAPI.forwardLeftZ(WorldAPI.getPlayer(), lineLR * 3, false));
                System.out.println(EntityAPI.forwardLeftX(WorldAPI.getPlayer(), lineLR * 3, false) + " - " + EntityAPI.forwardLeftZ(WorldAPI.getPlayer(), lineLR * 3, false));

            }
            if (lineLR > -1 && DebAPI.isKeyDown(Keyboard.KEY_D) && Keyboard.getEventKeyState()) {
                lineLR--;
                MiniGame.minerun.setPosition(EntityAPI.forwardRightX(WorldAPI.getPlayer(), Math.abs(lineLR) * 3, false), 0, EntityAPI.forwardRightZ(WorldAPI.getPlayer(), Math.abs(lineLR) * 3, false));
                System.out.println(EntityAPI.forwardRightX(WorldAPI.getPlayer(), Math.abs(lineLR) * 3, false) + " - " + EntityAPI.forwardRightZ(WorldAPI.getPlayer(), Math.abs(lineLR) * 3, false));

            }
            System.out.println(lineLR);
        }
        if (DebAPI.isKeyDown(Keyboard.KEY_SPACE) && Keyboard.getEventKeyState()) {
            fakePlayer.jump();

        }
    }

    @SubscribeEvent
    public void logout(WorldEvent.Unload e) {
        MiniGame.minerun.end();
    }
}
