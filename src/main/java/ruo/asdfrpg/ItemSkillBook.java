package ruo.asdfrpg;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import ruo.asdfrpg.skill.Skill;
import ruo.asdfrpg.skill.SkillHelper;
import ruo.asdfrpg.skill.Skills;

import java.util.List;

public class ItemSkillBook extends Item {
    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        SkillHelper.getPlayerSkill(playerIn).registerSkill(getSkill());
        System.out.println(getSkill());
        return super.onItemRightClick(itemStackIn, worldIn, playerIn, hand);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        super.addInformation(stack, playerIn, tooltip, advanced);
        tooltip.add(getSkill().getLocalizedName()+"를(을) 배울 수 있는 스킬북이다.");
    }

    public Skill getSkill(){
        return Skills.valueOf(getUnlocalizedName().replace("item.", "").replace(".name", ""));
    }

}
