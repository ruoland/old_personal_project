package minigameLib.minigame.minerun;


import cmplus.camera.Camera;
import cmplus.deb.DebAPI;
import com.google.common.collect.Lists;
import minigameLib.MiniGame;
import net.minecraft.command.CommandGameMode;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import oneline.api.Direction;
import oneline.api.EntityAPI;
import oneline.api.PosHelper;
import oneline.api.WorldAPI;
import oneline.fakeplayer.EntityFakePlayer;
import oneline.fakeplayer.FakePlayerHelper;
import minigameLib.minigame.AbstractMiniGame;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.item.EntityMinecartEmpty;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class MineRun extends AbstractMiniGame {
    public static List<BlockPos> removeLavaPos = Lists.newArrayList();//용암
    public static List<IBlockState> removeLavaState = Lists.newArrayList();
    public static PosHelper playerPosHelper;
    public static PosHelper runnerPosHelper;
    private static EnumElytra elytra = EnumElytra.RUNNING;
    private static double xCoord, zCoord;
    protected static double curX, curY, curZ, spawnX, spawnY, spawnZ;
    private static EntityPlayer player;
    public static EntityMineRunner runner;
    private static World worldObj;
    public static Vec3d spawnPoint = null;
    public static EnumElytra elytraMode() {
        return elytra;
    }

    public static void setElytra(EnumElytra elytraMode) {
        elytra = elytraMode;
        System.out.println(elytraMode);
        if (elytraMode == EnumElytra.ELYTRA) {
            if (runner == null) {
                runner = new EntityMineRunner(player.worldObj);
                runner.setPosition(playerPosHelper.getPosition());
                player.worldObj.spawnEntityInWorld(runner);
                player.noClip = !player.noClip;
                player.capabilities.isFlying = true;
                player.sendPlayerAbilities();
                Camera.getCamera().reset();
                //플레이어를 러너뒤로 보냄
                WorldAPI.teleport(runner.posX + EntityAPI.lookX(runner, -2), runner.posY + 1, runner.posZ + EntityAPI.lookZ(runner, -2), player.getHorizontalFacing().getHorizontalAngle(), 70);
            }
            runner.setElytra(true);
            curY++;
            if (runner.getItemStackFromSlot(EntityEquipmentSlot.CHEST) == null) {
                ItemStack itemstack = new ItemStack(Items.ELYTRA);
                runner.setItemStackToSlot(EntityEquipmentSlot.CHEST, itemstack);
            }
        } else {
            System.out.println("엘리트라 취소됨");
            player.noClip = !player.noClip;
            player.capabilities.isFlying = false;
            player.sendPlayerAbilities();
            MiniGame.mineRunEvent.lineX = 0;
            MiniGame.mineRunEvent.lineZ = 0;
            MiniGame.mineRunEvent.lineLR = 0;
            MiniGame.mineRunEvent.lineUD = 0;
            if (runner != null) {
                setFakePositionUpdate();
                runner.setElytra(false);
                WorldAPI.teleport(runner.posX, runner.posY, runner.posZ, player.getHorizontalFacing().getHorizontalAngle(), 70);
                runner.setDead();
                runner = null;

            }
        }
    }

    /**
     * 플레이어의 좌표를 갱신함
     */
    public static void setPosition(double x, double y, double z) {
        curX = x;
        curY = y;
        curZ = z;
        World worldObj = runner.worldObj;
        Vec3d teleportVec = new Vec3d(runner.posX + curX, runner.posY, runner.posZ + curZ);

        //플레이어가 이동할 지역에 있는 블럭을 가져옴
        Block block = (worldObj.getBlockState(new BlockPos(teleportVec)).getBlock());
        Block block2 = (worldObj.getBlockState(new BlockPos(teleportVec.addVector(0, 1, 0))).getBlock());

        //플레이어가 이동할 지역에 사다리가 있거나 플레이어가 사다리를 타고 있는 경우
        //플레이어가 이동할 지역에 조금 뒤로 이동하게 함
        if (runner.isOnLadder() || block == Blocks.LADDER || block2 == Blocks.LADDER) {
            teleportVec.addVector(-(runner.motionX * 1.5), 0, -(runner.motionZ * 1.5));
            System.out.println("사다리에 올라탐");
        }

        //플레이어가 마인카트에 타고 있으면 마인카트의 좌표를 설정함
        if (runner.getRidingEntity() instanceof EntityMinecartEmpty) {
            EntityMinecartEmpty minecartEmpty = (EntityMinecartEmpty) runner.getRidingEntity();
            Vec3d vec3d = null;
            if (DebAPI.isKeyDown(Keyboard.KEY_A)) {
                vec3d = (runner.getXZ(Direction.LEFT, 2, false));
            }
            if (DebAPI.isKeyDown(Keyboard.KEY_D)) {
                vec3d = (runner.getXZ(Direction.RIGHT, 2, false));
            }
            minecartEmpty.setPosition(minecartEmpty.posX + vec3d.xCoord, minecartEmpty.posY, minecartEmpty.posZ + vec3d.zCoord);
            minecartEmpty.setPositionAndRotationDirect(minecartEmpty.posX + vec3d.xCoord, minecartEmpty.posY, minecartEmpty.posZ + vec3d.zCoord, minecartEmpty.rotationYaw, minecartEmpty.rotationPitch, 0, false);

        } else if (elytraMode() == EnumElytra.RUNNING) {
            runner.setPosition(teleportVec);
            runner.setPositionAndUpdate(teleportVec.xCoord, teleportVec.yCoord, teleportVec.zCoord);
            System.out.println(teleportVec.xCoord + " - "+teleportVec.zCoord+"로 이동함");
        } else if (elytraMode() == EnumElytra.ELYTRA) {
            Vec3d pos = playerPosHelper.getPosition().addVector(EntityAPI.lookX(runner, 6), -3, EntityAPI.lookZ(runner, 6));
            runner.setPosition(pos);
            runner.setPositionAndUpdate(pos.xCoord, pos.yCoord, pos.zCoord);
        }
        MineRun.setFakePositionUpdate();
    }

    public static void setPosition(BlockPos pos) {
        setPosition(pos.getX(), pos.getY(), pos.getZ());
    }

    public static void setPosition(Vec3d pos) {
        setPosition(pos.xCoord, pos.yCoord, pos.zCoord);
    }

    @Override
    public boolean start(Object... obj) {
        keySetting(true);

        ICommandSender sender = (ICommandSender) obj[0];
        player = (EntityPlayer) sender;
        worldObj = player.getEntityWorld();

        WorldAPI.teleport(player.posX, player.posY+2, player.posZ, player.getHorizontalFacing().getHorizontalAngle(), 30);//플레이어 pitch를 70으로
        spawnX = player.posX;
        spawnY = player.posY;
        spawnZ = player.posZ;
        player.setGameType(GameType.SPECTATOR);
        //cameraSetting();

        MiniGame.mineRunEvent.lineLR = 0;
        MiniGame.mineRunEvent.lineX = EntityAPI.getFacingX(player.rotationYaw - 90);
        MiniGame.mineRunEvent.lineZ = EntityAPI.getFacingZ(player.rotationYaw - 90);
        xCoord = EntityAPI.lookX(player, 0.3);
        zCoord = EntityAPI.lookZ(player, 0.3);
        playerPosHelper = new PosHelper(player);
        runner = new EntityMineRunner(sender.getEntityWorld());
        runner.setPosition(playerPosHelper.getPosition());
        worldObj.spawnEntityInWorld(runner);
        runnerPosHelper = new PosHelper(runner);
        runner.rotationYaw = player.rotationYaw;
        runner.rotationPitch = player.rotationPitch;
        runner.renderYawOffset = player.renderYawOffset;

        return super.start();
    }

    private void cameraSetting(){
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
    }
    private void keySetting(boolean isStart){
        GameSettings gs = Minecraft.getMinecraft().gameSettings;
        if(isStart) {
            gs.keyBindLeft.setKeyCode(Keyboard.KEY_SLEEP);
            gs.keyBindRight.setKeyCode(Keyboard.KEY_DIVIDE);
            gs.keyBindForward.setKeyCode(Keyboard.KEY_NOCONVERT);
            gs.keyBindBack.setKeyCode(Keyboard.KEY_SYSRQ);
        }else{
            gs.keyBindLeft.setKeyCode(Keyboard.KEY_A);
            gs.keyBindRight.setKeyCode(Keyboard.KEY_D);
            gs.keyBindForward.setKeyCode(Keyboard.KEY_W);
            gs.keyBindBack.setKeyCode(Keyboard.KEY_S);
        }
        KeyBinding.resetKeyBindingArrayAndHash();
    }
    public static double xCoord() {
        if (player.getRidingEntity() instanceof EntityMinecartEmpty) {
            return xCoord * 1.5;
        } else
            return xCoord;
    }

    public static double zCoord() {
        if (player.getRidingEntity() instanceof EntityMinecartEmpty) {
            return zCoord * 1.5;
        } else
            return zCoord;
    }

    public static void setFakePositionUpdate() {
        double posX = player.posX + curX + EntityAPI.lookX(player, 3);
        double posZ = player.posZ + curZ + EntityAPI.lookZ(player, 3);
        runner.moveEntity(posX - runner.posX, 0, posZ - runner.posZ);
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


    @Override
    public boolean end(Object... obj) {
        keySetting(false);
        setElytra(EnumElytra.RUNNING);
        curX = 0;
        curY = 0;
        curZ = 0;
        xCoord = 0;
        zCoord = 0;
        MiniGame.mineRunEvent.lineX = 0;
        MiniGame.mineRunEvent.lineZ = 0;
        MiniGame.mineRunEvent.lineLR = 0;
        MiniGame.mineRunEvent.lineUD = 0;
        WorldAPI.command("/minerun lava");
        Camera.getCamera().reset();
        player.setGameType(GameType.CREATIVE);
        return super.end();
    }
}
