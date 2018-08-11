package ruo.asdfrpg.skill;

import net.minecraft.entity.player.EntityPlayer;
import ruo.minigame.api.NBTAPI;
import ruo.minigame.api.WorldAPI;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class SkillHelper {
    private static HashMap<UUID, PlayerSkill> playerSkillMap = new HashMap<>();
    private static HashMap<String, Skill> registerSkills = new HashMap<>();
    public static PlayerSkill getPlayerSkill() {
        return getPlayerSkill(WorldAPI.getPlayer());
    }

    public static PlayerSkill getPlayerSkill(UUID username) {
        return playerSkillMap.get(username);
    }

    public static PlayerSkill getPlayerSkill(EntityPlayer player) {
        return getPlayerSkill(player.getUniqueID());
    }

    public static void registerSkill(EntityPlayer player, Skill skill) {
        getPlayerSkill(player).registerSkill(skill);
    }

    public static void init(EntityPlayer player) {
        playerSkillMap.put(player.getUniqueID(), new PlayerSkill(player.getUniqueID()));
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

    public static void savePlayerSkill() {
        NBTAPI nbtapi = new NBTAPI("./asdfrpg.dat");
        nbtapi.readNBT();

        for (UUID uuid : playerSkillMap.keySet()) {
            PlayerSkill playerSkill = playerSkillMap.get(uuid);
            nbtapi.getNBT().setTag(playerSkill.getName().toString(), playerSkill.writeToNBT());

        }
        nbtapi.saveNBT();
    }

    public static void readPlayerSkill() {
        NBTAPI nbtapi = new NBTAPI("./asdfrpg.dat");
        nbtapi.readNBT();

        for (UUID uuid : playerSkillMap.keySet()) {
            PlayerSkill playerSkill = playerSkillMap.get(uuid);
            playerSkill.readToNBT(nbtapi.getNBT());
        }
    }
}
