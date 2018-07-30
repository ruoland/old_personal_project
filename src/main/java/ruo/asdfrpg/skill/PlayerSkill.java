package ruo.asdfrpg.skill;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import ruo.minigame.api.WorldAPI;

import java.util.ArrayList;
import java.util.UUID;

public class PlayerSkill {
    private UUID uuid;
    private ArrayList<SkillStack> skillList = new ArrayList();

    public PlayerSkill(UUID user) {
        uuid = user;
    }

    public void registerSkill(Skill skill) {
        SkillStack skillStack = new SkillStack(getPlayer(), skill);
        skillStack.addLevel();
        skillList.add(skillStack);
        System.out.println(skillStack+" - "+skill);
    }

    public SkillStack getSkill(Skill skill) {
        for (SkillStack skillStack : skillList) {
            if (skillStack.getSkill() == skill) {
                return skillStack;
            }
        }
        return null;
    }

    public boolean isRegister(Skill skill){
        return getSkill(skill) != null;
    }
    public boolean isUnlock(Skill skill) {
        return getSkill(skill).isUnlock();
    }

    public ArrayList<SkillStack> getSkillList() {
        return skillList;
    }

    public EntityPlayer getPlayer() {
        return WorldAPI.getPlayerByUUID(uuid.toString());
    }

    public UUID getName() {
        return uuid;
    }

    public void useSkill(Skill skill, int data) {
        if(isRegister(skill)) {
            getSkill(skill).addExp();
            getSkill(skill).onEffect(data);
        }
    }
    public void useSkill(Skill skill) {
        useSkill(skill, 0);
    }

    public NBTTagCompound writeToNBT(){
        NBTTagCompound tagCompound = new NBTTagCompound();
        for(SkillStack skillStack : skillList)
        {
            System.out.println(skillStack.getSkill());
            if(skillStack.getSkill() != null)
            tagCompound.setTag(skillStack.getSkill().getUnlocalizedName(), skillStack.serializeNBT());
        }
        return tagCompound;
    }

    public void readToNBT(NBTTagCompound compound){
        for(Skill skill : SkillHelper.getSkillList()){
            if(compound.hasKey(skill.getUnlocalizedName())){
                NBTTagCompound stackCompound = compound.getCompoundTag(skill.getUnlocalizedName());
                registerSkill(skill);
                getSkill(skill).readFromNBT(stackCompound);
            }
        }
    }

}