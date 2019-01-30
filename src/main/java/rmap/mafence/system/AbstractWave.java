package rmap.mafence.system;

import minigameLib.api.WorldAPI;
import minigameLib.effect.AbstractTick;
import minigameLib.effect.TickRegister;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import rmap.mafence.EntityMonster;
import rmap.mafence.EntityZombieMonster;

import java.util.ArrayList;

public abstract class AbstractWave {
    private double[] spawnPos = new double[3];
    private ArrayList<EntityMonster> monsterArrayList = new ArrayList<>();
    public abstract int level();

    public void startWave() {
        TickRegister.register(new AbstractTick(TickEvent.Type.SERVER, 10, true) {
            @Override
            public void run(TickEvent.Type type) {
                if(isEndWave()){
                    absLoop = false;
                    endWave();
                }
            }
        });
    }

    public void endWave() {

    }

    public int getLifePoint(){
        return 10;
    }
    public void setSpawnPos(double[] spawn) {
        if (spawnPos == null) {
            spawnPos = spawn;
        }else{
            System.out.println("이미 설정되어 있습니다.");
        }
    }

    public void spawnZombie() {
        EntityZombieMonster zombieMonster = new EntityZombieMonster(WorldAPI.getWorld());
        spawn(zombieMonster);
    }

    private void spawn(EntityMonster entity) {
        double[] spawn = spawnPos;
        entity.setPosition(spawn[0], spawn[1], spawn[2]);
        WorldAPI.getWorld().spawnEntityInWorld(entity);
        monsterArrayList.add(entity);
    }

    //모든 몬스터가 죽었는지 확인하는 코드
    private boolean isEndWave(){
        for(EntityMonster monster : monsterArrayList){
            if(monster.isDead)
                continue;
            else
                return false;
        }
        return true;
    }
}
