package ruo.asdfrpg.skill;

import net.minecraft.entity.player.EntityPlayer;

public class SkillBlockGrab extends Skill {

    public SkillBlockGrab() {
        this.setLocalizedName("블럭 잡기");
    }

    @Override
    public int maxLevel() {
        return 1;
    }

    @Override
    public int minLevel() {
        return 0;
    }

    @Override
    public void onEffect(EntityPlayer player) {

    }
}
