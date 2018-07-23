package ruo.asdfrpg.skill;

import ruo.minigame.action.ActionEffect;

public class SkillDoubleJump extends Skill {

    public SkillDoubleJump() {
        this.setLocalizedName("더블 점프");
    }

    @Override
    public int maxLevel() {
        return 5;
    }

    @Override
    public int minLevel() {
        return 0;
    }

    @Override
    public void onEffect() {
        ActionEffect.doubleJump(true);
    }
}
