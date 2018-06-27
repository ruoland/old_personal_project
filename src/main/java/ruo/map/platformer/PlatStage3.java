package ruo.map.platformer;

import net.minecraft.init.Blocks;
import ruo.map.platformer.chapter1.EntityBossHorse;
import ruo.minigame.api.ScriptAPI;
import ruo.minigame.api.WorldAPI;

public class PlatStage3 extends PlatStage {
    private ScriptAPI.Script stage1;
    @Override
    public void start() {
        PlatEffect.spawnEntity(new EntityBossHorse(WorldAPI.getWorld()), -39.4, 70, -16.2);
        PlatEffect.spawnPlatBlock(WorldAPI.getWorld(), -20, 40, -54, -20, 36, -51, Blocks.COBBLESTONE, true);
        resetAndSpawn();
    }

    public void end(){
    }

    public void runScript(String name){
        stage1.runNoThreadFunction(name);
    }
}
