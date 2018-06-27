package ruo.minigame.action;

import net.minecraftforge.common.config.ConfigCategory;
import ruo.minigame.MiniGame;
import ruo.minigame.api.WorldAPI;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class ActionData2 implements Serializable {

    protected static ArrayList<String> crawlMapList =  new ArrayList<>();
    protected static HashMap<String, Boolean> inWaterMap = new HashMap<>();//물 속에 있는 효과를 내거나 물 속에 없는 효과를 냄
    protected static ArrayList<String> doubleJumpList  = new ArrayList<>();
    protected static HashMap<String, Double> tpYMap = new HashMap<>();
    protected static HashMap<String, Float> tpYawMap = new HashMap<>();
    protected static HashMap<String, Float> tpPitchMap = new HashMap<>();
    public static void save(){
        String worldName = WorldAPI.getCurrentWorldName();
        MiniGame.instance.minigameConfig.get(worldName, "crawl", false).set(crawlMapList.contains(worldName));
        MiniGame.instance.minigameConfig.get(worldName, "inWater", false).set(inWaterMap.containsKey(worldName));
        MiniGame.instance.minigameConfig.get(worldName, "doubleJump", false).set(doubleJumpList.contains(worldName));
        if(tpYMap.containsKey(worldName)) {
            MiniGame.instance.minigameConfig.get(worldName, "tpY", 0).set(tpYMap.get(worldName));
            MiniGame.instance.minigameConfig.get(worldName, "tpYaw", 0).set(tpYawMap.get(worldName));
            MiniGame.instance.minigameConfig.get(worldName, "tpPitch", 0).set(tpPitchMap.get(worldName));
        }
    }

    public static void load(){
        for(String category : MiniGame.instance.minigameConfig.getCategoryNames()){
            ConfigCategory action = MiniGame.instance.minigameConfig.getCategory(category);
            if(action.containsKey("crawl") && action.get("crawl").getBoolean())
                crawlMapList.add(category);
            if(action.containsKey("inWater"))
                inWaterMap.put(category, action.get("inWater").getBoolean());
            if(action.containsKey("doubleJump"))
                doubleJumpList.add(category);
            if(action.containsKey("tpY")){
                tpYMap.put(category, action.get("tpY").getDouble());
                tpYawMap.put(category, (float)action.get("tpYaw").getDouble());
                tpPitchMap.put(category, (float) action.get("tpPitch").getDouble());
            }
        }
    }
}
