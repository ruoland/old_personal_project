package ruo.asdfrpg.skill.system;

import ruo.asdfrpg.skill.*;
import ruo.asdfrpg.skill.defaultskill.SkillCampFire;
import ruo.asdfrpg.skill.defaultskill.SkillCook;
import ruo.asdfrpg.skill.defaultskill.SkillRiding;
import ruo.asdfrpg.skill.defaultskill.SkillVillageReturn;

public class Skills {
    public static final Skill BLOCK_GRAB = new Skill().setX(1).setUnlocalizedName("BLOCK_GRAB").setLocalizedName("블럭 잡기").setMaxLevel(1);
    public static final Skill DOUBLE_JUMP = new Skill().setX(2).setUnlocalizedName("DOUBLE_JUMP").setLocalizedName("더블 점프").setMaxLevel(5);
    public static final Skill FLY = new SkillFly().setX(3).setUnlocalizedName("FLY").setLocalizedName("플라이");
    public static final Skill RIDING = new SkillRiding().setX(4).setMaxExp(2).setUnlocalizedName("RIDING").setLocalizedName("라이딩");

    public static final Skill BONUS_ATTACK = new SkillBonusAttack().setX(4).setUnlocalizedName("BONUS_ATTACK").setLocalizedName("추가 공격");

    public static final Skill CAMP_FIRE = new SkillCampFire().setX(1).setY(2).setUnlocalizedName("CAMP_FIRE").setLocalizedName("캠프파이어");
    public static final Skill COOKED = new SkillCook().setX(2).setY(2).setUnlocalizedName("COOKED").setLocalizedName("요리");
    public static final Skill VILLAGE_RETURN = new SkillVillageReturn().setX(3).setY(2).setUnlocalizedName("VILLAGE_RETURN").setLocalizedName("마을 귀환");
    public static final Skill LIGHT = new SkillLight().setX(4).setY(2).setUnlocalizedName("LIGHT").setLocalizedName("라이트");
    public static final Skill THROW_BLOCK = new SkillBlockThrowAttack().setX(5).setY(2).setUnlocalizedName("THROW_BLOCK").setLocalizedName("블럭 던지기");
    public static final Skill IRON_BODY = new SkillIronBody().setX(6).setY(2).setUnlocalizedName("IRON_BODY").setLocalizedName("스탠스");
    public static final Skill WIND_FLOAT = new SkillWindUP().setX(7).setY(2).setUnlocalizedName("WIND_FLOAT").setLocalizedName("바람으로 띄우기");
    public static final Skill WIND_DROP = new SkillWindDown().setX(7).setY(3).setUnlocalizedName("WIND_DROP").setLocalizedName("내리기");

    public static void register(){
        SkillHelper.registerSkill(BLOCK_GRAB);
        SkillHelper.registerSkill(DOUBLE_JUMP);
        SkillHelper.registerSkill(FLY);
        SkillHelper.registerSkill(RIDING);
        SkillHelper.registerSkill(CAMP_FIRE);
        SkillHelper.registerSkill(COOKED);
        SkillHelper.registerSkill(VILLAGE_RETURN);
        SkillHelper.registerSkill(BONUS_ATTACK);
        SkillHelper.registerSkill(LIGHT);
        SkillHelper.registerSkill(THROW_BLOCK);
        SkillHelper.registerSkill(IRON_BODY);
        SkillHelper.registerSkill(WIND_FLOAT);
        SkillHelper.registerSkill(WIND_DROP);
    }

    public static Skill valueOf(String skillName){
        return SkillHelper.getSkill(skillName);

    }
}
