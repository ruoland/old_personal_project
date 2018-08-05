package ruo.asdfrpg.skill;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class SkillStack {
    private NBTTagCompound tagCompound = new NBTTagCompound();
    private EntityPlayer player;
    private Skill skill;
    private int level, exp;
    public SkillStack(EntityPlayer player, Skill skill) {
        this.player = player;
        this.skill = skill;
    }

    public Skill getSkill() {
        return skill;
    }

    public int getLevel() {
        return level;
    }

    public void addLevel() {
        level++;
    }

    public void addExp() {
        exp+=3;
        if (exp >= skill.maxExp()) {
            addLevel();
            exp = 0;
            System.out.println(getSkill().getLocalizedName()+" 스킬의 레벨이 올랐습니다 "+level);
        }
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public int getExp() {
        return exp;
    }
    public int getMaxExp() {
        return skill.maxExp();
    }

    public boolean isUnlock() {
        return getLevel() > 0;
    }

    public void onEffect(int data){
        getSkill().onEffect(this, data);
    }

    public EntityPlayer getPlayer() {
        return player;
    }

    public NBTTagCompound serializeNBT()
    {
        this.writeToNBT();
        return tagCompound;
    }

    public void writeToNBT(){
        tagCompound.setInteger("EXP",getExp());
        tagCompound.setInteger("LEVEL",getLevel());
    }

    public NBTTagCompound getTagCompound() {
        return tagCompound;
    }

    public void readFromNBT(NBTTagCompound tagCompound){
        setExp(tagCompound.getInteger("EXP"));
        setLevel(tagCompound.getInteger("LEVEL"));
    }
}
