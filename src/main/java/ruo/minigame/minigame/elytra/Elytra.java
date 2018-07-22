package ruo.minigame.minigame.elytra;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.gameevent.TickEvent.Type;
import ruo.cmplus.camera.Camera;
import ruo.minigame.MiniGame;
import ruo.minigame.api.WorldAPI;
import ruo.minigame.effect.AbstractTick;
import ruo.minigame.effect.TickRegister;
import ruo.minigame.fakeplayer.FakePlayerHelper;
import ruo.minigame.minigame.AbstractMiniGame;
import ruo.minigame.minigame.elytra.miniween.EntityElytraPumpkin;
import ruo.minigame.minigame.elytra.miniween.EntityElytraWeenCore;
import ruo.minigame.minigame.elytra.miniween.SpawnDirection;

public class Elytra extends AbstractMiniGame {
    public static EntityFlyingWeen flyingWeen;
    public static boolean bossEnd, arrowUpgrade;
    public static int arrowDistance, bombCount;
    private EnumFacing facing;
    public double playerSpawnX, playerSpawnY, playerSpawnZ, weenSpawnX, weenSpawnY, weenSpawnZ, targetX, targetY, targetZ;
    private int width, height;

    public Elytra() {
    }

    @Override
    public boolean start(Object... obj) {
        System.out.println("엘리트라 실행ㅚㅁ");
        width = Minecraft.getMinecraft().displayWidth;
        height = Minecraft.getMinecraft().displayHeight;

        String facingName = WorldAPI.getPlayer().getHorizontalFacing().getName();
        facing = WorldAPI.getPlayer().getHorizontalFacing();
        String type;

        if (obj.length > 0)
            type = (String) obj[0];
        else
            type = "없음";

        if (!type.equalsIgnoreCase("없음"))
            WorldAPI.command("/display size 700 950");

        if (obj[0].equals("0")) {// 0번이 위에서 아래를 보는 시점임
            if (FakePlayerHelper.fakePlayer != null)
                FakePlayerHelper.fakePlayer.setDead();
            WorldAPI.teleport(WorldAPI.x(), WorldAPI.y() + 55, WorldAPI.z());//60 위로 이동
            FakePlayerHelper.spawnFakePlayer(true);
            playerSpawnX = FakePlayerHelper.fakePlayer.posX;
            playerSpawnY = FakePlayerHelper.fakePlayer.posY;
            playerSpawnZ = FakePlayerHelper.fakePlayer.posZ;
            FakePlayerHelper.fakePlayer.rotationYaw = WorldAPI.getPlayer().getHorizontalFacing().getHorizontalAngle();
            WorldAPI.teleport(WorldAPI.x(), WorldAPI.y() + 5, WorldAPI.z());//카메라를 위해 더 이동
            //spawnWeen();
            cameraSetting(facingName);
            //firstPattern();
            first();
            ((ElytraEvent) MiniGame.elytraEvent).elytraMode = true;
        }
        if (obj[0].equals("1")) {
            WorldAPI.getPlayer().inventory.armorInventory[2] = new ItemStack(Items.ELYTRA);
            Camera.getCamera().reset();
            if (facingName.equalsIgnoreCase("NORTH")) {
                Camera.getCamera().lockCamera(true, 0, 180);
                Camera.getCamera().playerCamera(true);
                Camera.getCamera().moveCamera(-11.7, 0.199, 4.5);
                Camera.getCamera().rotateCamera(0, -60, 0);
            }
            if (facingName.equalsIgnoreCase("WEST")) {
                Camera.getCamera().lockCamera(true, 0, 0);
                Camera.getCamera().playerCamera(true);
                Camera.getCamera().moveCamera(0, -5, 0);
                Camera.getCamera().rotateCamera(0, 0, -50);
            }
            if (facingName.equalsIgnoreCase("SOUTH")) {
                Camera.getCamera().lockCamera(true, 0, 0);
                Camera.getCamera().playerCamera(true);
                Camera.getCamera().moveCamera(0, -5, 0);
                Camera.getCamera().rotateCamera(-50, 0, 0);
            }
            if (facingName.equalsIgnoreCase("EAST")) {
                Camera.getCamera().lockCamera(true, 0, 0);
                Camera.getCamera().playerCamera(true);
                Camera.getCamera().moveCamera(0, -5, 0);
                Camera.getCamera().rotateCamera(0, 0, 50);
                Camera.getCamera().moveCamera(-11.7, 0.199, 4.5);
            }
            ((ElytraEvent) MiniGame.elytraEvent).elytraMode = false;

        }
        return super.start();
    }

    public void cameraSetting(String facingName) {
        WorldAPI.command("/gamemode " + " 1");//게임모드 설정
        WorldAPI.command("/ui hotbar false");//핫바 끔
        WorldAPI.command("/ui hand false");//핸드 끔
        Camera.getCamera().reset();
        Camera.getCamera().setYP(true);
        if (facingName.equalsIgnoreCase("NORTH")) {
            Camera.getCamera().lockCamera(true, 0, 0);
            Camera.getCamera().moveCamera(0, -10, -5);
            Camera.getCamera().rotateCamera(90, 180, 0);
            targetX = FakePlayerHelper.forwardX(20, true);
            targetZ = FakePlayerHelper.forwardZ(20, true);
        }
        if (facingName.equalsIgnoreCase("SOUTH")) {
            Camera.getCamera().lockCamera(true, 0, 0);
            Camera.getCamera().moveCamera(0, -10, -5);
            Camera.getCamera().rotateCamera(90, 180, 0);
            targetX = FakePlayerHelper.backX(20, true);
            targetZ = FakePlayerHelper.backZ(20, true);
        }
        if (facingName.equalsIgnoreCase("WEST")) {
            Camera.getCamera().lockCamera(true, 0, 0);
            Camera.getCamera().moveCamera(0, -10, -5);
            Camera.getCamera().rotateCamera(90, 180, 0);
            targetX = FakePlayerHelper.backX(20, true);
            targetZ = FakePlayerHelper.backZ(20, true);
        }
        if (facingName.equalsIgnoreCase("EAST")) {
            Camera.getCamera().lockCamera(true, 0, 0);
            Camera.getCamera().moveCamera(0, -10, -5);
            Camera.getCamera().rotateCamera(90, 180, 0);
            targetX = FakePlayerHelper.forwardX(20, true);
            targetZ = FakePlayerHelper.forwardZ(20, true);
        }
    }

    public void first() {
        FakePlayerHelper.fakePlayer.rotationYaw = WorldAPI.getPlayer().getHorizontalFacing().getHorizontalAngle();

        TickRegister.register(new AbstractTick(60, false) {
            @Override
            public void run(Type type) {
                spawnElytraPumpkinForward();
                spawnElytraPumpkinForwardRight();
                spawnElytraPumpkinForwardLeft();
            }
        });

    }

    public void second(){

    }
    public void spawnElytraPumpkinForward() {
        EntityElytraPumpkin pumpkin = new EntityElytraPumpkin(WorldAPI.getWorld());
        setForwardPosition(pumpkin, 20);
        WorldAPI.getWorld().spawnEntityInWorld(pumpkin);
        pumpkin.setDirection(SpawnDirection.FORWARD);
    }
    public void spawnElytraPumpkinForwardRight() {
        EntityElytraPumpkin pumpkin = new EntityElytraPumpkin(WorldAPI.getWorld());
        setForwardRightPosition(pumpkin, 3, 20);
        WorldAPI.getWorld().spawnEntityInWorld(pumpkin);
        pumpkin.setDirection(SpawnDirection.FORWARD);
    }
    public void spawnElytraPumpkinForwardLeft() {
        EntityElytraPumpkin pumpkin = new EntityElytraPumpkin(WorldAPI.getWorld());
        setForwardLeftPosition(pumpkin, 3, 20);
        WorldAPI.getWorld().spawnEntityInWorld(pumpkin);
        pumpkin.setDirection(SpawnDirection.FORWARD);
    }

    public void setForwardPosition(EntityLivingBase base, double distance) {
        base.setPosition(FakePlayerHelper.forwardX(distance, true), FakePlayerHelper.fakePlayer.posY,
                FakePlayerHelper.forwardZ(distance, true));
    }

    public void setForwardRightPosition(EntityLivingBase base, double right, double distance) {
        base.setPosition(FakePlayerHelper.forwardX(distance, true) + FakePlayerHelper.forwardRightX(right, false), FakePlayerHelper.fakePlayer.posY,
                FakePlayerHelper.forwardZ(distance, true) + FakePlayerHelper.forwardRightZ(right, false));
    }

    public void setForwardLeftPosition(EntityLivingBase base, double left, double distance) {
        base.setPosition(FakePlayerHelper.forwardX(distance, true) + FakePlayerHelper.forwardLeftX(left, false), FakePlayerHelper.fakePlayer.posY,
                FakePlayerHelper.forwardZ(distance, true) + FakePlayerHelper.forwardLeftZ(left, false));
    }

    public void spawnWeen() {
        weenSpawnX = FakePlayerHelper.forwardX(20, true);
        weenSpawnY = FakePlayerHelper.fakePlayer.posY;
        weenSpawnZ = FakePlayerHelper.forwardZ(20, true);
        targetX = weenSpawnX;
        targetY = weenSpawnY;
        targetZ = weenSpawnZ;
        flyingWeen = new EntityFlyingWeen(WorldAPI.getWorld());
        flyingWeen.setPosition(weenSpawnX, weenSpawnY - 5, weenSpawnZ);
        WorldAPI.getWorld().spawnEntityInWorld(flyingWeen);
    }

    public void firstPattern() {
        TickRegister.register(new AbstractTick(60, true) {
            @Override
            public boolean stopCondition() {
                return !flyingWeen.isEntityAlive() || !isStart() || flyingWeen.deadFalling;
            }

            @Override
            public void run(Type type) {
                if (!facing.getName().equalsIgnoreCase(WorldAPI.getPlayer().getHorizontalFacing().getName())) {
                    facing = WorldAPI.getPlayer().getHorizontalFacing();
                    cameraSetting(facing.getName());
                }
                double spawnPosX = FakePlayerHelper.forwardX(20, true);
                double spawnPosY = FakePlayerHelper.fakePlayer.posY;
                double spawnPosZ = FakePlayerHelper.forwardZ(20, true);

                if (absRunCount == 0)
                    absDefTick = 20;
                if (absRunCount >= 8) {
                    flyingWeen.secondPattern();
                    absLoop = false;
                    return;
                }
                for (int i = 0; i < 5; i++) {
                    double rightX = FakePlayerHelper.forwardRightX(WorldAPI.rand(5), false);
                    double rightZ = FakePlayerHelper.forwardRightZ(WorldAPI.rand(5), false);
                    spawnWeenElytra(spawnPosX + rightX, spawnPosY, spawnPosZ + rightZ, targetX + rightX, targetY, targetZ + rightZ);
                }
            }
        });
    }

    @Override
    public boolean end(Object... obj) {
        Camera.getCamera().reset();
        if (MiniGame.elytraEvent != null && MiniGame.elytraEvent instanceof ElytraEvent) {
            ElytraEvent eve = (ElytraEvent) MiniGame.elytraEvent;
            eve.spawnY = 0;
            WorldAPI.command("/display size 854 480");
            WorldAPI.command("/ui reset");
            bossEnd = true;
            WorldAPI.teleport(playerSpawnX, playerSpawnY - 45, playerSpawnZ);
        }

        return super.end();
    }


    public static EntityElytraWeenCore spawnWeenElytra(double spawnPosX, double spawnPosY, double spawnPosZ, double targetPosX, double targetPosY, double targetPosZ) {
        EntityElytraWeenCore ween = new EntityElytraWeenCore(WorldAPI.getWorld());
        ween.setPosition(spawnPosX, spawnPosY, spawnPosZ);
        WorldAPI.getWorld().spawnEntityInWorld(ween);
        ween.setTarget(targetPosX, targetPosY, targetPosZ);

        return ween;
    }

}
