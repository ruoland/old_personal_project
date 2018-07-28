package ruo.asdfrpg.skill;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import ruo.minigame.api.WorldAPI;
import ruo.minigame.effect.AbstractTick;
import ruo.minigame.effect.TickRegister;

import java.util.ArrayList;
import java.util.List;

public class CookedRecipeHelper {
    private static ArrayList<CookedRecipe> cookedRecipes = new ArrayList<>();

    public static void registerRecipe(CookedRecipe cookedRecipe) {
        cookedRecipes.add(cookedRecipe);
    }

    public static void cooking(EntityPlayer player, List<EntityItem> entityItems, List<ItemStack> itemList) {
        for (CookedRecipe cookedRecipe : cookedRecipes) {
            System.out.println("2222 레시피 "+cookedRecipe.getValue()+" - "+cookedRecipe.getItemList());
            if (cookedRecipe.checkRecipe(itemList)) {
                System.out.println("5555 레시피 일치"+entityItems+itemList);
                SkillHelper.getPlayerSkill(player).useSkill(Skills.COOKED);
                for (int i = 0; i < itemList.size(); i++) {
                    EntityItem cooking = entityItems.get(i);
                    ItemStack cooked = cookedRecipe.getValue();
                    System.out.println("6666 "+cooked.stackSize+" - "+cooking.getEntityItem().stackSize);
                    cooked.stackSize = cooking.getEntityItem().stackSize;
                    cooking.setEntityItemStack(cooked);
                }
            }
        }
    }
}
