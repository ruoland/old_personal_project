package minigameLib.minigame.elytra;

import cmplus.camera.Camera;
import cmplus.deb.DebAPI;
import minigameLib.MiniGame;
import olib.api.Direction;
import olib.api.PosHelper;
import olib.api.WorldAPI;
import olib.effect.TickTask;
import olib.effect.TickRegister;
import olib.fakeplayer.EntityFakePlayer;
import olib.fakeplayer.FakePlayerHelper;
import minigameLib.minigame.AbstractMiniGame;
import minigameLib.minigame.elytra.miniween.EntityElytraBossWeen;
import minigameLib.minigame.elytra.miniween.EntityElytraPumpkin;
import minigameLib.minigame.elytra.miniween.EntityElytraPumpkinAttack;
import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.gameevent.TickEvent.Type;

import java.util.ArrayList;

public class Elytra extends AbstractMiniGame {
    public static EntityFlyingWeen flyingWeen;
    public static boolean bossEnd, tripleArrow, tntArrow;
    public static int killCount, score;
    public double playerSpawnX, playerSpawnY, playerSpawnZ, targetX, targetY, targetZ;
    public static int elytraCooltime = 0, defaultCooltime = 10, bombCooltime = 0, tntCooltime = 0;

    private EntityFakePlayer fakePlayer;
    public PosHelper spawnPosHelper;
    private ArrayList<EntityElytraPumpkin> monsterList = new ArrayList<>();

    public Elytra() {
    }

    public void addBombCount(){
        MiniGame.elytraEvent.bomberStack.stackSize++;
    }
    @Override
    public boolean start(Object... obj) {

        WorldAPI.command("/display size 700 950");
        WorldAPI.teleport(WorldAPI.x(), WorldAPI.y() + 55, WorldAPI.z());

        fakePlayer = FakePlayerHelper.spawnFakePlayer(true);
        spawnPosHelper = new PosHelper(fakePlayer.getPositionVector(), fakePlayer.getHorizontalFacing());

        this.setFakePlayerUse();
        playerSpawnX = fakePlayer.posX;
        playerSpawnY = fakePlayer.posY - 45;
        playerSpawnZ = fakePlayer.posZ;
        WorldAPI.teleport(WorldAPI.x(), WorldAPI.y() + 5, WorldAPI.z());
        cameraSetting();

        TickRegister.register(new TickTask(100  , true) {
            @Override
            public boolean stopCondition() {
                return !isStart();
            }

            @Override
            public void run(Type type) {
                if (absRunCount == 0){
                    String facingName = WorldAPI.getPlayer().getHorizontalFacing().getName();

                    if (facingName.equalsIgnoreCase("NORTH")) {
                        targetX = fakePlayer.getX(Direction.FORWARD, 20, true);
                        targetZ = fakePlayer.getZ(Direction.FORWARD, 20, true);
                    }
                    if (facingName.equalsIgnoreCase("SOUTH")) {
                        targetX = fakePlayer.getX(Direction.BACK, 20, true);
                        targetZ = fakePlayer.getZ(Direction.BACK, 20, true);
                    }
                    if (facingName.equalsIgnoreCase("WEST")) {
                        targetX = fakePlayer.getX(Direction.BACK, 20, true);
                        targetZ = fakePlayer.getZ(Direction.BACK, 20, true);
                    }
                    if (facingName.equalsIgnoreCase("EAST")) {
                        targetX = fakePlayer.getX(Direction.FORWARD, 20, true);
                        targetZ = fakePlayer.getZ(Direction.FORWARD, 20, true);
                    }
                    boss();
                    absLoop = false;
                }
                if (absRunCount == 1) {
                    returnMonster();
                    six();
                }
                if (absRunCount == 2) {
                    returnMonster();
                    five();
                }
                if (absRunCount == 3) {
                    returnMonster();
                    six();
                }
                if (absRunCount == 4) {
                    returnMonster();
                    five();
                }
                if (absRunCount == 5) {
                    returnMonster();
                    six();
                    absLoop = false;
                }
            }
        });
        return super.start();
    }

    public void returnMonster() {
        for (EntityElytraPumpkin elytraPumpkin : monsterList) {
            if (elytraPumpkin.isDead) {
                continue;
            }
            elytraPumpkin.setTarget(elytraPumpkin.getReturnX(), elytraPumpkin.getReturnY(), elytraPumpkin.getReturnZ()).setTargetSpeed(0.5);
            elytraPumpkin.setAttackMode(false);
            elytraPumpkin.setDeathTimer(100);
            elytraPumpkin.setReturn(true);
            System.out.println(elytraPumpkin.getReturnX()+ " - "+elytraPumpkin.getReturnY() + " - "+elytraPumpkin.getReturnZ());
        }
        monsterList.clear();
    }

    public void first() {
        WorldAPI.addMessage("퍼스ㅡㅌ");
        for (int i = 1; i < 3; i++) {
            spawnPumpkinFire(Direction.FORWARD_RIGHT, i + 1).attackCooldown = 30;
            spawnPumpkinFire(Direction.FORWARD_LEFT, i + 1).defaultCooldown = 30;
        }
        for (int i = 1; i < 4; i++) {
            spawnPumpkinAttack(Direction.FORWARD, 5+i, 0).setForwardMode(true).setTargetSpeed(0.2F);
        }
        //spawnPumpkinAttack(Direction.LEFT, 5, 0);
        //spawnPumpkinAttack(Direction.RIGHT, 5, 0);
    }

    public void second() {
        WorldAPI.addMessage("둘");
        for (int i = 1; i < 3; i++) {
            spawnPumpkinFire(Direction.FORWARD_RIGHT, i + 3).setHealth(2);
            spawnPumpkinFire(Direction.FORWARD_LEFT, i + 3).setHealth(2);
        }
        for (int i = 1; i < 4; i++) {
            spawnPumpkinAttack(Direction.FORWARD_RIGHT, 5+i, i);
            spawnPumpkinAttack(Direction.FORWARD_LEFT, 5+i, i);
        }
        spawnPumpkinAttack(Direction.FORWARD, 5, 0).setFireAttack(true);
    }

    public void third() {
        WorldAPI.addMessage("셋");
        spawnPumpkinAttack(Direction.FORWARD_RIGHT, 8, 2).setForwardMode(true).setTargetSpeed(0.8);
        spawnPumpkinAttack(Direction.FORWARD_LEFT, 8, 2).setForwardMode(true).setTargetSpeed(0.4);
        spawnPumpkinAttack(Direction.FORWARD_RIGHT, 9, 3).setForwardMode(true).setTargetSpeed(0.8);
        spawnPumpkinAttack(Direction.FORWARD_LEFT, 9, 3).setForwardMode(true).setTargetSpeed(0.4);
        spawnPumpkinAttack(Direction.FORWARD_RIGHT, 9, 4).setForwardMode(true).setTargetSpeed(0.8);
        spawnPumpkinAttack(Direction.FORWARD_LEFT, 9, 4).setForwardMode(true).setTargetSpeed(0.4);
    }
    public void four() {
        WorldAPI.addMessage("네번째");
        spawnPumpkinAttack(Direction.FORWARD_RIGHT, 8, 2).setTNTMode(true);
        spawnPumpkinAttack(Direction.FORWARD_LEFT, 8, 2).setTNTMode(true);
    }
    public void five() {
        WorldAPI.addMessage("다섯번째");
        spawnPumpkinAttack(Direction.FORWARD_RIGHT, 8, 2).setWaterMode(true);
        spawnPumpkinAttack(Direction.FORWARD_LEFT, 8, 2).setWaterMode(true);
    }
    public void six() {
        WorldAPI.addMessage("여섯번째");
        spawnPumpkinAttack(Direction.FORWARD, 8, 0).setTeleportMode(true);
        spawnPumpkinAttack(Direction.FORWARD, 10, 0).setTeleportMode(true);
        //spawnPumpkinAttack(Direction.FORWARD_RIGHT, 8, 2).setWaterMode(true);
        //spawnPumpkinAttack(Direction.FORWARD_LEFT, 8, 2).setWaterMode(true);
    }
    public void boss() {
        WorldAPI.addMessage("보스");
        EntityElytraBossWeen bossWeen = new EntityElytraBossWeen(WorldAPI.getWorld());
        setPositionAndSpawn(Direction.FORWARD, bossWeen, 26, 0);
        WorldAPI.getWorld().spawnEntityInWorld(bossWeen);
        bossWeen.setTarget(spawnPosHelper.getXZ(Direction.FORWARD, 6, 0, true));
        bossWeen.setTargetSpeed(0.2);
    }
    @Override
    public boolean end(Object... obj) {
        Camera.reset();
        ElytraEvent eve = MiniGame.elytraEvent;
        WorldAPI.command("/display size 854 480");
        WorldAPI.command("/ui reset");
        bossEnd = true;
        WorldAPI.teleport(playerSpawnX, playerSpawnY, playerSpawnZ);
        for (EntityElytraPumpkin pumpkin : monsterList)
            pumpkin.setDead();
        monsterList.clear();

        return super.end();
    }


    public EntityElytraPumpkinAttack  spawnPumpkinAttack(Direction direction, int plus, int rlplus) {
        EntityElytraPumpkinAttack pumpkin = new EntityElytraPumpkinAttack(WorldAPI.getWorld());
        setPositionAndSpawn(direction, pumpkin, plus+20, rlplus+6);
        pumpkin.setTarget(spawnPosHelper.getXZ(direction, plus, rlplus, true));
        pumpkin.setTargetSpeed(0.2);
        DebAPI.msgText("MiniGame",""+spawnPosHelper.getXZ(direction, plus, rlplus, true));
        pumpkin.setAttackMode(true);
        pumpkin.setDirection(direction);
        return pumpkin;
    }

    /**
     * 플레이어 20블럭 앞에 불타는 호박을 소환함
     * 이 호박은 플레이어 뒤로 이동하면서 플레이어를 공격함
     */
    public EntityElytraPumpkinAttack spawnPumpkinFire(Direction direction, int rlPlus) {
        EntityElytraPumpkinAttack pumpkin = new EntityElytraPumpkinAttack(WorldAPI.getWorld());
        if (direction == Direction.LEFT || direction == Direction.RIGHT)
            setPositionAndSpawn(direction, pumpkin, 6, rlPlus);
        else
            setPositionAndSpawn(direction, pumpkin, 20, rlPlus);
        pumpkin.setAttackMode(true);
        pumpkin.setForwardMode(true);
        pumpkin.setBlock(Blocks.BRICK_BLOCK);
        return pumpkin;
    }

    private void setPositionAndSpawn(Direction direction, EntityElytraPumpkin pumpkin, double plus, double rlplus) {
        switch (direction) {
            case FORWARD:
                pumpkin.setPosition(spawnPosHelper.getXZ(Direction.FORWARD, plus, true));
                break;
            case FORWARD_LEFT:
                pumpkin.setPosition(spawnPosHelper.getXZ(Direction.FORWARD_LEFT, plus, rlplus, true));
                break;
            case FORWARD_RIGHT:
                pumpkin.setPosition(spawnPosHelper.getXZ(Direction.FORWARD_RIGHT, plus, rlplus, true));
                break;
            case LEFT:
                pumpkin.setPosition(spawnPosHelper.getXZ(Direction.LEFT, plus, true));
                break;
            case RIGHT:
                pumpkin.setPosition(spawnPosHelper.getXZ(Direction.RIGHT, plus, true));
                break;
        }
        pumpkin.setDirection(direction);
        monsterList.add(pumpkin);
        pumpkin.updateSpawnPosition();
        pumpkin.setReturnPosition();
        WorldAPI.getWorld().spawnEntityInWorld(pumpkin);
    }

    public void cameraSetting() {
        WorldAPI.command("/gamemode " + "1");//게임모드 설정
        WorldAPI.command("/ui hotbar false");//핫바 끔
        WorldAPI.command("/ui hand false");//핸드 끔
        Camera.reset();
        Camera.setYP(true);
        Camera.lockCamera(true, 0, 0);
        Camera.moveCamera(0, -10, -5);
        Camera.rotateCamera(90, 180, 0);

    }

}
