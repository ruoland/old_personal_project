package ruo.asdfrpg.skill;

import net.minecraft.entity.player.EntityPlayer;
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
        skillList.add(new SkillStack(getPlayer(), skill));
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
}