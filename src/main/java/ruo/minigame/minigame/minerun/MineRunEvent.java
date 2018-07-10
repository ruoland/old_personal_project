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

public class MineRunEvent {
    //elytra 1
    public int lineLR = 0, lineFB = 0;

    //elytra 2
    public int lineUD = 0;

    public double lineX, lineY, lineZ, lineFBX, lineFBZ;

    @SubscribeEvent
    public void playerTick(PlayerTickEvent e) {
        if (!MiniGame.minerun.isStart())
            return;
        if(e.player.isElytraFlying() || MiniGame.minerun.elytraMode() == 2) {
            e.player.motionY = 0;
            e.player.onGround = false;
            if(!e.player.isElytraFlying()){
                e.player.motionY -= 0.001;//안되면 이것

                if(e.player instanceof EntityPlayerMP)
                ((EntityPlayerMP) e.player).setElytraFlying();
                if(WorldAPI.equalsHeldItem(Items.APPLE)) {
                    e.player.onGround = false;
                    e.player.capabilities.isFlying = true;
                    e.player.sendPlayerAbilities();
                }
                WorldAPI.getPlayerSP().connection.sendPacket(new CPacketEntityAction(WorldAPI.getPlayerSP(), CPacketEntityAction.Action.START_FALL_FLYING));
                NBTTagCompound compound = new NBTTagCompound();
                WorldAPI.getPlayerSP().writeEntityToNBT(compound);
                compound.setBoolean("FallFlying" , true);
                WorldAPI.getPlayerSP().readEntityFromNBT(compound);
                System.out.println("MP인가?" +(e.player instanceof EntityPlayerMP)+ "땅? "+e.player.onGround + "모션? "+e.player.motionY+"SP 플라잉"+WorldAPI.getPlayerSP().isElytraFlying() + WorldAPI.getPlayerMP().isElytraFlying());
            }
        }
        if (e.phase == Phase.END) {

            if (e.player.fallDistance > 5) {
                e.player.fallDistance = 0;
                ActionEffect.teleportSpawnPoint(e.player);
            }
            if (MineRun.elytraMode() == 1) {
                e.player.motionY = 0.2;
                e.player.fallDistance = 0;
            } else {
                e.player.motionX = EntityAPI.lookX(e.player, 0.2);
                e.player.motionZ = EntityAPI.lookZ(e.player, 0.2);
            }
        }
    }

    @SubscribeEvent
    public void keyInput(KeyInputEvent e) {
        if (!MiniGame.minerun.isStart())
            return;
        EntityPlayerMP player = WorldAPI.getPlayerMP();
        if (DebAPI.isKeyDown(Keyboard.KEY_V)) {
            System.out.println("aaa " + MineRun.elytraMode());
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
                WorldAPI.teleport(player.posX, player.posY + 1, player.posZ);
                lineUD++;
                System.out.println(lineUD);
            }
            if (lineUD > -1 && DebAPI.isKeyDown(Keyboard.KEY_S) && Keyboard.getEventKeyState()) {
                WorldAPI.teleport(player.posX, player.posY - 1, player.posZ);
                lineUD--;
                System.out.println(lineUD);
            }
            if(DebAPI.isKeyDown(Keyboard.KEY_W) || DebAPI.isKeyDown(Keyboard.KEY_S)){
                if(lineUD == 0)
                WorldAPI.teleport(player.posX, player.posY + lineUD, player.posZ);

            }
            if (lineLR < 1 && DebAPI.isKeyDown(Keyboard.KEY_A) && Keyboard.getEventKeyState()) {
                WorldAPI.teleport(player.posX + lineX, player.posY, player.posZ + lineZ);
                lineLR++;
            }
            if (lineLR > -1 && DebAPI.isKeyDown(Keyboard.KEY_D) && Keyboard.getEventKeyState()) {
                WorldAPI.teleport(player.posX - lineX, player.posY, player.posZ - lineZ);
                lineLR--;
            }
        }
        if (MineRun.elytraMode() == 1) {
            if (lineFB < 1 && DebAPI.isKeyDown(Keyboard.KEY_W) && Keyboard.getEventKeyState()) {
                WorldAPI.teleport(player.posX + lineFBX, player.posY, player.posZ + lineFBZ);
                lineFB++;
            }
            if (lineFB > -1 && DebAPI.isKeyDown(Keyboard.KEY_S) && Keyboard.getEventKeyState()) {
                WorldAPI.teleport(player.posX - lineFBX, player.posY, player.posZ - lineFBZ);
                lineFB--;
            }
            if (lineLR < 1 && DebAPI.isKeyDown(Keyboard.KEY_A) && Keyboard.getEventKeyState()) {
                WorldAPI.teleport(player.posX + lineX, player.posY, player.posZ + lineZ);
                lineLR++;
            }
            if (lineLR > -1 && DebAPI.isKeyDown(Keyboard.KEY_D) && Keyboard.getEventKeyState()) {
                WorldAPI.teleport(player.posX - lineX, player.posY, player.posZ - lineZ);
                lineLR--;
            }
        } else if(MineRun.elytraMode() == 0){
            if (lineLR < 1 && DebAPI.isKeyDown(Keyboard.KEY_A) && Keyboard.getEventKeyState()) {
                WorldAPI.teleport(player.posX +  (lineX * 2), player.posY, player.posZ +  (lineZ * 2));
                lineLR++;
            }
            if (lineLR > -1 && DebAPI.isKeyDown(Keyboard.KEY_D) && Keyboard.getEventKeyState()) {
                WorldAPI.teleport(player.posX -  (lineX * 2), player.posY, player.posZ -  (lineZ * 2));
                lineLR--;
            }
            System.out.print(lineLR);
        }
    }

    @SubscribeEvent
    public void logout(WorldEvent.Unload e) {
        MiniGame.minerun.end();
    }
}
