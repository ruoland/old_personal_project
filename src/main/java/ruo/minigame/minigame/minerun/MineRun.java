package ruo.minigame.minigame.minerun;


import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.input.Keyboard;
import ruo.cmplus.camera.Camera;
import ruo.minigame.MiniGame;
import ruo.minigame.api.EntityAPI;
import ruo.minigame.api.WorldAPI;
import ruo.minigame.fakeplayer.EntityFakePlayer;
import ruo.minigame.fakeplayer.FakePlayerHelper;
import ruo.minigame.minigame.AbstractMiniGame;

import java.util.List;

public class MineRun extends AbstractMiniGame {
    public static List<BlockPos> removeLavaPos = Lists.newArrayList();
    private static int elytra = 0;//1 = 위로 2 = 앞으로
    private static double xCoord, zCoord;
    protected static double curX, curY, curZ, spawnX, spawnY, spawnZ;
    private static EntityFakePlayer fakePlayer;
    private static EntityPlayer player;

    public static int elytraMode() {
        return elytra;
    }

    public static void setElytra(int elytraMode) {
        elytra = elytraMode;

        if (fakePlayer == null)
            return;

        if (elytraMode == 1 || elytraMode == 2) {
            if (fakePlayer == null) {
                fakePlayer = FakePlayerHelper.spawnFakePlayer(false);
                player.noClip = !player.noClip;
                player.capabilities.isFlying = true;
                player.sendPlayerAbilities();
                WorldAPI.teleport(fakePlayer.posX + EntityAPI.lookX(fakePlayer, -2), fakePlayer.posY + 1, fakePlayer.posZ + EntityAPI.lookZ(fakePlayer, -2), player.getHorizontalFacing().getHorizontalAngle(), 70);
            }
            if (fakePlayer.getItemStackFromSlot(EntityEquipmentSlot.CHEST) == null) {
                ItemStack itemstack = new ItemStack(Items.ELYTRA);
                fakePlayer.setItemStackToSlot(EntityEquipmentSlot.CHEST, itemstack);
            }
        }
        if (elytraMode == 2) {
            fakePlayer.setElytra(true);
            curY++;
        } else if (elytraMode == 1) {
            fakePlayer.setElytra(false);
            Camera.getCamera().rotateX = -Camera.getCamera().rotateX;
            Camera.getCamera().rotateZ = -Camera.getCamera().rotateZ;
            System.out.println("엘리트라 모드 해제");
        } else {
            System.out.println("엘리트라 취소됨");
            fakePlayer.setElytra(false);
            player.noClip = !player.noClip;
            player.capabilities.isFlying = false;
            player.sendPlayerAbilities();
            MiniGame.mineRunEvent.lineFB = 0;
            MiniGame.mineRunEvent.lineX = 0;
            MiniGame.mineRunEvent.lineZ = 0;
            MiniGame.mineRunEvent.lineLR = 0;
            MiniGame.mineRunEvent.lineUD = 0;
            MiniGame.mineRunEvent.lineFBX = 0;
            MiniGame.mineRunEvent.lineFBZ = 0;
            setFakePositionUpdate();
            WorldAPI.teleport(fakePlayer.posX, fakePlayer.posY, fakePlayer.posZ, player.getHorizontalFacing().getHorizontalAngle(), 70);

            fakePlayer = null;

        }
    }


    public static void setPosition(double x, double y, double z) {
        curX = x;
        curY = y;
        curZ = z;
        WorldAPI.teleport(player.posX + curX + player.motionX, player.posY, player.posZ + curZ + player.motionZ);
    }

    public static void setPosition(BlockPos pos) {
        setPosition(pos.getX(), pos.getY(), pos.getZ());
    }

    public static void setPosition(Vec3d pos) {
        setPosition(pos.xCoord, pos.yCoord, pos.zCoord);
    }

    @Override
    public boolean start(Object... obj) {
        GameSettings gs = Minecraft.getMinecraft().gameSettings;
        gs.keyBindLeft.setKeyCode(Keyboard.KEY_SLEEP);
        gs.keyBindRight.setKeyCode(Keyboard.KEY_DIVIDE);
        gs.keyBindForward.setKeyCode(Keyboard.KEY_NOCONVERT);
        gs.keyBindBack.setKeyCode(Keyboard.KEY_SYSRQ);
        KeyBinding.resetKeyBindingArrayAndHash();
        ICommandSender sender = (ICommandSender) obj[0];
        player = (EntityPlayer) sender;
        WorldAPI.teleport(((int)player.posX) + 0.5, player.posY, ((int)player.posZ) + 0.5, player.getHorizontalFacing().getHorizontalAngle(), 70);
        spawnX = player.posX;
        spawnY = player.posY;
        spawnZ = player.posZ;
        Camera.getCamera().reset();
        Camera.getCamera().lockCamera(true, player.getHorizontalFacing().getHorizontalAngle(), 0);
        Camera.getCamera().rotateX = EntityAPI.lookZ(player, 1) * 30;
        Camera.getCamera().rotateY = (player.getHorizontalFacing().getIndex() - 1) * 90;
        Camera.getCamera().rotateZ = EntityAPI.lookX(player, 1) * 30;
        if (player.getHorizontalFacing() == EnumFacing.NORTH) {
            Camera.getCamera().rotateX = 30;
            Camera.getCamera().rotateY = 0;
            Camera.getCamera().moveCamera(0, 0, -2);
        }
        if (player.getHorizontalFacing() == EnumFacing.EAST) {
            Camera.getCamera().rotateX = 30;
            Camera.getCamera().rotateY = 90;
            Camera.getCamera().rotateZ = 0;
        }
        Camera.getCamera().moveCamera(EntityAPI.lookX(player, 3.5), -1.5, EntityAPI.lookZ(player, 3.5));
        Camera.getCamera().playerCamera(true);
        MiniGame.mineRunEvent.lineLR = 0;
        MiniGame.mineRunEvent.lineFB = 0;
        MiniGame.mineRunEvent.lineX = EntityAPI.getFacingX(player.rotationYaw - 90);
        MiniGame.mineRunEvent.lineZ = EntityAPI.getFacingZ(player.rotationYaw - 90);
        MiniGame.mineRunEvent.lineFBX = EntityAPI.lookX(player, 1);
        MiniGame.mineRunEvent.lineFBZ = EntityAPI.lookZ(player, 1);
        xCoord = EntityAPI.lookX(player, 0.3);
        zCoord = EntityAPI.lookZ(player, 0.3);
        return super.start();
    }

    public static double xCoord() {
        return xCoord;
    }

    public static double zCoord() {
        return zCoord;
    }

    public static void setFakePositionUpdate() {
        EntityPlayer player = WorldAPI.getPlayer();
        if (elytraMode() == 1) {
            fakePlayer.setPosition(player.posX + curX + getLookX(), player.posY + 8, player.posZ + curZ + getLookZ());
        } else
            fakePlayer.setPosition(player.posX + curX + getLookX(), fakePlayer.posY + curY, player.posZ + curZ + getLookZ());
        if (curY != 0) {
            curY = 0;
        }
    }

    public static double getLookX() {
        return EntityAPI.lookX(player, 3);
    }

    public static double getLookZ() {
        return EntityAPI.lookZ(player, 3);
    }

    public static float getYaw() {
        return WorldAPI.getPlayer().rotationYaw;
    }

    @Override
    public boolean end(Object... obj) {
        GameSettings gs = Minecraft.getMinecraft().gameSettings;
        gs.keyBindLeft.setKeyCode(Keyboard.KEY_A);
        gs.keyBindRight.setKeyCode(Keyboard.KEY_D);
        gs.keyBindForward.setKeyCode(Keyboard.KEY_W);
        gs.keyBindBack.setKeyCode(Keyboard.KEY_S);
        gs.keyBindUseItem.resetKeyBindingArrayAndHash();
        setElytra(0);
        Camera.getCamera().reset();
        curX = 0;
        curY = 0;
        curZ = 0;
        xCoord = 0;
        zCoord = 0;
        MiniGame.mineRunEvent.lineFB = 0;
        MiniGame.mineRunEvent.lineX = 0;
        MiniGame.mineRunEvent.lineZ = 0;
        MiniGame.mineRunEvent.lineLR = 0;
        MiniGame.mineRunEvent.lineUD = 0;
        MiniGame.mineRunEvent.lineFBX = 0;
        MiniGame.mineRunEvent.lineFBZ = 0;
        WorldAPI.command("/minerun lava");
        return super.end();
    }
}
