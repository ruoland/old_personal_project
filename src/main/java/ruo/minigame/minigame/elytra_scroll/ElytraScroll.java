package ruo.minigame.minigame.elytra_scroll;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import ruo.cmplus.camera.Camera;
import ruo.minigame.MiniGame;
import ruo.minigame.api.PosHelper;
import ruo.minigame.api.WorldAPI;
import ruo.minigame.fakeplayer.EntityFakePlayer;
import ruo.minigame.fakeplayer.FakePlayerHelper;
import ruo.minigame.minigame.AbstractMiniGame;

public class ElytraScroll extends AbstractMiniGame {
    public static boolean bossEnd, tripleArrow;
    public static int bombCount;
    public double playerSpawnX, playerSpawnY, playerSpawnZ;
    private EntityFakePlayer fakePlayer;
    public PosHelper spawnPosHelper;

    public ElytraScroll() {
    }

    @Override
    public boolean start(Object... obj) {
        fakePlayer = FakePlayerHelper.spawnFakePlayer(true);
        fakePlayer.setElytra(true);
        playerSpawnX = fakePlayer.posX;
        playerSpawnY = fakePlayer.posY;
        playerSpawnZ = fakePlayer.posZ;
        cameraSetting();
        spawnPosHelper = new PosHelper(fakePlayer.getPositionVector(), fakePlayer.getHorizontalFacing());
        return super.start();
    }
    @Override
    public boolean end(Object... obj) {
        Camera.getCamera().reset();
        ElytraScrollEvent eve = MiniGame.elytraScrollEvent;
        eve.spawnY = 0;
        WorldAPI.command("/display size 854 480");
        WorldAPI.command("/ui reset");
        bossEnd = true;
        WorldAPI.teleport(playerSpawnX, playerSpawnY, playerSpawnZ);

        return super.end();
    }

    public void cameraSetting() {
        String facingName = WorldAPI.getPlayer().getHorizontalFacing().getName();
        WorldAPI.getPlayer().inventory.armorInventory[2] = new ItemStack(Items.ELYTRA);
        Camera.getCamera().reset();
        if (facingName.equalsIgnoreCase("NORTH")) {

            Camera.getCamera().lockCamera(true, 180, 0);
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
    }

}
