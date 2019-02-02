package map.platformer;

public abstract class PlatStage {
    public boolean isEnd = false;
    public abstract void start();
    public abstract void end();

    public void runScript(String name){
    }
    public void resetAndSpawn(){
        PlatEffect.init();
    }

}
