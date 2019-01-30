package rmap.platformer;

import minigameLib.api.ScriptAPI;
import minigameLib.api.WorldAPI;
import net.minecraft.init.Blocks;
import rmap.platformer.chapter1.EntityBossHorse;

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
