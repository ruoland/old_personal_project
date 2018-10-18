package ruo.minigame.minigame.starmine;

import ruo.cmplus.camera.Camera;
import ruo.minigame.MiniGame;
import ruo.minigame.api.Direction;
import ruo.minigame.api.PosHelper;
import ruo.minigame.api.WorldAPI;
import ruo.minigame.fakeplayer.EntityFakePlayer;
import ruo.minigame.fakeplayer.FakePlayerHelper;
import ruo.minigame.minigame.AbstractMiniGame;

public class StarMine extends AbstractMiniGame {
    public static boolean bossEnd, tripleArrow;
    public static int bombCount;
    public double playerSpawnX, playerSpawnY, playerSpawnZ;
    private EntityFakePlayer fakePlayer;
    public PosHelper spawnPosHelper;

    public StarMine() {
    }

    @Override
    public boolean start(Object... obj) {
        fakePlayer = FakePlayerHelper.spawnFakePlayer(true);
        fakePlayer.setElytra(true);
        playerSpawnX = fakePlayer.posX;
        playerSpawnY = fakePlayer.posY;
        playerSpawnZ = fakePlayer.posZ;
        spawnPosHelper = new PosHelper(fakePlayer.getPositionVector(), fakePlayer.getHorizontalFacing());
        fakePlayer.setPosition(spawnPosHelper.getXZ(Direction.FORWARD, 5, true));
        fakePlayer.isFly = true;
        return super.start();
    }
    @Override
    public boolean end(Object... obj) {
        Camera.getCamera().reset();
        StarMineEvent eve = MiniGame.starMineEvent;
        eve.spawnY = 0;
        WorldAPI.command("/ui reset");
        bossEnd = true;
        WorldAPI.teleport(playerSpawnX, playerSpawnY, playerSpawnZ);

        return super.end();
    }

}
