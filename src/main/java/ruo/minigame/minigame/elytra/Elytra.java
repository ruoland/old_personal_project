package ruo.minigame.minigame.elytra;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.gameevent.TickEvent.Type;
import ruo.cmplus.camera.Camera;
import ruo.cmplus.cm.CommandUI;
import ruo.minigame.MiniGame;
import ruo.minigame.api.PosHelper;
import ruo.minigame.api.SpawnDirection;
import ruo.minigame.api.WorldAPI;
import ruo.minigame.effect.AbstractTick;
import ruo.minigame.effect.TickRegister;
import ruo.minigame.fakeplayer.EntityFakePlayer;
import ruo.minigame.fakeplayer.FakePlayerHelper;
import ruo.minigame.minigame.AbstractMiniGame;
import ruo.minigame.minigame.elytra.miniween.EntityElytraPumpkin;
import ruo.minigame.minigame.elytra.miniween.EntityElytraPumpkinAttack;
import ruo.minigame.minigame.elytra.miniween.EntityElytraPumpkinFire;

import java.util.ArrayList;

public class Elytra extends AbstractMiniGame {
    public static EntityFlyingWeen flyingWeen;
    public static boolean bossEnd, arrowUpgrade;
    public static int arrowDistance, bombCount;
    private EnumFacing facing;
    public double playerSpawnX, playerSpawnY, playerSpawnZ, targetX, targetY, targetZ;
    private EntityFakePlayer fakePlayer;
    private PosHelper spawnPosHelper;
    private ArrayList<EntityElytraPumpkin> monsterList = new ArrayList<>();

    public Elytra() {
    }

    @Override
    public boolean start(Object... obj) {
        WorldAPI.command("/display size 700 950");
        facing = WorldAPI.getPlayer().getHorizontalFacing();
        WorldAPI.teleport(WorldAPI.x(), WorldAPI.y() + 55, WorldAPI.z());

        fakePlayer = FakePlayerHelper.spawnFakePlayer(true);
        playerSpawnX = fakePlayer.posX;
        playerSpawnY = fakePlayer.posY - 45;
        playerSpawnZ = fakePlayer.posZ;
        WorldAPI.teleport(WorldAPI.x(), WorldAPI.y() + 5, WorldAPI.z());
        cameraSetting();
        MiniGame.elytraEvent.elytraMode = true;
        spawnPosHelper = new PosHelper(fakePlayer.getPositionVector(), fakePlayer.getHorizontalFacing());

        TickRegister.register(new AbstractTick(100, true) {
            @Override
            public boolean stopCondition() {
                return !isStart();
            }

            @Override
            public void run(Type type) {
                if (absRunCount == 0)
                    first();
                if (absRunCount == 1) {
                    returnMonster();
                    second();
                }
                if (absRunCount == 2) {
                    returnMonster();
                    third();
                    absLoop = false;
                }
            }
        });
        return super.start();
    }

    public void returnMonster() {
        for (EntityElytraPumpkin elytraPumpkin : monsterList) {
            System.out.println(spawnPosHelper.getXZ(elytraPumpkin.getSpawnDirection().simple(), 15, true) + " - " + elytraPumpkin.getSpawnDirection().simple());
            elytraPumpkin.setTarget(spawnPosHelper.getXZ(elytraPumpkin.getSpawnDirection().simple(), 15, true)).setTargetSpeed(0.2);
            elytraPumpkin.setBlockMode(Blocks.STONE);
        }
    }

    public void first() {
        System.out.println("퍼스ㅡㅌ");
        for (int i = 1; i < 3; i++) {
            spawnPumpkinFire(SpawnDirection.FORWARD_RIGHT, i + 1);
        }
        for (int i = 1; i < 3; i++) {
            spawnPumpkinFire(SpawnDirection.FORWARD_LEFT, i + 1);
        }
        spawnPumpkinAttack(SpawnDirection.FORWARD).setFireAttack(true);
        spawnPumpkinAttack(SpawnDirection.LEFT);
        spawnPumpkinAttack(SpawnDirection.RIGHT);
    }

    public void second() {
        System.out.println("세컨드");
        for (int i = 1; i < 4; i++) {
            EntityElytraPumpkinFire elytraPumpkin = new EntityElytraPumpkinFire(WorldAPI.getWorld());
            setPositionAndSpawn(SpawnDirection.FORWARD_LEFT, elytraPumpkin, i + 2);
            elytraPumpkin.setHealth(2);
        }
        for (int i = 1; i < 4; i++) {
            EntityElytraPumpkinFire elytraPumpkin = new EntityElytraPumpkinFire(WorldAPI.getWorld());
            setPositionAndSpawn(SpawnDirection.FORWARD_RIGHT, elytraPumpkin, i + 2);
            elytraPumpkin.setHealth(2);
        }
        spawnPumpkinAttack(SpawnDirection.FORWARD).setFireAttack(true);
    }

    public void third() {
        System.out.println("서드");
        spawnPumpkinAttack(SpawnDirection.FORWARD_RIGHT).setForwardMode(true).setAttack(true).setTargetSpeed(0.8);
        spawnPumpkinAttack(SpawnDirection.FORWARD_LEFT).setForwardMode(true).setAttack(true).setTargetSpeed(0.4);
    }

    @Override
    public boolean end(Object... obj) {
        Camera.getCamera().reset();
        ElytraEvent eve = MiniGame.elytraEvent;
        eve.spawnY = 0;
        WorldAPI.command("/display size 854 480");
        WorldAPI.command("/ui reset");
        bossEnd = true;
        WorldAPI.teleport(playerSpawnX, playerSpawnY, playerSpawnZ);
        for (EntityElytraPumpkin pumpkin : monsterList)
            pumpkin.setDead();
        monsterList.clear();

        return super.end();
    }


    public EntityElytraPumpkinAttack spawnPumpkinAttack(SpawnDirection direction) {
        EntityElytraPumpkinAttack pumpkin = new EntityElytraPumpkinAttack(WorldAPI.getWorld());
        setPositionAndSpawn(direction, pumpkin, 3);
        pumpkin.setAttack(true);
        return pumpkin;
    }

    public EntityElytraPumpkinAttack spawnPumpkinFire(SpawnDirection direction, int rlPlus) {
        EntityElytraPumpkinAttack pumpkin = new EntityElytraPumpkinAttack(WorldAPI.getWorld());
        setPositionAndSpawn(direction, pumpkin, 20, rlPlus);
        pumpkin.setAttack(true);
        pumpkin.setTarget(spawnPosHelper.getX(direction.simple().reverse(), 10, true), pumpkin.posY, spawnPosHelper.getZ(direction.simple().reverse(), 10, true));
        return pumpkin;
    }

    private void setPositionAndSpawn(SpawnDirection direction, EntityElytraPumpkin pumpkin, double rlplus) {
        setPositionAndSpawn(direction, pumpkin, 8, rlplus);
    }

    private void setPositionAndSpawn(SpawnDirection direction, EntityElytraPumpkin pumpkin, double plus, double rlplus) {
        System.out.println("몬스터 소환됨");
        switch (direction) {
            case FORWARD:
                pumpkin.setPosition(spawnPosHelper.getXZ(SpawnDirection.FORWARD, plus, true));
                break;
            case FORWARD_LEFT:
                pumpkin.setPosition(spawnPosHelper.getXZ(SpawnDirection.FORWARD_LEFT, plus, rlplus, true));
                break;
            case FORWARD_RIGHT:
                pumpkin.setPosition(spawnPosHelper.getXZ(SpawnDirection.FORWARD_RIGHT, plus, rlplus, true));
                break;
            case LEFT:
                pumpkin.setPosition(spawnPosHelper.getXZ(SpawnDirection.LEFT, plus - 2, true));
                break;
            case RIGHT:
                pumpkin.setPosition(spawnPosHelper.getXZ(SpawnDirection.RIGHT, plus - 2, true));
                break;
        }
        pumpkin.setDirection(direction);
        monsterList.add(pumpkin);
        WorldAPI.getWorld().spawnEntityInWorld(pumpkin);
    }

    public void cameraSetting() {
        String facingName = WorldAPI.getPlayer().getHorizontalFacing().getName();
        WorldAPI.command("/gamemode " + "1");//게임모드 설정
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
