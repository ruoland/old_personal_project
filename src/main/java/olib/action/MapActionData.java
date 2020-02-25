package olib.action;

import net.minecraftforge.common.config.ConfigCategory;
import olib.OLib;

/**
 * 맵마다 다른 액션 설정을 저장하기 위해서 있는 클래스
 */
public class MapActionData {
    private String mapName;
    private boolean canUseCrawl;
    private boolean canUseDoubleJump;
    private double yLimit;
    private float tpPitch, tpYaw;
    private boolean isPlayerJump;
    private boolean canRestart;
    public MapActionData(String mapName) {
        this.mapName = mapName;
        load();
    }


    public void crawl(boolean on) {
        canUseCrawl = on;
    }

    public boolean canCrawl() {
        return canUseCrawl;
    }

    public void mapDoubleJump(boolean on) {
        canUseDoubleJump = on;
    }

    public boolean canMapDoubleJump() {
        return canUseDoubleJump;
    }

    public boolean canRestart() {
        return canRestart;
    }

    public void setCanRestart(boolean canRestart) {
        this.canRestart = canRestart;
    }

    public void setYTP(double tpy, float pitch, float yaw) {
        this.yLimit = tpy;
        tpPitch = pitch;
        tpYaw = yaw;
    }

    public void setYP(float yaw, float pitch) {
        tpPitch = pitch;
        tpYaw = yaw;
    }

    public double getYTP() {
        return yLimit;
    }

    public float getYaw() {
        return tpYaw;
    }

    public float getPitch() {
        return tpPitch;
    }


    public void save() {
        String worldName = mapName;
        if (worldName != null && !worldName.equalsIgnoreCase("noworld")) {
            OLib.config.get(worldName, "crawl", false).set(canUseCrawl);
            OLib.config.get(worldName, "doubleJump", false).set(canUseDoubleJump);
            OLib.config.get(worldName, "tpY", -12345).set(yLimit);
            OLib.config.get(worldName, "tpYaw", 0).set(tpYaw);
            OLib.config.get(worldName, "tpPitch", 0).set(tpPitch);
            OLib.config.get(worldName, "canRestart", false).set(canRestart);
        }
    }

    public void load() {

        for (String category : OLib.config.getCategoryNames()) {
            if (category.equalsIgnoreCase(mapName))
                category = mapName;
            ConfigCategory action = OLib.config.getCategory(category);
            canUseCrawl = action.get("crawl").getBoolean();
            canUseDoubleJump = action.get("doubleJump").getBoolean();
            yLimit = action.get("tpY").getDouble();
            tpYaw = (float) action.get("tpYaw").getDouble();
            tpPitch = (float) action.get("tpPitch").getDouble();
            canRestart = action.get("canRestart").getBoolean();
        }
    }
}


