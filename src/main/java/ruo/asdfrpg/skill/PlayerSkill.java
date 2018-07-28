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

    public EntityPlayer getPlayer() {
        return WorldAPI.getPlayerByUUID(uuid.toString());
    }

    public UUID getName() {
        return uuid;
    }

    public void useSkill(Skill skill) {
        if(isRegister(skill)) {
            getSkill(skill).addExp();
            getSkill(skill).onEffect();
        }
    }

    public NBTTagCompound writeToNBT(){
        NBTTagCompound tagCompound = new NBTTagCompound();
        for(SkillStack skillStack : skillList) {
            NBTTagCompound stackCompound = new NBTTagCompound();
            stackCompound.setInteger("EXP",skillStack.getExp());
            stackCompound.setInteger("LEVEL",skillStack.getLevel());
            tagCompound.setTag(skillStack.getSkill().getUnlocalizedName(), stackCompound);
        }
        return tagCompound;
    }
}