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
    public static int deadCount = 0;

    private EntityFakePlayer fakePlayer;

    public MineRun() {
    }

    public static int elytraMode() {
        return elytra;
    }

    public void setElytra(int elytraMode) {
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
        } else {
            System.out.println("엘리트라 취소됨");
            fakePlayer.setElytra(false);
        }
    }
    public Vec3d moveVec3d;
    private double xCoord, zCoord;
    public double curX, curY, curZ, playerStartY, elytraY;

    public void setPosition(double x, double y, double z){
        curX = x;
        curY = y;
        curZ = z;
        System.out.println(MiniGame.minerun.curX+ " - "+MiniGame.minerun.curY+" - "+MiniGame.minerun.curZ);

    }
    @Override
    public boolean start(Object... obj) {
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
        Camera.getCamera().lockCamera(true, (float) player.getHorizontalFacing().getHorizontalAngle(), 70);
        Camera.getCamera().rotateX = -EntityAPI.lookZ(fakePlayer, 1) * 30;
        Camera.getCamera().rotateZ = -EntityAPI.lookX(fakePlayer, 1) * 30;

        MiniGame.mineRunEvent.lineLR = 0;
        MiniGame.mineRunEvent.lineFB = 0;
        MiniGame.mineRunEvent.lineX = EntityAPI.getFacingX(fakePlayer.rotationYaw - 90);
        MiniGame.mineRunEvent.lineZ = EntityAPI.getFacingZ(fakePlayer.rotationYaw - 90);
        MiniGame.mineRunEvent.lineFBX = EntityAPI.lookX(fakePlayer, 1);
        MiniGame.mineRunEvent.lineFBZ = EntityAPI.lookZ(fakePlayer, 1);
        MiniGame.mineRunEvent.spawnX = fakePlayer.posX;
        MiniGame.mineRunEvent.spawnY = fakePlayer.posY;
        MiniGame.mineRunEvent.spawnZ = fakePlayer.posZ;
        xCoord =  EntityAPI.lookX(player, 0.3);
        zCoord =  EntityAPI.lookZ(player, 0.3);
        System.out.println(xCoord+" - "+zCoord);
        return super.start();
    }

    public double xCoord(){
        return xCoord;
    }
    public double zCoord(){
        return zCoord;
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
        gs.keyBindJump.setKeyCode(Keyboard.KEY_SPACE);
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
