package ruo.minigame.minigame.elytra;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.gameevent.TickEvent.Type;
import ruo.cmplus.camera.Camera;
import ruo.minigame.MiniGame;
import ruo.minigame.api.PosHelper;
import ruo.minigame.api.WorldAPI;
import ruo.minigame.effect.AbstractTick;
import ruo.minigame.effect.TickRegister;
import ruo.minigame.fakeplayer.EntityFakePlayer;
import ruo.minigame.fakeplayer.FakePlayerHelper;
import ruo.minigame.map.EntityDefaultNPC;
import ruo.minigame.minigame.AbstractMiniGame;
import ruo.minigame.minigame.elytra.miniween.EntityElytraPumpkin;
import ruo.minigame.minigame.elytra.miniween.EntityElytraWeenCore;
import ruo.minigame.api.SpawnDirection;
import scala.xml.dtd.EntityDef;

public class Elytra extends AbstractMiniGame {
    public static EntityFlyingWeen flyingWeen;
    public static boolean bossEnd, arrowUpgrade;
    public static int arrowDistance, bombCount;
    private EnumFacing facing;
    public double playerSpawnX, playerSpawnY, playerSpawnZ, targetX, targetY, targetZ;
    private EntityFakePlayer fakePlayer;

    public Elytra() {
    }

    @Override
    public boolean start(Object... obj) {
        facing = WorldAPI.getPlayer().getHorizontalFacing();
        WorldAPI.teleport(WorldAPI.x(), WorldAPI.y() + 55, WorldAPI.z());
        fakePlayer = FakePlayerHelper.spawnFakePlayer(true);
        playerSpawnX = fakePlayer.posX;
        playerSpawnY = fakePlayer.posY - 45;
        playerSpawnZ = fakePlayer.posZ;
        WorldAPI.teleport(WorldAPI.x(), WorldAPI.y() + 5, WorldAPI.z());
        cameraSetting();
        first();
        MiniGame.elytraEvent.elytraMode = true;

        return super.start();
    }

    public void cameraSetting() {
        String facingName = WorldAPI.getPlayer().getHorizontalFacing().getName();
        WorldAPI.command("/gamemode " + " 1");//게임모드 설정
        WorldAPI.command("/ui hotbar false");//핫바 끔
        WorldAPI.command("/ui hand false");//핸드 끔
        Camera.getCamera().reset();
        Camera.getCamera().setYP(true);
        Camera.getCamera().lockCamera(true, 0, 0);
        Camera.getCamera().moveCamera(0, -10, -5);
        Camera.getCamera().rotateCamera(90, 180, 0);
        if (facingName.equalsIgnoreCase("NORTH")) {
            targetX = fakePlayer.getX(SpawnDirection.FORWARD, 20, true);
            targetZ = fakePlayer.getZ(SpawnDirection.FORWARD, 20, true);
        }
        if (facingName.equalsIgnoreCase("SOUTH")) {
            targetX = fakePlayer.getX(SpawnDirection.BACK, 20, true);
            targetZ = fakePlayer.getZ(SpawnDirection.BACK, 20, true);
        }
        if (facingName.equalsIgnoreCase("WEST")) {
            targetX = fakePlayer.getX(SpawnDirection.BACK, 20, true);
            targetZ = fakePlayer.getZ(SpawnDirection.BACK, 20, true);
        }
        if (facingName.equalsIgnoreCase("EAST")) {
            targetX = fakePlayer.getX(SpawnDirection.FORWARD, 20, true);
            targetZ = fakePlayer.getZ(SpawnDirection.FORWARD, 20, true);
        }
    }

    public void first() {
        TickRegister.register(new AbstractTick(30, false) {
            @Override
            public void run(Type type) {
                spawnPumpkin(SpawnDirection.FORWARD);
                spawnPumpkin(SpawnDirection.FORWARD_RIGHT);
                spawnPumpkin(SpawnDirection.FORWARD_LEFT);
            }
        });

        TickRegister.register(new AbstractTick(180, false) {
            @Override
            public void run(Type type) {
            }
        });
    }

    public void second() {

    }

    public void spawnPumpkin(SpawnDirection direction) {
        EntityElytraPumpkin pumpkin = new EntityElytraPumpkin(WorldAPI.getWorld());
        switch (direction) {
            case FORWARD:
                pumpkin.setPosition(fakePlayer.getXZ(SpawnDirection.FORWARD, 16, true));
            case FORWARD_LEFT:
                pumpkin.setPosition(fakePlayer.getXZ(SpawnDirection.FORWARD_LEFT, 16, 3, true));
            case FORWARD_RIGHT:
                pumpkin.setPosition(fakePlayer.getXZ(SpawnDirection.FORWARD_RIGHT, 16, 3, true));
        }
        System.out.println(pumpkin.getPositionVector());
        WorldAPI.getWorld().spawnEntityInWorld(pumpkin);
        pumpkin.setDirection(SpawnDirection.FORWARD);
    }

    public void spawnWeen() {
        flyingWeen = new EntityFlyingWeen(WorldAPI.getWorld());
        flyingWeen.setPosition(fakePlayer.getXZ(SpawnDirection.FORWARD, 20, true).add(0, -5, 0));
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
                    cameraSetting();
                }
                double spawnPosX = fakePlayer.getX(SpawnDirection.FORWARD, 20, true);
                double spawnPosY = fakePlayer.posY;
                double spawnPosZ = fakePlayer.getZ(SpawnDirection.FORWARD, 20, true);

                if (absRunCount == 0)
                    absDefTick = 20;
                if (absRunCount >= 8) {
                    flyingWeen.secondPattern();
                    absLoop = false;
                    return;
                }
                for (int i = 0; i < 5; i++) {
                    double rightX = fakePlayer.getX(SpawnDirection.RIGHT, WorldAPI.rand(5), false);
                    double rightZ = fakePlayer.getZ(SpawnDirection.RIGHT, WorldAPI.rand(5), false);
                    spawnWeenElytra(spawnPosX + rightX, spawnPosY, spawnPosZ + rightZ, targetX + rightX, targetY, targetZ + rightZ);
                }
            }
        });
    }

    @Override
    public boolean end(Object... obj) {
        Camera.getCamera().reset();
        if (MiniGame.elytraEvent instanceof ElytraEvent) {
            ElytraEvent eve = (ElytraEvent) MiniGame.elytraEvent;
            eve.spawnY = 0;
            WorldAPI.command("/display size 854 480");
            WorldAPI.command("/ui reset");
            bossEnd = true;
            WorldAPI.teleport(playerSpawnX, playerSpawnY, playerSpawnZ);
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

    public void a() {
        String facingName = null;
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
}
