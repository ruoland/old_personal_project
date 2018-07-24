package ruo.asdfrpg.skill;

import ibxm.Player;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class SkillHelper {
    private static HashMap<String, PlayerSkill> playerSkill = new HashMap<>();
    private static HashMap<String, Skill> registerSkills = new HashMap<>();

    public static PlayerSkill getPlayerSkill(String username){
        return playerSkill.get(username);
    }
    public static void init(EntityPlayer player){
        playerSkill.put(player.getCustomNameTag(), new PlayerSkill(player.getCustomNameTag()));
    }
    public static void registerSkill(Skill skill){
        registerSkills.put(skill.getUnlocalizedName(), skill);
    }

    public static Collection<Skill> getSkillList(){
        return registerSkills.values();
    }

    public static Skill getSkill(String name){
        return registerSkills.get(name);
    }


    public static class PlayerSkill{
        private String name;
        private ArrayList<Skill> skillList = new ArrayList();
        public PlayerSkill(String user){
            name = user;
        }

        public void addSkill(Skill skill){
            Skill skill1 = getSkill(skill.getLocalizedName());
            if(skill1 != null){
                skill1.addLevel();
            }
            skillList.add(skill);
        }

        public Skill getSkill(String name){
            for(Skill str : skillList){
                if(str.getUnlocalizedName().equalsIgnoreCase(name)){
                    return str;
                }
            }
            return null;
        }
        public boolean isUnlock(String name){
            return getSkill(name).isUnlock();
        }

        public String getName() {
            return name;
        }
    }
}
