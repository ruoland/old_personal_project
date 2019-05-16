package minigameLib.minigame.starmine;

import cmplus.camera.Camera;
import minigameLib.MiniGame;
import olib.api.Direction;
import olib.api.PosHelper;
import olib.api.WorldAPI;
import olib.fakeplayer.EntityFakePlayer;
import olib.fakeplayer.FakePlayerHelper;
import minigameLib.minigame.AbstractMiniGame;

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
