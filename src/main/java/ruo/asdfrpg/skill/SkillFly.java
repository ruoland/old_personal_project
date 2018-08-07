package ruo.asdfrpg.skill;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.PotionTypes;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import ruo.asdfrpg.AsdfRPG;
import ruo.minigame.api.WorldAPI;

public class SkillFly extends Skill {

    public SkillFly() {
        this.setLocalizedName("하늘 날기");
    }

    @Override
    public int maxLevel() {
        return 5;
    }

    @Override
    public void onEffect(SkillStack skillStack, int data) {
        skillStack.getPlayer().addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("fly"), skillStack.getLevel() * 100));
    }
}
