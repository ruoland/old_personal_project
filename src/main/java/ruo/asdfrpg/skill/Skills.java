package ruo.asdfrpg.skill;

public class Skills {
    public static final Skill BLOCK_GRAB = new Skill().setX(1).setUnlocalizedName("BLOCK_GRAB").setLocalizedName("블럭 잡기").setMaxLevel(1);
    public static final Skill DOUBLE_JUMP = new Skill().setX(2).setUnlocalizedName("DOUBLE_JUMP").setLocalizedName("더블 점프").setMaxLevel(5);
    public static final Skill FLY = new SkillFly().setX(3).setUnlocalizedName("FLY");
    public static final Skill RIDING = new SkillRiding().setX(4).setMaxExp(2).setUnlocalizedName("RIDING").setLocalizedName("라이딩");

    public static final Skill AUTO_ATTACK = new SkillAutoAttack().setX(4).setUnlocalizedName("AUTO_ATTACK").setLocalizedName("자동 공격");

    public static final Skill CAMP_FIRE = new Skill().setY(2).setX(1).setUnlocalizedName("CAMP_FIRE").setLocalizedName("캠프파이어");
    public static final Skill COOKED = new Skill().setY(2).setX(2).setUnlocalizedName("COOKED").setLocalizedName("요리");
    public static final Skill VILLAGE_RETURN = new SkillVillageReturn().setY(2).setX(3).setUnlocalizedName("VILLAGE_RETURN").setLocalizedName("마을 귀환");
    public static final Skill LIGHT = new SkillLight().setY(2).setX(4).setUnlocalizedName("LIGHT").setLocalizedName("라이트");

    public static final Skill THROW_BLOCK = new SkillBlockThrowAttack().setY(2).setX(5).setUnlocalizedName("THROW_BLOCK").setLocalizedName("블럭 던지기");
    public static final Skill IRON_BODY = new SkillIronBody().setY(2).setX(6).setUnlocalizedName("IRON_BODY").setLocalizedName("스탠스");

    public static void register(){
        SkillHelper.registerSkill(BLOCK_GRAB);
        SkillHelper.registerSkill(DOUBLE_JUMP);
        SkillHelper.registerSkill(FLY);
        SkillHelper.registerSkill(RIDING);
        SkillHelper.registerSkill(CAMP_FIRE);
        SkillHelper.registerSkill(COOKED);
        SkillHelper.registerSkill(VILLAGE_RETURN);
        SkillHelper.registerSkill(AUTO_ATTACK);
        SkillHelper.registerSkill(LIGHT);
        SkillHelper.registerSkill(THROW_BLOCK);
        SkillHelper.registerSkill(IRON_BODY);
    }

    public static Skill valueOf(String skillName){
        return SkillHelper.getSkill(skillName);

    }
}
