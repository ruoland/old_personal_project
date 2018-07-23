package ruo.asdfrpg.skill;

public abstract class Skill {
    private int level;
    private String localizedName;
    public Skill(){
    }

    public int getLevel() {
        return level;
    }

    public void setLocalizedName(String localizedName) {
        this.localizedName = localizedName;
    }

    public String getUnlocalizedName() {
        return getClass().getSimpleName().replaceFirst("Skill", "");
    }

    public String getLocalizedName() {
        return localizedName;
    }
    public void addLevel(){
        level++;
    }

    public boolean isUnlock(){
        return getLevel() > 0;
    }
    public abstract int maxLevel();
    public abstract int minLevel();
    public abstract void onEffect();

    //값이 낮으면 스킬 창에서 스킬이 왼쪽으로 이동함
    public int lineX(){
        return 30;
    }

    //값이 낮으면 스킬 창에서 스킬이 위로 이동함
    public int lineY(){
        return 30;
    }



}
