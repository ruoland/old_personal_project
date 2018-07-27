package ruo.asdfrpg.skill;

import net.minecraft.item.Item;

public class Skills {
    public static final Skill BLOCK_GRAB = new Skill().setUnlocalizedName("BLOCK_GRAB").setLocalizedName("블럭 잡기").setMaxLevel(1);
    public static final Skill DOUBLE_JUMP = new Skill().setUnlocalizedName("DOUBLE_JUMP").setLocalizedName("더블 점프").setMaxLevel(5);
    public static final Skill CAMP_FIRE = new Skill().setUnlocalizedName("CAMP_FIRE").setLocalizedName("캠프파이어");
    public static final Skill COOKED = new Skill().setUnlocalizedName("COOKED").setLocalizedName("요리");
    public static final Skill FLY = new SkillFly().setUnlocalizedName("FLY");
    public static void register(){
        SkillHelper.registerSkill(BLOCK_GRAB);
        SkillHelper.registerSkill(DOUBLE_JUMP);
        SkillHelper.registerSkill(FLY);
        SkillHelper.registerSkill(CAMP_FIRE);
        SkillHelper.registerSkill(COOKED);
    }

}
