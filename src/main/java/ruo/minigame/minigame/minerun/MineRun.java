package ruo.minigame.minigame.minerun;


import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderZombie;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.FakePlayer;
import org.lwjgl.input.Keyboard;
import ruo.cmplus.camera.Camera;
import ruo.minigame.MiniGame;
import ruo.minigame.api.EntityAPI;
import ruo.minigame.api.WorldAPI;
import ruo.minigame.fakeplayer.EntityFakePlayer;
import ruo.minigame.fakeplayer.FakePlayerHelper;
import ruo.minigame.map.RenderDefaultNPC;
import ruo.minigame.minigame.AbstractMiniGame;

public class MineRun extends AbstractMiniGame {
    private static int elytra = 0;//1 = 위로 2 = 앞으로
    public static int deadCount = 0;

    private EntityFakePlayer player;

    public MineRun() {
    }

    public static int elytraMode() {
        return elytra;
    }

    public void setElytra(int elytraMode) {
        elytra = elytraMode;

        if (player == null)
            return;
        double yaw = player.getHorizontalFacing().getHorizontalAngle();

        if (elytraMode == 1 || elytraMode == 2) {
            ItemStack itemstack = new ItemStack(Items.ELYTRA);
            player.setItemStackToSlot(EntityEquipmentSlot.CHEST, itemstack);
        }
        if (elytraMode == 2) {

        } else if (elytraMode == 1) {
            Camera.getCamera().moveCamera(EntityAPI.lookX(player, 2), 3, EntityAPI.lookZ(player, 2));
            float roX = EntityAPI.lookX(player, 1) == 0 ? -50 : 0;
            float roZ = EntityAPI.lookZ(player, 1) == 0 ? -50 : 0;
            if (player.getHorizontalFacing() == EnumFacing.WEST) {
                roZ = Math.abs(roZ);
            }
            if (yaw < 0)
                Camera.getCamera().rotateCamera(roX, yaw - 180, roZ);
            else
                Camera.getCamera().rotateCamera(roX, yaw + 180, roZ);
        } else {
            System.out.println("엘리트라 취소됨");
            player.setElytra(false);
        }
    }

    @Override
    public boolean start(Object... obj) {
        GameSettings gs = Minecraft.getMinecraft().gameSettings;
        gs.keyBindLeft.setKeyCode(Keyboard.KEY_SLEEP);
        gs.keyBindRight.setKeyCode(Keyboard.KEY_SLEEP);
        gs.keyBindForward.setKeyCode(Keyboard.KEY_SLEEP);
        gs.keyBindBack.setKeyCode(Keyboard.KEY_SLEEP);
        ICommandSender sender = (ICommandSender) obj[0];
        player = FakePlayerHelper.spawnFakePlayer(false);
        WorldAPI.teleport(player.posX + EntityAPI.lookX(player, -2), player.posY + 1, player.posZ + EntityAPI.lookZ(player, -2), player.rotationYaw, 70);
        double yaw = player.getHorizontalFacing().getHorizontalAngle();
        Camera.getCamera().reset();
        Camera.getCamera().lockCamera(true, (float) yaw, 0);
        Camera.getCamera().rotateY = yaw - 180;
        MiniGame.mineRunEvent.lineLR = 0;
        MiniGame.mineRunEvent.lineFB = 0;
        MiniGame.mineRunEvent.lineX = EntityAPI.getFacingX(player.rotationYaw - 90);
        MiniGame.mineRunEvent.lineZ = EntityAPI.getFacingZ(player.rotationYaw - 90);
        MiniGame.mineRunEvent.lineFBX = EntityAPI.lookX(player, 1);
        MiniGame.mineRunEvent.lineFBZ = EntityAPI.lookZ(player, 1);
        MiniGame.mineRunEvent.spawnX = player.posX;
        MiniGame.mineRunEvent.spawnY = player.posY;
        MiniGame.mineRunEvent.spawnZ = player.posZ;
        return super.start();
    }


    public BlockPos getTeleportPos() {
        System.out.println(WorldAPI.getPlayer().getPosition() + " - " + EntityAPI.lookX(FakePlayerHelper.fakePlayer, -2) + " - " + EntityAPI.lookZ(FakePlayerHelper.fakePlayer, -2));
        return new BlockPos(EntityAPI.forwardX(FakePlayerHelper.fakePlayer, -2, true), FakePlayerHelper.fakePlayer.posY + 1, EntityAPI.forwardZ(FakePlayerHelper.fakePlayer, -2, true));
    }

    public float getYaw() {
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

        return super.end();
    }

    public void resetLine() {
        MineRunEvent runEvent = MiniGame.mineRunEvent;
        runEvent.lineLR = 0;
        runEvent.lineFB = 0;
        runEvent.lineUD = 0;
    }
}
