package map.platformer;

import map.platformer.chapter1.EntityCowBumo;
import minigameLib.api.ScriptAPI;
import minigameLib.api.WorldAPI;
import net.minecraft.init.Blocks;
import map.platformer.chapter1.EntityBabyCow;
import map.platformer.chapter1.EntityBossHorse;

public class PlatStage1 extends PlatStage {
    private ScriptAPI.Script stage1;
    @Override
    public void start() {
        EntityBabyCow babyCow = (EntityBabyCow) PlatEffect.spawnEntity(new EntityBabyCow(WorldAPI.getWorld()), -39.4, 70, -16.2);
        EntityBossHorse bossHorse = (EntityBossHorse) PlatEffect.spawnEntity(new EntityBossHorse(WorldAPI.getWorld()), -39.4, 70, -16.2);
        PlatEffect.spawnEntity(new EntityCowBumo(WorldAPI.getWorld()), 32.4D, 12D, 3.2D).setCustomNameTag("cowbumo");
        PlatEffect.spawnEntity(new EntityCowBumo(WorldAPI.getWorld()),34.4, 12, 3.2).setCustomNameTag("cowbumo2");
        PlatEffect.spawnPlatBlock(WorldAPI.getWorld(), -20, 40, -54, -20, 36, -51, Blocks.COBBLESTONE, true);
        PlatEffect.spawnPlatBlock(WorldAPI.getWorld(), -19, 40, -53, -20, 36, -50, Blocks.COBBLESTONE, true);
        PlatEffect.spawnPlatBlock(WorldAPI.getWorld(), -37, 50, -43, -38, 52, -45, Blocks.COBBLESTONE, false);
        PlatEffect.spawnBigBlock(Blocks.COBBLESTONE, -36.621, 44.116, -51.98);
        stage1 = ScriptAPI.createScript("stage1", true);
        stage1.addFunction("start", "", "");
        stage1.addObject("plat", new PlatEffect());
        babyCow.startRiding(bossHorse);
        resetAndSpawn();
    }

    public void end(){
    }

    public void runScript(String name){
        stage1.runNoThreadFunction(name);
    }
}
