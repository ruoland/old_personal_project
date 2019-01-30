package rmap.platformer;

import minigameLib.api.ScriptAPI;
import minigameLib.api.WorldAPI;
import minigameLib.map.EntityDefaultNPC;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import rmap.platformer.chapter2.EntityChickenShopKeeper;

public class PlatStage2 extends PlatStage {
    private ScriptAPI.Script stage1;
    private static boolean c2Game;
    private static BlockPos c2pos;

    @Override
    public void start() {
        PlatEffect.spawnEntity(new EntityChickenShopKeeper(WorldAPI.getWorld()), -39.4, 70, -16.2);
        PlatEffect.spawnPlatBlock(WorldAPI.getWorld(), -20, 40, -54, -20, 36, -51, Blocks.COBBLESTONE, true);
        resetAndSpawn();
    }

    public void end(){
    }

    public static void flyGameStart() {
        if (!c2Game) {
            c2Game = true;
            EntityChickenShopKeeper shopKeeper = (EntityChickenShopKeeper) EntityDefaultNPC.getNPC("치킨샵키퍼");
            c2pos = shopKeeper.getPosition();
        }
    }

    public static void flyGameEnd() {
        if (c2Game) {
            c2Game = false;
            double distance = WorldAPI.getPlayer().getPosition().getDistance(c2pos.getX(), c2pos.getY(), c2pos.getZ());
            EntityChickenShopKeeper shopKeeper = (EntityChickenShopKeeper) EntityDefaultNPC.getNPC("치킨샵키퍼");
            shopKeeper.eft.addChat(0, PlatEffect.format("lang","plat.c1.stage2.end1", distance));
            shopKeeper.eft.addChat(2000, PlatEffect.format("lang","plat.c1.stage2.end2", distance));
            shopKeeper.completeMiniGame(distance);
        }
    }

    public static boolean isFlyStart() {
        return c2Game;
    }

    public void runScript(String name){
        stage1.runNoThreadFunction(name);
    }
}
