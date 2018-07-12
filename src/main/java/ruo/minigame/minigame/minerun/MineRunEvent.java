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
                if(e.player.getDistanceToEntity(FakePlayerHelper.fakePlayer) > 6){
                    //WorldAPI.teleport(fakePlayer.posX + EntityAPI.lookX(fakePlayer, -4), fakePlayer.posY + 1, fakePlayer.posZ + EntityAPI.lookZ(fakePlayer, -4), e.player.getHorizontalFacing().getHorizontalAngle(), 70);
                }
                if(FakePlayerHelper.fakePlayer.isNotColliding() && !FakePlayerHelper.fakePlayer.isCollidedHorizontally && e.player.getDistanceToEntity(FakePlayerHelper.fakePlayer) > 3) {
                    e.player.motionX = MiniGame.minerun.xCoord() * pspeed;
                    e.player.motionY = (MiniGame.minerun.curY + 3) - e.player.posY;
                    e.player.motionZ = MiniGame.minerun.zCoord() * pspeed;
                }else {
                    e.player.motionX = 0;
                    e.player.motionZ = 0;
                    e.player.motionY = (fakePlayer.posY + 3) - e.player.posY;
                }
                FakePlayerHelper.fakePlayer.setPosition(e.player.posX+MiniGame.minerun.curX+EntityAPI.lookX(e.player, 3), fakePlayer.posY+MiniGame.minerun.curY, e.player.posZ+MiniGame.minerun.curZ+EntityAPI.lookZ(e.player, 3));
                FakePlayerHelper.fakePlayer.motionX = MiniGame.minerun.xCoord() * speed;
                FakePlayerHelper.fakePlayer.motionZ = MiniGame.minerun.zCoord() * speed;
            }
            if(MineRun.elytraMode() == 1){
                if (lineZ == 0)
                    WorldAPI.teleport(e.player.posX, fakePlayer.posY - 3, fakePlayer.posZ + EntityAPI.lookZ(fakePlayer, -4), fakePlayer.getHorizontalFacing().getHorizontalAngle(), 70);
                else
                    WorldAPI.teleport(fakePlayer.posX + EntityAPI.lookX(fakePlayer, -4), fakePlayer.posY - 3, e.player.posZ, fakePlayer.getHorizontalFacing().getHorizontalAngle(), 70);
                FakePlayerHelper.fakePlayer.motionY = 0.2;
                FakePlayerHelper.fakePlayer.fallDistance = 0;
            }
            if (FakePlayerHelper.fakePlayer.fallDistance > 1242345 && e.player.getBedLocation() != null) {//추락시 스폰지점으로
                FakePlayerHelper.fakePlayer.fallDistance = 0;
                MiniGame.minerun.setPosition(e.player.getBedLocation().getX(), e.player.getBedLocation().getY(), e.player.getBedLocation().getZ());
                //ActionEffect.teleportSpawnPoint(e.player);
                WorldAPI.teleport(fakePlayer.posX + EntityAPI.lookX(fakePlayer, -2), MiniGame.minerun.curY, fakePlayer.posZ + EntityAPI.lookZ(fakePlayer, -2), fakePlayer.rotationYaw, 70);
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
            speed+=0.01;
            System.out.println(speed);
        }
        if (DebAPI.isKeyDown(Keyboard.KEY_P)) {
            speed-=0.01;
            System.out.println(speed);
        }
        if (DebAPI.isKeyDown(Keyboard.KEY_K)) {
            pspeed+=0.01;
            System.out.println(pspeed);
        }
        if (DebAPI.isKeyDown(Keyboard.KEY_L)) {
            pspeed-=0.01;
            System.out.println(pspeed);
        }
        if (DebAPI.isKeyDown(Keyboard.KEY_N)) {
            pspeed=Double.valueOf(CommandChat.getLastChat());
        }
        if (DebAPI.isKeyDown(Keyboard.KEY_M)) {
            speed=Double.valueOf(CommandChat.getLastChat());
        }
        if (MineRun.elytraMode() == 2) {
            if (lineUD < 1 && DebAPI.isKeyDown(Keyboard.KEY_W) && Keyboard.getEventKeyState()) {
                MiniGame.minerun.setPosition(0, 0 + 1, 0);
                lineUD++;
                System.out.println(lineUD);
            }
            if (lineUD > -1 && DebAPI.isKeyDown(Keyboard.KEY_S) && Keyboard.getEventKeyState()) {
                MiniGame.minerun.setPosition(0, 0 - 1, 0);
                lineUD--;
                System.out.println(lineUD);
            }
            if (DebAPI.isKeyDown(Keyboard.KEY_W) || DebAPI.isKeyDown(Keyboard.KEY_S)) {
                if (lineUD == 0)
                    MiniGame.minerun.setPosition(0, 0 + lineUD, 0);
            }
            if (lineLR < 1 && DebAPI.isKeyDown(Keyboard.KEY_A) && Keyboard.getEventKeyState()) {
                MiniGame.minerun.setPosition(0 + lineX, 0, 0 + lineZ);
                lineLR++;
            }
            if (lineLR > -1 && DebAPI.isKeyDown(Keyboard.KEY_D) && Keyboard.getEventKeyState()) {
                MiniGame.minerun.setPosition(0 - lineX, 0, 0 - lineZ);
                lineLR--;
            }
        }
        if (MineRun.elytraMode() == 1) {
            if (lineFB < 1 && DebAPI.isKeyDown(Keyboard.KEY_W) && Keyboard.getEventKeyState()) {
                MiniGame.minerun.setPosition(0 + lineFBX, 0, 0 + lineFBZ);
                lineFB++;
            }
            if (lineFB > -1 && DebAPI.isKeyDown(Keyboard.KEY_S) && Keyboard.getEventKeyState()) {
                MiniGame.minerun.setPosition(0 - lineFBX, 0, 0 - lineFBZ);
                lineFB--;
            }
            if (lineLR < 1 && DebAPI.isKeyDown(Keyboard.KEY_A) && Keyboard.getEventKeyState()) {
                MiniGame.minerun.setPosition(0 + lineX, 0, 0 + lineZ);
                lineLR++;
            }
            if (lineLR > -1 && DebAPI.isKeyDown(Keyboard.KEY_D) && Keyboard.getEventKeyState()) {
                MiniGame.minerun.setPosition(0 - lineX, 0, 0 - lineZ);
                lineLR--;
            }
        } else if (MineRun.elytraMode() == 0) {
            if (lineLR < 1 && DebAPI.isKeyDown(Keyboard.KEY_A) && Keyboard.getEventKeyState()) {
                MiniGame.minerun.setPosition(0 + (lineX * 2), 0, 0 + (lineZ * 2));
                lineLR++;
            }
            if (lineLR > -1 && DebAPI.isKeyDown(Keyboard.KEY_D) && Keyboard.getEventKeyState()) {
                MiniGame.minerun.setPosition(0 - (lineX * 2), 0, 0 - (lineZ * 2));
                lineLR--;
            }
            System.out.println(lineLR);
        }
        if (DebAPI.isKeyDown(Keyboard.KEY_SPACE) && Keyboard.getEventKeyState()) {
            fakePlayer.jump();
            MiniGame.minerun.curY++;
        }
    }

    @SubscribeEvent
    public void logout(WorldEvent.Unload e) {
        MiniGame.minerun.end();
    }
}
