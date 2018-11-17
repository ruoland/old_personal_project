package ruo.map.mafence.system;

import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import ruo.minigame.effect.AbstractTick;
import ruo.minigame.effect.TickRegister;

import java.util.HashMap;
import java.util.Map;

public class MafenceSystem {
    private static int curLevel, curWave;
    private static int LIFE_POINT;
    private static MafenceSystem system;
    private static BlockPos monsterSpawnPos;
    private static Map<Integer, AbstractLevelWave> levelWaveMap = new HashMap<>();

    public static MafenceSystem getInstance() {
        if (system == null)
            system = new MafenceSystem();
        return system;
    }

    public void registerLevelWave(AbstractLevelWave levelWave) {
        levelWaveMap.put(levelWave.level(), levelWave);
    }

    public void start() {
        TickRegister.register(new AbstractTick(100, false) {
            @Override
            public void run(TickEvent.Type type) {
                AbstractLevelWave wave = levelWaveMap.get(curLevel);
                if(!wave.waveStart) {
                    wave.startWave(curLevel);
                    return;
                }
                if(!wave.waveEnd){
                    return;
                }
                curLevel++;
            }
        });
    }


    public void lifePointAdd() {
        LIFE_POINT++;
    }

    public void lifePointSub() {
        LIFE_POINT--;
        if (LIFE_POINT == 0) {
            System.out.println("게임 끝");
        }
    }

    public void setMonsterSpawn(BlockPos pos) {
        monsterSpawnPos = pos;
    }

}
