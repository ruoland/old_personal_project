package ruo.asdfrpg.skill;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class SkillStack {
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
        exp++;
        if (exp >= skill.maxExp()) {
            addLevel();
            exp = 0;
        }
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

    public void onEffect(){
        getSkill().onEffect(this);
    }

    public EntityPlayer getPlayer() {
        return player;
    }
}
