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

    //line은 왼쪽라인 오른쪽라인으로 갈 수 있는 값을 담고 있음
    //
    public double lineX, lineZ, lineFBX, lineFBZ;
    public double spawnX, spawnY, spawnZ;
    @SubscribeEvent
    public void playerTick(PlayerTickEvent e) {
        if (!MiniGame.minerun.isStart() || FakePlayerHelper.fakePlayer == null)
            return;

            if (e.phase == Phase.END) {
                EntityFakePlayer fakePlayer = FakePlayerHelper.fakePlayer;
                if(lineZ == 0)
                    WorldAPI.teleport(e.player.posX, fakePlayer.posY + 1, fakePlayer.posZ + EntityAPI.lookZ(fakePlayer, -4), fakePlayer.getHorizontalFacing().getHorizontalAngle(), 30);
                else
                    WorldAPI.teleport(fakePlayer.posX+EntityAPI.lookX(fakePlayer, -4), fakePlayer.posY + 1, e.player.posZ, fakePlayer.getHorizontalFacing().getHorizontalAngle(), 30);

                if (MiniGame.minerun.elytraMode() == 2) {
                    FakePlayerHelper.fakePlayer.motionX = EntityAPI.lookX(e.player, 0.08);
                    FakePlayerHelper.fakePlayer.motionZ = EntityAPI.lookZ(e.player, 0.08);
                }
                if (FakePlayerHelper.fakePlayer.fallDistance > 5) {
                    FakePlayerHelper.fakePlayer.fallDistance = 0;
                    FakePlayerHelper.fakePlayer.setPosition(e.player.getBedLocation());
                    ActionEffect.teleportSpawnPoint(e.player);
                    //WorldAPI.teleport(MiniGame.minerun.getTeleportPos(), MiniGame.minerun.getYaw(), -70);
                }
                if (MineRun.elytraMode() == 1) {
                    FakePlayerHelper.fakePlayer.motionY = 0.2;
                    FakePlayerHelper.fakePlayer.fallDistance = 0;
                } else {
                    FakePlayerHelper.fakePlayer.motionX = EntityAPI.lookX(FakePlayerHelper.fakePlayer, 0.1);
                    FakePlayerHelper.fakePlayer.motionZ = EntityAPI.lookZ(FakePlayerHelper.fakePlayer, 0.1);
                    //WorldAPI.teleport(MiniGame.minerun.getTeleportPos(), MiniGame.minerun.getYaw(), -70);
                }
            }
    }

    @SubscribeEvent
    public void keyInput(KeyInputEvent e) {
        if (!MiniGame.minerun.isStart()|| FakePlayerHelper.fakePlayer == null)
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
        if (MineRun.elytraMode() == 2) {

            if (lineUD < 1 && DebAPI.isKeyDown(Keyboard.KEY_W) && Keyboard.getEventKeyState()) {
                fakePlayer.setPosition(fakePlayer.posX, fakePlayer.posY + 1, fakePlayer.posZ);
                lineUD++;
                System.out.println(lineUD);
            }
            if (lineUD > -1 && DebAPI.isKeyDown(Keyboard.KEY_S) && Keyboard.getEventKeyState()) {
                fakePlayer.setPosition(fakePlayer.posX, fakePlayer.posY - 1, fakePlayer.posZ);
                lineUD--;
                System.out.println(lineUD);
            }
            if(DebAPI.isKeyDown(Keyboard.KEY_W) || DebAPI.isKeyDown(Keyboard.KEY_S)){
                if(lineUD == 0)
                fakePlayer.setPosition(fakePlayer.posX, fakePlayer.posY + lineUD, fakePlayer.posZ);
            }
            if (lineLR < 1 && DebAPI.isKeyDown(Keyboard.KEY_A) && Keyboard.getEventKeyState()) {
                fakePlayer.setPosition(fakePlayer.posX + lineX, fakePlayer.posY, fakePlayer.posZ + lineZ);
                lineLR++;
            }
            if (lineLR > -1 && DebAPI.isKeyDown(Keyboard.KEY_D) && Keyboard.getEventKeyState()) {
                fakePlayer.setPosition(fakePlayer.posX - lineX, fakePlayer.posY, fakePlayer.posZ - lineZ);
                lineLR--;
            }
        }
        if (MineRun.elytraMode() == 1) {
            if (lineFB < 1 && DebAPI.isKeyDown(Keyboard.KEY_W) && Keyboard.getEventKeyState()) {
                fakePlayer.setPosition(fakePlayer.posX + lineFBX, fakePlayer.posY, fakePlayer.posZ + lineFBZ);
                lineFB++;
            }
            if (lineFB > -1 && DebAPI.isKeyDown(Keyboard.KEY_S) && Keyboard.getEventKeyState()) {
                fakePlayer.setPosition(fakePlayer.posX - lineFBX, fakePlayer.posY, fakePlayer.posZ - lineFBZ);
                lineFB--;
            }
            if (lineLR < 1 && DebAPI.isKeyDown(Keyboard.KEY_A) && Keyboard.getEventKeyState()) {
                fakePlayer.setPosition(fakePlayer.posX + lineX, fakePlayer.posY, fakePlayer.posZ + lineZ);
                lineLR++;
            }
            if (lineLR > -1 && DebAPI.isKeyDown(Keyboard.KEY_D) && Keyboard.getEventKeyState()) {
                fakePlayer.setPosition(fakePlayer.posX - lineX, fakePlayer.posY, fakePlayer.posZ - lineZ);
                lineLR--;
            }
        } else if(MineRun.elytraMode() == 0){
            if (lineLR < 1 && DebAPI.isKeyDown(Keyboard.KEY_A) && Keyboard.getEventKeyState()) {
                fakePlayer.setPosition(fakePlayer.posX +  (lineX * 2), fakePlayer.posY, fakePlayer.posZ +  (lineZ * 2));
                lineLR++;
            }
            if (lineLR > -1 && DebAPI.isKeyDown(Keyboard.KEY_D) && Keyboard.getEventKeyState()) {
                fakePlayer.setPosition(fakePlayer.posX -  (lineX * 2), fakePlayer.posY, fakePlayer.posZ -  (lineZ * 2));
                lineLR--;
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
