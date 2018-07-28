package ruo.asdfrpg.skill;

import ibxm.Player;
import net.minecraft.entity.player.EntityPlayer;
import ruo.minigame.api.WorldAPI;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class SkillHelper {
    private static HashMap<UUID, PlayerSkill> playerSkill = new HashMap<>();
    private static HashMap<String, Skill> registerSkills = new HashMap<>();

    public static PlayerSkill getPlayerSkill(UUID username) {
        return playerSkill.get(username);
    }

    public static PlayerSkill getPlayerSkill(EntityPlayer player) {
        return getPlayerSkill(player.getUniqueID());
    }

    public static void registerSkill(EntityPlayer player, Skill skill){
        getPlayerSkill(player).registerSkill(skill);
    }
    public static void init(EntityPlayer player) {
        playerSkill.put(player.getUniqueID(), new PlayerSkill(player.getUniqueID()));
    }

    public static void registerSkill(Skill skill) {
        registerSkills.put(skill.getUnlocalizedName(), skill);
    }

    public static Collection<Skill> getSkillList() {
        return registerSkills.values();
    }

    public static Skill getSkill(String name) {
        return registerSkills.get(name);
    }

}
