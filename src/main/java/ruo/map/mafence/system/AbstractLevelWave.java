package ruo.map.mafence.system;

public abstract class AbstractLevelWave {
    public abstract int level();
    public abstract int maxWave();
    public abstract void startWave(int wave);
    public void endWave(){
    }
}
