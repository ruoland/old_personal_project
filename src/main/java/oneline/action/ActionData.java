package oneline.action;

import minigameLib.MiniGame;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBasePressurePlate;
import net.minecraft.block.BlockLiquid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.ConfigCategory;
import oneline.OnlyOneLine;
import oneline.api.BlockAPI;
import oneline.api.WorldAPI;

import java.util.ArrayList;
import java.util.HashMap;

public class ActionData {
    private String mapName;
    private boolean canUseCrawl;
    private boolean canUseDoubleJump;
    private double yLimit;
    private float tpPitch, tpYaw;
    private boolean isPlayerJump;

    public ActionData(String mapName) {
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

    public boolean isPlayerJump() {
        return isPlayerJump;
    }

    public void setPlayerJump(boolean playerJump) {
        isPlayerJump = playerJump;
    }

    public void teleportSpawnPoint(EntityPlayer player) {
        Block block = player.worldObj.getBlockState(player.getBedLocation().add(0, -1, 0)).getBlock();
        if (block instanceof BlockLiquid || player.worldObj.isAirBlock(player.getBedLocation().add(0, -1, 0))) {
            BlockAPI blockAPI = WorldAPI.getBlock(player.worldObj, player.getBedLocation(), 4D);
            for (int i = 0; i < blockAPI.size(); i++) {
                if (blockAPI.getBlock(i) instanceof BlockBasePressurePlate) {
                    player.setSpawnPoint(blockAPI.getPos(i), true);
                    WorldAPI.teleport(player.getBedLocation().add(0, 1, 0), ActionEffect.getYaw(), ActionEffect.getPitch());
                    break;
                }
            }
        }
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
        String worldName = ActionEffect.mapName;
        if (worldName != null && !worldName.equalsIgnoreCase("noworld")) {
            OnlyOneLine.config.get(worldName, "crawl", false).set(canUseCrawl);
            OnlyOneLine.config.get(worldName, "doubleJump", false).set(canUseDoubleJump);
            OnlyOneLine.config.get(worldName, "tpY", -12345).set(yLimit);
            OnlyOneLine.config.get(worldName, "tpYaw", 0).set(tpYaw);
            OnlyOneLine.config.get(worldName, "tpPitch", 0).set(tpPitch);
        }
    }

    public void load() {

        for (String category : OnlyOneLine.config.getCategoryNames()) {
            if (category.equalsIgnoreCase(mapName))
                category = mapName;
            ConfigCategory action = OnlyOneLine.config.getCategory(category);
            canUseCrawl = action.get("crawl").getBoolean();
            canUseDoubleJump = action.get("doubleJump").getBoolean();
            yLimit = action.get("tpY").getDouble();
            tpYaw = (float) action.get("tpYaw").getDouble();
            tpPitch = (float) action.get("tpPitch").getDouble();
        }
    }
}


