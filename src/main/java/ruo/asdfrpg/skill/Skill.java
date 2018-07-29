package ruo.asdfrpg.skill;

import net.minecraft.nbt.NBTTagCompound;

public class Skill {
    private String localizedName, unlocalizedName;
    private int maxLevel = 5, maxExp = 30;
    private double lineX = 30, lineY = 30;
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
    public void onEffect(SkillStack playerSkill, int data){

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
    public double lineX(){
        return lineX;
    }

    //값이 낮으면 스킬 창에서 스킬이 위로 이동함
    public double lineY(){
        return lineY;
    }

    public Skill setX(int x) {
        this.lineX = 30 + (x * 40);
        return this;
    }
    public Skill setY(int y) {
        this.lineY = 30 + (y * 40);
        return this;
    }
}
