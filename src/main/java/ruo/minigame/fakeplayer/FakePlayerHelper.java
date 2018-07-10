package ruo.minigame.fakeplayer;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumFacing;
import ruo.minigame.MiniGame;
import ruo.minigame.api.EntityAPI;
import ruo.minigame.api.WorldAPI;

public class FakePlayerHelper {

    public static EntityFakePlayer fakePlayer;
    public static double forwardLeftX(double plus, boolean pos) {
        return EntityAPI.forwardLeftX(fakePlayer, plus, pos);
    }
    public static double forwardLeftZ(double plus, boolean pos) {
        return EntityAPI.forwardLeftZ(fakePlayer, plus, pos);
    }
    public static double forwardRightX(double plus, boolean pos) {
        return EntityAPI.forwardRightX(fakePlayer, plus, pos);
    }
    public static double forwardRightZ(double plus, boolean pos) {
        return EntityAPI.forwardRightZ(fakePlayer, plus, pos);
    }

    public static double forwardX(double plus, boolean pos) {
        return EntityAPI.forwardX(fakePlayer, fakePlayer.getHorizontalFacing(), plus, pos);
    }

    public static double forwardZ(double plus, boolean pos) {
        return EntityAPI.forwardZ(fakePlayer, fakePlayer.getHorizontalFacing(), plus, pos);
    }

    public static double backX(double minus, boolean pos)
    {
        return EntityAPI.forwardX(fakePlayer, -minus, pos);
    }

    public static double backZ(double minus, boolean pos) {
        return EntityAPI.forwardZ(fakePlayer,-minus, pos);
    }

    public static boolean isMove(){
        return fakePlayer != null && Minecraft.getMinecraft().currentScreen == null;
    }
    public static String index() {
        return fakePlayer.getHorizontalFacing().getName();
    }

    public static EntityFakePlayer spawnFakePlayer(boolean isElytra) {
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
