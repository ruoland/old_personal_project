package ruo.asdfrpg.skill;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class CookedRecipeHelper {
    private static ArrayList<CookedRecipe> cookedRecipes = new ArrayList<>();

    public static void registerRecipe(CookedRecipe cookedRecipe) {
        cookedRecipes.add(cookedRecipe);
    }

    public static void cooking(EntityPlayer player, List<EntityItem> entityItems, List<ItemStack> itemList) {
        for (CookedRecipe cookedRecipe : cookedRecipes) {
            System.out.println("2222 레시피 " + cookedRecipe.getValue() + " - " + cookedRecipe.getItemList());
            if (cookedRecipe.checkRecipe(itemList)) {
                System.out.println("5555 레시피 일치" + entityItems + itemList);
                SkillHelper.getPlayerSkill(player).useSkill(Skills.COOKED);
                EntityItem entityItem = entityItems.get(0);
                ItemStack cookedStack = cookedRecipe.getValue();
                System.out.println("6666 " + cookedStack.stackSize + " - " + entityItem.getEntityItem().stackSize);
                cookedStack.stackSize = entityItem.getEntityItem().stackSize;
                entityItem.setEntityItemStack(cookedStack);
                for (int i = 1; i < itemList.size(); i++) {
                    entityItems.get(i).setDead();
                    ;
                }
            }
        }
    }
}
