package ruo.minigame.minigame.minerun;


import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.input.Keyboard;
import ruo.cmplus.camera.Camera;
import ruo.minigame.api.EntityAPI;
import ruo.minigame.api.WorldAPI;
import ruo.minigame.minigame.AbstractMiniGame;

public class MineRun extends AbstractMiniGame {
    private static boolean isElytraMode = false;

    public MineRun() {
        this.event = new MineRunEvent();
    }

    public static boolean isElytraMode() {
        return isElytraMode;
    }

    public static void setElytraMode(boolean elytraMode) {
        isElytraMode = elytraMode;
        EntityPlayer player = WorldAPI.getPlayerMP();
        double yaw = player.getHorizontalFacing().getHorizontalAngle();

        if (isElytraMode()) {
            player.inventory.setInventorySlotContents(1, new ItemStack(Items.ELYTRA));
            Camera.getCamera().moveCamera(EntityAPI.lookX(player, 2), 3, EntityAPI.lookZ(player, 2));
            float roX = EntityAPI.lookX(player, 1) == 0 ? -50 : 0;
            float roZ = EntityAPI.lookZ(player, 1) == 0 ? -50 : 0;
            if (yaw < 0)
                Camera.getCamera().rotateCamera(roX, yaw - 180, roZ);
            else
                Camera.getCamera().rotateCamera(roX, yaw + 180, roZ);
        } else {
            noElytra(player, yaw);
        }
    }

    @Override
    public boolean start(Object... obj) {
        GameSettings gs = Minecraft.getMinecraft().gameSettings;
        gs.keyBindLeft.setKeyCode(Keyboard.KEY_SLEEP);
        gs.keyBindRight.setKeyCode(Keyboard.KEY_SLEEP);
        gs.keyBindForward.setKeyCode(Keyboard.KEY_SLEEP);
        gs.keyBindBack.setKeyCode(Keyboard.KEY_SLEEP);
        KeyBinding.resetKeyBindingArrayAndHash();
        ICommandSender sender = (ICommandSender) obj[0];
        EntityPlayer player = (EntityPlayer) sender;
        BlockPos pos = player.getPosition();
        double x = player.posX > player.posX + 0.5 ? pos.getX() + 0.5 : pos.getX() - 0.5;
        double z = player.posZ > player.posZ + 0.5 ? pos.getZ() + 0.5 : pos.getZ() - 0.5;

        WorldAPI.teleport(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
        double yaw = player.getHorizontalFacing().getHorizontalAngle();
        Camera.getCamera().reset();
        Camera.getCamera().lockCamera(true);
        Camera.getCamera().playerCamera(true);
        Camera.getCamera().lockCamera(true, (float) yaw, 0);
        if (obj.length == 1 || !obj[1].equals("elytra"))
            noElytra(player, yaw);
        else {
            setElytraMode(true);
        }
        ((MineRunEvent) event).lineX = EntityAPI.getFacingX(player.rotationYaw - 90);
        ((MineRunEvent) event).lineZ = EntityAPI.getFacingZ(player.rotationYaw - 90);
        ((MineRunEvent) event).lineFBX = EntityAPI.lookX(player, 1);
        ((MineRunEvent) event).lineFBZ = EntityAPI.lookZ(player, 1);
        return super.start();
    }

    public static void noElytra(EntityPlayer player, double yaw) {
        Camera.getCamera().moveCamera(EntityAPI.lookX(player, 3), -0.9, EntityAPI.lookZ(player, 3));
        float roX = EntityAPI.lookX(player, 4) == 0 ? 20 : 0;
        float roZ = EntityAPI.lookZ(player, 4) == 0 ? 20 : 0;
        if (yaw < 0)
            Camera.getCamera().rotateCamera(roX, yaw - 180, roZ);
        else
            Camera.getCamera().rotateCamera(roX, yaw + 180, roZ);
    }

    @Override
    public boolean end(Object... obj) {
        GameSettings gs = Minecraft.getMinecraft().gameSettings;
        gs.keyBindLeft.setKeyCode(Keyboard.KEY_A);
        gs.keyBindRight.setKeyCode(Keyboard.KEY_D);
        gs.keyBindForward.setKeyCode(Keyboard.KEY_W);
        gs.keyBindBack.setKeyCode(Keyboard.KEY_S);
        gs.keyBindUseItem.resetKeyBindingArrayAndHash();
        setElytraMode(false);
        Camera.getCamera().reset();
        return super.end();
    }
}
