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
                System.out.println("5555 레시피 일치");
                SkillHelper.getPlayerSkill(player).useSkill(Skills.COOKED);
                for (int i = 0; i < itemList.size(); i++) {
                    if (i == 0) {
                        System.out.println("6666 레시피 일치"+!player.worldObj.isRemote);
                        EntityItem cooking = entityItems.get(0);
                        cooking.setDead();
                        cookedRecipe.getValue().stackSize = itemList.size();
                        EntityItem item = new EntityItem(WorldAPI.getWorld(), cooking.posX, cooking.posY, cooking.posZ, cookedRecipe.getValue());

                        WorldAPI.getWorld().spawnEntityInWorld(item);

                    }else if(entityItems.get(i).worldObj.isRemote) {
                        System.out.println("6666 아이템 삭제"+ entityItems.get(i));
                        entityItems.get(i).setDead();
                    }
                }
            }
        }
    }
}
