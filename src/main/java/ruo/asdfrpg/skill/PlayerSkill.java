package ruo.asdfrpg.skill;

import java.util.Collection;
import java.util.HashMap;

public class PlayerSkill {

    private static HashMap<String, Skill> registerSkills = new HashMap<>();

    public static void registerSkill(Skill skill){
        registerSkills.put(skill.getUnlocalizedName(), skill);
    }

    public static Collection<Skill> getSkillList(){
        return registerSkills.values();
    }

    public static Skill getSkill(String name){
        return registerSkills.get(name);
    }

    public static boolean isUnlock(String name){
        return registerSkills.get(name).isUnlock();
    }
}
