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
import net.minecraft.util.math.Vec3d;
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

    private static EntityFakePlayer fakePlayer;

    public static int elytraMode() {
        return elytra;
    }

    public static void setElytra(int elytraMode) {
        elytra = elytraMode;

        if (fakePlayer == null)
            return;
        double yaw = fakePlayer.getHorizontalFacing().getHorizontalAngle();

        if (elytraMode == 1 || elytraMode == 2) {
            if(fakePlayer.getItemStackFromSlot(EntityEquipmentSlot.CHEST) == null) {
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
        }
    }
    private static double xCoord, zCoord;
    protected static double curX, curY, curZ, playerStartY;

    public static void setPosition(double x, double y, double z){
        curX = x;
        curY = y;
        curZ = z;
        System.out.println(curX+ " - "+curY+" - "+curZ);
    }
    public static void setPosition(BlockPos pos){
        setPosition(pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public boolean start(Object... obj) {
        this.setFakePlayerUse();
        GameSettings gs = Minecraft.getMinecraft().gameSettings;
        gs.keyBindLeft.setKeyCode(Keyboard.KEY_SLEEP);
        gs.keyBindRight.setKeyCode(Keyboard.KEY_DIVIDE);
        gs.keyBindForward.setKeyCode(Keyboard.KEY_NOCONVERT);
        gs.keyBindBack.setKeyCode(Keyboard.KEY_SYSRQ);
        gs.keyBindJump.setKeyCode(Keyboard.KEY_POWER);
        KeyBinding.resetKeyBindingArrayAndHash();
        ICommandSender sender = (ICommandSender) obj[0];
        EntityPlayer player = (EntityPlayer) sender;
        WorldAPI.teleport(player.posX, player.posY, player.posZ,player.getHorizontalFacing().getHorizontalAngle(), 70);
        fakePlayer = FakePlayerHelper.spawnFakePlayer(false);
        player.noClip = !player.noClip;
        player.capabilities.isFlying = true;

        player.sendPlayerAbilities();
        WorldAPI.teleport(fakePlayer.posX + EntityAPI.lookX(fakePlayer, -2), fakePlayer.posY + 1, fakePlayer.posZ + EntityAPI.lookZ(fakePlayer, -2), player.getHorizontalFacing().getHorizontalAngle(), 70);
        playerStartY = player.posY;

        Camera.getCamera().reset();
        Camera.getCamera().lockCamera(true, player.getHorizontalFacing().getHorizontalAngle(), 70);
        Camera.getCamera().rotateX = -EntityAPI.lookZ(fakePlayer, 1) * 30;
        Camera.getCamera().rotateZ = -EntityAPI.lookX(fakePlayer, 1) * 30;

        MiniGame.mineRunEvent.lineLR = 0;
        MiniGame.mineRunEvent.lineFB = 0;
        MiniGame.mineRunEvent.lineX = EntityAPI.getFacingX(fakePlayer.rotationYaw - 90);
        MiniGame.mineRunEvent.lineZ = EntityAPI.getFacingZ(fakePlayer.rotationYaw - 90);
        MiniGame.mineRunEvent.lineFBX = EntityAPI.lookX(fakePlayer, 1);
        MiniGame.mineRunEvent.lineFBZ = EntityAPI.lookZ(fakePlayer, 1);
        xCoord =  EntityAPI.lookX(player, 0.3);
        zCoord =  EntityAPI.lookZ(player, 0.3);
        System.out.println(xCoord+" - "+zCoord);
        return super.start();
    }

    public static double xCoord(){
        return xCoord;
    }
    public static double zCoord(){
        return zCoord;
    }

    public static void setFakePositionUpdate(){
        EntityPlayer player = WorldAPI.getPlayer();
        EntityFakePlayer fakePlayer = FakePlayerHelper.fakePlayer;
        if(elytraMode() == 1){
            fakePlayer.setPosition(player.posX + curX + EntityAPI.lookX(player, 3), player.posY + 8, player.posZ + curZ + EntityAPI.lookZ(player, 3));
        }else
        fakePlayer.setPosition(player.posX + curX + EntityAPI.lookX(player, 3), fakePlayer.posY + curY, player.posZ + curZ + EntityAPI.lookZ(player, 3));
        if (curY != 0) {
            curY = 0;
        }
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
        gs.keyBindJump.setKeyCode(Keyboard.KEY_SPACE);
        gs.keyBindUseItem.resetKeyBindingArrayAndHash();
        setElytra(0);
        Camera.getCamera().reset();
        curX = 0;
        curY = 0;
        curZ = 0;
        xCoord = 0;
        zCoord = 0;
        playerStartY = 0;
        MiniGame.mineRunEvent.lineFB = 0;
        MiniGame.mineRunEvent.lineX = 0;
        MiniGame.mineRunEvent.lineZ = 0;
        MiniGame.mineRunEvent.lineLR = 0;
        MiniGame.mineRunEvent.lineUD = 0;
        MiniGame.mineRunEvent.lineFBX = 0;
        MiniGame.mineRunEvent.lineFBZ = 0;
        return super.end();
    }
}
