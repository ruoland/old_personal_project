package ruo.asdfrpg;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import ruo.asdfrpg.skill.SkillHelper;
import ruo.asdfrpg.skill.Skills;

import java.util.List;

public class ItemVillageReturn extends Item {

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        super.addInformation(stack, playerIn, tooltip, advanced);
        tooltip.add("마지막으로 등록한 마을로 돌아갈 수 있다.");
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        SkillHelper.getPlayerSkill(playerIn).useSkill(Skills.VILLAGE_RETURN, 1);
        return super.onItemRightClick(itemStackIn, worldIn, playerIn, hand);
    }
}
