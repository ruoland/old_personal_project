package ruo.asdfrpg.skill.system;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import ruo.minigame.api.WorldAPI;

import java.util.ArrayList;
import java.util.UUID;

public class PlayerSkill {
    private UUID uuid;
    private ArrayList<SkillStack> skillList = new ArrayList();
    private int playerLevel = 1;
    private float playerExp = 8;

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

    public float getPlayerExp() {
        return playerExp;
    }

    public int getPlayerLevel() {
        return playerLevel;
    }

    public void addPlayerExp(int i){
        playerExp += i;
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
        tagCompound.setInteger("playerLevel", playerLevel);
        tagCompound.setFloat("playerExp", playerExp);

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
        playerLevel = compound.getInteger("playerLevel");
        playerExp = compound.getFloat("playerExp");
        playerExp = 10;
    }

}