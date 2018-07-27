package ruo.asdfrpg.skill;

public class Skill {
    private String localizedName, unlocalizedName;
    private int maxLevel = 5, maxExp = 10;
    public Skill(){
    }

    public String getUnlocalizedName() {
        return unlocalizedName;
    }

    public String getLocalizedName() {
        return localizedName;
    }

    public int maxExp() {
        return maxExp;
    }
    public int maxLevel(){
        return maxLevel;
    }
    public void onEffect(SkillStack playerSkill){

    }
    public Skill setUnlocalizedName(String name){
        this.unlocalizedName = name;
        return this;
    }
    public Skill setLocalizedName(String name){
        this.localizedName = name;
        return this;
    }
    public Skill setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
        return this;
    }

    public Skill setMaxExp(int maxExp) {
        this.maxExp = maxExp;
        return this;
    }

    //값이 낮으면 스킬 창에서 스킬이 왼쪽으로 이동함
    public int lineX(){
        return 30;
    }

    //값이 낮으면 스킬 창에서 스킬이 위로 이동함
    public int lineY(){
        return 30;
    }



}
