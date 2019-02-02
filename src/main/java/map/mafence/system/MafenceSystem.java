package map.mafence.system;

import map.mafence.EntityMonster;

import java.util.HashMap;
import java.util.Map;

public class MafenceSystem {
    private static MafenceSystem system;

    private static int curWave;
    private int lifePoint;
    private Map<Integer, AbstractWave> waveMap = new HashMap<>();
    public boolean waveRunning;


    public static MafenceSystem getInstance() {
        if (system == null)
            system = new MafenceSystem();
        return system;
    }

    public void registerWave(AbstractWave levelWave) {
        waveMap.put(levelWave.level(), levelWave);
    }

    public void waveStart() {
        AbstractWave wave = waveMap.get(curWave);
        if(!waveRunning) {//웨이브가 시작되지 않은 경우 시작함
            wave.startWave();
            waveRunning = true;
            lifePoint = wave.getLifePoint();
            curWave++;
            return;
        }
    }

    public void waveEnd(){
        curWave++;
    }

    public void attackToPlayer(EntityMonster monster){
        lifePoint--;
        monster.setDead();
        if (lifePoint == 0) {
            System.out.println("게임 끝");
        }
    }

    public void setLifePoint(int lifePoint) {
        this.lifePoint = lifePoint;
    }

}
