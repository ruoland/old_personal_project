package minigameLib.fakeplayer;

import minigameLib.api.WorldAPI;
import net.minecraft.client.Minecraft;

public class FakePlayerHelper {

    public static EntityFakePlayer fakePlayer;

    public static boolean isMove(){
        return fakePlayer != null && Minecraft.getMinecraft().currentScreen == null;
    }
    public static String index() {
        return fakePlayer.getHorizontalFacing().getName();
    }

    public static EntityFakePlayer spawnFakePlayer(boolean isElytra) {
        if(fakePlayer != null)
            fakePlayer.setDead();
        EntityFakePlayer fake = new EntityFakePlayer(WorldAPI.getWorld());
        fake.setPosition(WorldAPI.x(), WorldAPI.y(), WorldAPI.z());
        fake.setElytra(isElytra);
        WorldAPI.getWorld().spawnEntityInWorld(fake);
        fake.setPositionAndRotation(WorldAPI.x(), WorldAPI.y(), WorldAPI.z(), WorldAPI.getPlayer().rotationYaw, WorldAPI.getPlayer().rotationPitch);
        return fakePlayer = fake;
    }

    public static void setFakeDead() {
        if (fakePlayer == null)
            return;
        if(!fakePlayer.isDead)
        fakePlayer.setDead();
    }

    public static boolean isDead(){
        return fakePlayer == null || fakePlayer.isDead;
    }

}
