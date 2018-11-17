package ruo.map.mafence.system;

import net.minecraft.entity.Entity;
import ruo.map.mafence.EntityZombieMonster;
import ruo.minigame.api.WorldAPI;

public abstract class AbstractLevelWave {
    public boolean waveEnd, waveStart;
    private double[][] spawnPos = new double[3][3];

    public abstract int level();

    public abstract int maxWave();

    public void startWave(int wave) {
        waveStart = true;
    }

    public void endWave() {
        waveEnd = true;
        spawnPos = new double[3][3];
    }

    public void setSpawnPos(int i, double[] spawn) {
        if (spawnPos[i] == null) {
            spawnPos[i] = spawn;
        }else{
            System.out.println("이미 설정되어 있습니다.");
        }
    }

    public void spawnZombie(int spawnLevel) {
        EntityZombieMonster zombieMonster = new EntityZombieMonster(WorldAPI.getWorld());
        spawn(spawnLevel, zombieMonster);
    }

    private void spawn(int spawnLevel, Entity entity) {
        double[] spawn = spawnPos[spawnLevel];
        entity.setPosition(spawn[0], spawn[1], spawn[2]);
        WorldAPI.getWorld().spawnEntityInWorld(entity);
    }
}
