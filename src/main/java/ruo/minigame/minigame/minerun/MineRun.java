package ruo.minigame.minigame.minerun;


import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.input.Keyboard;
import ruo.cmplus.camera.Camera;
import ruo.minigame.api.EntityAPI;
import ruo.minigame.api.WorldAPI;
import ruo.minigame.minigame.AbstractMiniGame;

public class MineRun extends AbstractMiniGame {
    private static int elytra = 0;//1 = 위로 2 = 앞으로
    public static int deadCount = 0;

    public MineRun() {
        this.event = new MineRunEvent();
    }

    public static int elytraMode() {
        return elytra;
    }

    public static void setElytra(int elytraMode) {
        elytra = elytraMode;
        EntityPlayer player = WorldAPI.getPlayerMP();
        if(player == null)
            return;
        double yaw = player.getHorizontalFacing().getHorizontalAngle();

        if (elytraMode == 1 || elytraMode == 2) {
            ItemStack itemstack = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
            player.inventory.armorInventory[2] = new ItemStack(Items.ELYTRA);
        }
        if (elytraMode == 2) {
            ((EntityPlayerMP) player).setElytraFlying();
            WorldAPI.teleport(player.posX, player.posY + 1, player.posZ);
            player.motionY -= 0.1;
            WorldAPI.getPlayerSP().connection.sendPacket(new CPacketEntityAction(WorldAPI.getPlayerSP(), CPacketEntityAction.Action.START_FALL_FLYING));
            NBTTagCompound compound = new NBTTagCompound();
            WorldAPI.getPlayerSP().writeEntityToNBT(compound);
            compound.setBoolean("FallFlying" , true);
            WorldAPI.getPlayerSP().readEntityFromNBT(compound);
            System.out.println("12345 " + player.onGround + player.motionY+WorldAPI.getPlayerSP().isElytraFlying() + WorldAPI.getPlayerMP().isElytraFlying());
            noElytra(player, yaw);
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
            ((EntityPlayerMP) player).clearElytraFlying();
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
        noElytra(player, yaw);

        if (obj.length > 1 && obj[1] instanceof String[]) {
            String[] str = (String[]) obj[1];
            if (str.length > 0) {
                if (str[0].equals("elytra"))
                    setElytra(1);
                if (str[0].equals("create")) {
                    new MapSetting().create();
                }
            }
        }
        ((MineRunEvent) event).lineLR = 0;
        ((MineRunEvent) event).lineFB = 0;
        ((MineRunEvent) event).lineX = EntityAPI.getFacingX(player.rotationYaw - 90);
        ((MineRunEvent) event).lineZ = EntityAPI.getFacingZ(player.rotationYaw - 90);
        ((MineRunEvent) event).lineFBX = EntityAPI.lookX(player, 1);
        ((MineRunEvent) event).lineFBZ = EntityAPI.lookZ(player, 1);
        ((MineRunEvent) event).lineY = player.posY;

        return super.start();
    }

    public static void noElytra(EntityPlayer player, double yaw) {
        Camera.getCamera().moveCamera(EntityAPI.lookX(player, 3), -0.9, EntityAPI.lookZ(player, 3));
        float roX = EntityAPI.lookX(player, 4) == 0 ? -20 : 0;
        float roZ = EntityAPI.lookZ(player, 4) == 0 ? -20 : 0;
        if (roX < 0) {
            roX = 20;
        }
        if (roZ < 0) {
            roZ = 20;
        }
        if (player.getHorizontalFacing() == EnumFacing.WEST)
            roZ = -20;
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
        Camera.getCamera().reset();
        setElytra(0);
        return super.end();
    }

    public void resetLine() {
        MineRunEvent runEvent = (MineRunEvent) event;
        runEvent.lineLR = 0;
        runEvent.lineFB = 0;
        runEvent.lineUD = 0;
    }
}
